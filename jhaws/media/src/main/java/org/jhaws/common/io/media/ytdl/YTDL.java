package org.jhaws.common.io.media.ytdl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.console.Processes.ExitValueException;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.Tool;
import org.jhaws.common.lang.StringValue;
import org.jhaws.common.lang.Value;

// https://github.com/ytdl-org/youtube-dl/blob/master/README.md
//
// youtube-dl.exe --list-extractors
//
// youtube-dl.exe --extractor-descriptions
//
// https://github.com/ytdl-org/youtube-dl
// https://ytdl-org.github.io/youtube-dl/supportedsites.html
// --write-info-json
//
// https://askubuntu.com/questions/673442/downloading-youtube-playlist-with-youtube-dl-skipping-existing-files
//
// --restrict-filenames Restrict filenames to only ASCII
// characters, and avoid "&" and spaces in
// filenames
// -w, --no-overwrites Do not overwrite files
// -c, --continue Force resume of partially downloaded files.
// By default, youtube-dl will resume
// downloads if possible.
// --write-info-json Write video metadata to a .info.json file
//
//
// cookies
// https://github.com/ytdl-org/youtube-dl/issues/26152
//
//
// Map<String, Object> extraConfig = new LinkedHashMap<>();
// extraConfig.put("--sub-lang", "english");
// extraConfig.put("--http-chunk-size", "1M");
// extraConfig.put("--buffer-size", "16K");
// extraConfig.put("--ffmpeg-location", ffmpegTool.getFfmpeg().getAbsolutePath());
// extraConfig.put("--write-info-json", null);
// extraConfig.put("--no-progress", null);
// if(ytdlp) {
// if (Utils.osgroup == OSGroup.Windows) {
// extraConfig.put("--windows-filenames", null);
// }
// extraConfig.put("--concurrent-fragments", "3");
// extraConfig.put("--cookies-from-browser", "firefox");
// }
public class YTDL extends Tool {
	public static String UNSUPPORTED = "ERROR: Unsupported URL: ";

	protected static final String UTF_8 = "utf-8";

	public static final String EXE = "youtube-dl";

	public static final String URL = "https://yt-dl.org/downloads/latest/";

	protected String userAgent;

	protected String escapeChar;

	protected boolean autoUpdate = true;

	public YTDL() {
		super(System.getenv("YOUTUBEDL"));
	}

	public YTDL(String s) {
		super(s);
	}

	public YTDL(boolean disableAuto) {
		super(disableAuto);
	}

	@Override
	public String toString() {
		return String.valueOf(executable);
	}

	@Override
	protected void setPathImpl(String path) {
		if (StringUtils.isBlank(path)) {
			new NullPointerException().printStackTrace();
			return;
		}
		FilePath f = new FilePath(path);
		if (f.exists()) {
			if (f.isFile()) {
				executable = f;
			} else {
				if (Utils.osgroup == OSGroup.Windows) {
					executable = f.child(EXE).appendExtension("exe");
				} else {
					executable = f.child(EXE);
				}
			}
		} else {
			f.createDirectory();
			if (Utils.osgroup == OSGroup.Windows) {
				executable = f.child(EXE).appendExtension("exe");
			} else {
				executable = f.child(EXE);
			}
		}

		if (executable.exists()) {
			FilePath update = executable.appendExtension("update");
			if (autoUpdate || update.notExists()
					|| (update.getLastModifiedTime().toMillis() + 24 * 3_600_000) < System.currentTimeMillis()) {
				Tool.call(Duration.ofSeconds(60), null, new Lines(), executable.getParentPath(),
						Arrays.asList(Tool.command(executable), "-U"), true, null, true, null);
				update.write("" + System.currentTimeMillis());
			}
		} else {
			String tmp = EXE;
			if (Utils.osgroup == OSGroup.Windows) {
				tmp = tmp + ".exe";
			}
			try (InputStream in = new URL(URL + tmp).openConnection().getInputStream();
					OutputStream out = executable.newBufferedOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException ex) {
				loggeri.error("{}", ex);
				return;
			}
		}
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = new ArrayList<>();
		command.add(Tool.command(executable));
		command.add("--version");
		Lines lines = new Lines();
		Tool.call(null, lines, executable.getParentPath(), command);
		return lines.lines().get(0);
	}

	public String fileName(String url, String cookiesLoc, Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		List<String> command = new ArrayList<>();
		command.add(Tool.command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		extraConfig(command, extraConfig);
		command.add("--get-filename");
		command.add(url);
		StringValue fn = new StringValue();
		List<String> full = new ArrayList<>();
		try {
			Lines lines = new Lines() {
				@Override
				public void accept(String t) {
					fn.set(t);
					full.add(t);
				}
			};
			call(null, lines, null, command);
			return fn.get();
		} catch (RuntimeException ex) {
			full.forEach(System.out::println);
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	public String metadata(String url, String cookiesLoc, Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		List<String> command = new ArrayList<>();
		command.add(Tool.command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		extraConfig(command, extraConfig);
		command.add("--write-info-json");
		command.add("--skip-download");
		command.add("--no-clean-info-json");
		command.add(url);
		StringValue fn = new StringValue();
		List<String> full = new ArrayList<>();
		try {
			Lines l = new Lines() {
				@Override
				public void accept(String t) {
					String pre = "[info] Writing video metadata as JSON to: ";
					if (t.startsWith(pre)) {
						lines.add(t.substring(pre.length()).trim());
					}
				}
			};
			FilePath fp = FilePath.getTempDirectory();
			call(null, l, fp, command);
			return fp.child(l.lines().get(0)).readAll();
		} catch (RuntimeException ex) {
			full.forEach(System.out::println);
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	public FilePath downloadAudio(String url, FilePath tmpFolder, FilePath targetFolder, String cookiesLoc,
			Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		tmpFolder = tmpFolder.child(String.valueOf(System.currentTimeMillis())).createDirectory();
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		// command.add("--verbose");
		command.add("-f");
		command.add("bestaudio[ext=m4a]");
		// command.add("--embed-thumbnail");
		// command.add("--ignore-errors");
		// command.add("--add-metadata");
		extraConfig(command, extraConfig);
		command.add("-o");
		command.add(getEscapeChar() + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s"
				+ getEscapeChar());
		command.add(url);
		List<String> dl = new ArrayList<>();
		try {
			dl(executable.getParentPath(), command, dl);
		} catch (ExitValueException ex) {
			//
		}
		if (dl.isEmpty()) {
			throw new NullPointerException();
		}
		if (dl.size() == 1) {
			FilePath from = tmpFolder.child(dl.get(0));
			FilePath to = targetFolder.child(from.getFileNameString()).newFileIndex();
			from.renameTo(to);
			return to;
		}
		throw new UnsupportedOperationException(url);
	}

	protected void extraConfig(List<String> command, Map<String, Object> extraConfig) {
		Optional.ofNullable(extraConfig).ifPresent(_extraConfig -> extraConfig.entrySet().forEach(entry -> {
			command.add(entry.getKey());
			if (entry.getValue() != null) {
				boolean isNumber = entry.getValue() instanceof Number;
				boolean isDouble = false;
				try {
					Double.parseDouble("" + entry.getValue());
					isDouble = true;
				} catch (Exception ex) {
					//
				}
				boolean isLong = false;
				try {
					Long.parseLong("" + entry.getValue());
					isLong = true;
				} catch (Exception ex) {
					//
				}
				if (isNumber || isDouble || isLong)
					command.add(String.valueOf(entry.getValue()));
				else
					command.add(getEscapeChar() + String.valueOf(entry.getValue()) + getEscapeChar());
			}
		}));

	}

	protected void userAgent(List<String> command) {
		if (StringUtils.isNotBlank(userAgent)) {
			command.add("--user-agent");
			command.add(getEscapeChar() + userAgent + getEscapeChar());
			command.add("--add-header");
			command.add(getEscapeChar() + "User-Agent:" + userAgent + getEscapeChar());
		}
	}

	public List<FilePath> download(String url, FilePath tmpFolder, FilePath targetFolder, String cookiesLoc,
			Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		tmpFolder = tmpFolder.child(String.valueOf(System.currentTimeMillis())).createDirectory();
		List<String> dl = new ArrayList<>();
		try {
			if (!(url.contains("youtube") || url.contains("youtu.be"))) {
				throw new ExitValueException(0);
			}
			downloadYT(url, tmpFolder, cookiesLoc, dl, extraConfig);
		} catch (ExitValueException ex) {
			try {
				downloadOther(url, tmpFolder, cookiesLoc, dl, false, extraConfig);
			} catch (Exception ex2) {
				downloadOther(url, tmpFolder, cookiesLoc, dl, true, extraConfig);
			}
		}
		if (dl.isEmpty()) {
			throw new NullPointerException();
		}
		FilePath _tmpFolder = tmpFolder;
		return dl.stream().map(a -> {
			FilePath fp = new FilePath(a);
			if (fp.exists())
				return fp;
			fp = _tmpFolder.child(a);
			if (fp.exists())
				return fp;
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public void downloadOther(String url, FilePath tmpFolder, String cookiesLoc, List<String> dl, boolean onlyId,
			Map<String, Object> extraConfig) {
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		// command.add("--verbose");
		// command.add("--embed-thumbnail");
		// command.add("--ignore-errors");
		// command.add("--add-metadata");
		extraConfig(command, extraConfig);
		command.add("-o");
		// https://github.com/ytdl-org/youtube-dl/blob/master/README.md#output-template
		command.add(getEscapeChar() + tmpFolder.getAbsolutePath() + "/"
				+ (onlyId ? "f%(id)s.%(ext)s" : "%(title)s.f%(format_id)s.%(ext)s") + getEscapeChar());
		command.add(url);
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		dl(tmpFolder, command, dl);
	}

	protected void common(List<String> command) {
		command.add("--no-check-certificate");
		command.add("--restrict-filenames");
		command.add("--encoding");
		command.add(UTF_8);
	}

	public void downloadYT(String url, FilePath tmpFolder, String cookiesLoc, List<String> dl,
			Map<String, Object> extraConfig) {
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		extraConfig(command, extraConfig);
		// --compat-options format-sort
		command.add("-f");
		// command.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
		command.add("bestvideo,bestaudio");
		command.add("-o");
		command.add(getEscapeChar() + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s"
				+ getEscapeChar());
		command.add(url);
		dl(tmpFolder, command, dl);
	}

	protected void cookies(List<String> command, String cookiesLoc) {
		FilePath cookies;
		if (StringUtils.isNotBlank(cookiesLoc)) {
			cookies = new FilePath(cookiesLoc);
		} else {
			cookies = executable.getParentPath().child("cookies.txt");
		}
		if (cookies.exists()) {
			command.add("--cookies");
			command.add(Tool.command(cookies));
		}
	}

	protected void dl(FilePath tmpFolder, List<String> command, List<String> dl) {
		// loggeri.info("\n" + dl.stream().collect(Collectors.joining(" ")));
		Set<String> u = new HashSet<>();
		Lines lines = new Lines() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				{
					String prefix = "[download] Destination: ";
					if (t != null && t.startsWith(prefix)) {
						u.add(t.substring(prefix.length()));
					}
				}
				{
					String prefix = "[download] ";
					String suffix = " has already been downloaded";
					if (t != null && t.startsWith(prefix) && t.endsWith(suffix)) {
						u.add(t.substring(prefix.length(), t.length() - suffix.length()));
					}
				}
				{
					String prefix = "[atomicparsley] Adding thumbnail to \"";
					if (t != null && t.startsWith(prefix)) {
						u.add(t.substring(prefix.length(), t.length() - 1));
					}
				}
				{
					String prefix = "[ffmpeg] Adding metadata to '";
					if (t != null && t.startsWith(prefix)) {
						u.add(t.substring(prefix.length(), t.length() - 1));
					}
				}

				super.accept(t);
				loggeri.info("> " + t);
			}
		};
		Value<Process> processHolder = null;
		boolean log = true;
		Consumer<String> listener = null;
		boolean throwExitValue = true;
		List<FilePath> paths = Arrays.asList(executable.getParentPath());
		System.out.println(tmpFolder.getAbsolutePath() + ">\n" + command.stream().collect(Collectors.joining(" ")));
		try {
			call(processHolder, lines, tmpFolder, command, log, listener, throwExitValue, paths);
		} catch (RuntimeException ex) {
			if (lines.lines() != null && !lines.lines().isEmpty()) {
				String l = lines.lines().get(lines.lines().size() - 1);
				if (l.startsWith(UNSUPPORTED)) {
					throw new UnsupportedOperationException(l.substring(UNSUPPORTED.length()));
				}
			}
			throw ex;
		}
		if (dl != null) {
			dl.addAll(u);
		}
	}

	public List<YTDLFormat> formats(String url, String cookiesLoc, Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		extraConfig(command, extraConfig);
		command.add("--list-formats");
		// --compat-options list-formats
		command.add(url);
		Value<Process> processHolder = null;
		boolean log = true;
		Consumer<String> listener = null;
		boolean throwExitValue = true;
		List<FilePath> paths = Arrays.asList(executable.getParentPath());
		FilePath tmpFolder = getExecutable().getParentPath();
		List<YTDLFormat> rv = new ArrayList<>();
		StringValue head = new StringValue();
		Lines lines = new Lines() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				if (t.startsWith("ID")) {
					head.set(t);
				} else if (!(t.startsWith("[") || t.startsWith("format code") || t.startsWith("ID")
						|| t.startsWith("--"))) {
					YTDLFormat f = new YTDLFormat();
					f.extension = t.substring(head.get().indexOf("EXT"), head.get().indexOf("RESOLUTION")).trim();
					if (!"mhtml".equals(f.extension)) {
						f.formatCode = t.substring(0, head.get().indexOf("EXT")).trim();
						f.resolution = t.substring(head.get().indexOf("RESOLUTION"), head.get().indexOf("FPS")).trim();
						f.note = t;
						String tmp = t
								.substring(t.indexOf("|") + 1, head.get().indexOf("FILESIZE") + "FILESIZE".length())
								.replace("~", "").trim();
						int p = tmp.indexOf("KiB");
						if (p != -1) {
							f.size = (long) (Double.parseDouble(tmp.substring(0, p)) * 1024.0);
						} else {
							p = tmp.indexOf("MiB");
							if (p != -1) {
								f.size = (long) (Double.parseDouble(tmp.substring(0, p)) * 1024.0 * 1024.0);
							} else {
								p = tmp.indexOf("GiB");
								if (p != -1) {
									f.size = (long) (Double.parseDouble(tmp.substring(0, p)) * 1024.0 * 1024.0
											* 1024.0);
								}
							}
						}
						if (f.resolution.contains("x")) {
							String[] pa = f.resolution.split("x");
							f.width = Integer.parseInt(pa[0]);
							f.height = Integer.parseInt(pa[1]);
						}
						rv.add(f);
					}
				}
				super.accept(t);
			}
		};
		call(processHolder, lines, tmpFolder, command, log, listener, throwExitValue, paths);
		return rv;
	}

	public FilePath downloadFormat(String url, YTDLFormat format, FilePath tmpFolder, FilePath targetFolder,
			String cookiesLoc, Map<String, Object> extraConfig) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		tmpFolder = tmpFolder.child(String.valueOf(System.currentTimeMillis())).createDirectory();
		List<String> dl = new ArrayList<>();
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		userAgent(command);
		common(command);
		command.add("--format");
		command.add(format.formatCode);
		extraConfig(command, extraConfig);
		command.add(url);
		dl(tmpFolder, command, dl);
		FilePath s = tmpFolder.listFiles().get(0);
		if (tmpFolder.equals(targetFolder)) {
			return s;
		}
		return s.moveTo(targetFolder);
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getEscapeChar() {
		if (escapeChar == null) {
			escapeChar = Utils.osgroup == OSGroup.Windows ? "\"" : "'";
		}
		return this.escapeChar;
	}

	public void setEscapeChar(String escapeChar) {
		this.escapeChar = escapeChar;
	}

	public boolean isAutoUpdate() {
		return this.autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@SuppressWarnings("serial")
	public static class YTDLFormat implements Serializable {
		private Integer height;
		private Integer width;
		private String formatCode;
		private String extension;
		private String resolution;
		private String note;
		private Long size;

		public String getFormatCode() {
			return this.formatCode;
		}

		public void setFormatCode(String formatCode) {
			this.formatCode = formatCode;
		}

		public String getExtension() {
			return this.extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public String getResolution() {
			return this.resolution;
		}

		public void setResolution(String resolution) {
			this.resolution = resolution;
		}

		public String getNote() {
			return this.note;
		}

		public void setNote(String note) {
			this.note = note;
		}

		public Long getSize() {
			return this.size;
		}

		public void setSize(Long size) {
			this.size = size;
		}

		public Integer getHeight() {
			return this.height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

		public Integer getWidth() {
			return this.width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.formatCode == null) ? 0 : this.formatCode.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			YTDLFormat other = (YTDLFormat) obj;
			if (this.formatCode == null) {
				if (other.formatCode != null)
					return false;
			} else if (!this.formatCode.equals(other.formatCode))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "YTDLFormat [height=" + this.height + ", width=" + this.width + ", formatCode=" + this.formatCode
					+ ", extension=" + this.extension + ", resolution=" + this.resolution + ", note=" + this.note
					+ ", size=" + this.size + "]";
		}
	}
}
