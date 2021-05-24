package org.jhaws.common.io.media.ytdl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
public class YTDL extends Tool {
	private static final String UTF_8 = "utf-8";

	public static final String EXE = "youtube-dl";

	public static final String URL = "https://yt-dl.org/downloads/latest/";

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
	protected void setPathImpl(String path) {
		if (StringUtils.isBlank(path)) {
			new NullPointerException().printStackTrace();
			return;
		}
		FilePath f = new FilePath(path);
		if (f.exists()) {
			if (f.isFile()) {
				if (!EXE.equalsIgnoreCase(f.getShortFileName())) {
					new IllegalArgumentException().printStackTrace();
					return;
				}
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
			List<String> command = new ArrayList<>();
			command.add(Tool.command(executable));
			command.add("-U");

			// ---------------->
			// https://stackoverflow.com/questions/2275443/how-to-timeout-a-thread
			ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			});
			Future<Lines> future = executor.submit(new Callable<Lines>() {
				@Override
				public Lines call() throws Exception {
					return Tool.call(null, new Lines(), executable.getParentPath(), command);
				}
			});
			try {
				logger.info("Started ...");
				logger.info("{}", future.get(60, TimeUnit.SECONDS));
				logger.info("Finished!");
			} catch (TimeoutException ex) {
				future.cancel(true);
				logger.error("{}", ex);
			} catch (InterruptedException ex) {
				logger.error("{}", ex);
			} catch (ExecutionException ex) {
				logger.error("{}", ex);
			}
			executor.shutdownNow();
			// <----------------
		} else {
			String tmp = EXE;
			if (Utils.osgroup == OSGroup.Windows) {
				tmp = tmp + ".exe";
			}
			try (InputStream in = new URL(URL + tmp).openConnection().getInputStream(); OutputStream out = executable.newBufferedOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException ex) {
				logger.error("{}", ex);
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

	public String fileName(String url, String cookiesLoc) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		List<String> command = new ArrayList<>();
		command.add(Tool.command(executable));
		cookies(command, cookiesLoc);
		command.add("--get-filename");
		command.add(url);
		StringValue fn = new StringValue();
		List<String> full = new ArrayList<>();
		try {
			call(null, new Lines() {
				@Override
				public void accept(String t) {
					fn.set(t);
					full.add(t);
				}
			}, null, command);
			return fn.get();
		} catch (RuntimeException ex) {
			full.forEach(System.out::println);
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	public FilePath downloadAudio(String url, FilePath tmpFolder, FilePath targetFolder, String cookiesLoc) {
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
		// command.add("--verbose");
		command.add("-f");
		command.add("bestaudio[ext=m4a]");
		// command.add("--embed-thumbnail");
		// command.add("--ignore-errors");
		// command.add("--add-metadata");
		command.add("--no-check-certificate");
		command.add("-o");
		command.add("\"" + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s" + "\"");
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
		throw new UnsupportedOperationException();
	}

	public List<FilePath> download(String url, FilePath tmpFolder, FilePath targetFolder, String cookiesLoc) {
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
			List<String> command = new ArrayList<>();
			command.add(command(executable));
			cookies(command, cookiesLoc);
			// command.add("--verbose");
			// command.add("--embed-thumbnail");
			// command.add("--ignore-errors");
			// command.add("--add-metadata");
			command.add("--no-check-certificate");
			command.add("--encoding");
			command.add(UTF_8);
			command.add("-f");
			// command.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
			command.add("bestvideo,bestaudio");
			command.add("-o");
			command.add("\"" + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s" + "\"");
			command.add(url);
			dl(tmpFolder, command, dl);
		} catch (ExitValueException ex) {
			try {
				List<String> command = new ArrayList<>();
				command.add(command(executable));
				cookies(command, cookiesLoc);
				// command.add("--verbose");
				// command.add("--embed-thumbnail");
				// command.add("--ignore-errors");
				// command.add("--add-metadata");
				command.add("--no-check-certificate");
				command.add("--encoding");
				command.add(UTF_8);
				command.add("-o");
				command.add("\"" + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s" + "\"");
				command.add(url);
				dl(tmpFolder, command, dl);
			} catch (Exception ex2) {
				List<String> command = new ArrayList<>();
				command.add(command(executable));
				cookies(command, cookiesLoc);
				command.add("--no-check-certificate");
				command.add("--encoding");
				command.add(UTF_8);
				command.add("-o");
				command.add("\"" + tmpFolder.getAbsolutePath() + "/" + "%(title)s.f%(format_id)s.%(ext)s" + "\"");
				command.add(url);
				dl(tmpFolder, command, dl);
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

	private void dl(FilePath tmpFolder, List<String> command, List<String> dl) {
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
				logger.info("> " + t);
			}
		};
		Value<Process> processHolder = null;
		boolean log = true;
		Consumer<String> listener = null;
		boolean throwExitValue = true;
		List<FilePath> paths = Arrays.asList(executable.getParentPath());
		call(processHolder, lines, tmpFolder, command, log, listener, throwExitValue, paths);
		dl.addAll(u);
	}

	public List<YTDLFormat> formats(String url, String cookiesLoc) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		cookies(command, cookiesLoc);
		command.add("--no-check-certificate");
		command.add("--encoding");
		command.add(UTF_8);
		command.add("--list-formats");
		command.add(url);
		Value<Process> processHolder = null;
		boolean log = true;
		Consumer<String> listener = null;
		boolean throwExitValue = true;
		List<FilePath> paths = Arrays.asList(executable.getParentPath());
		FilePath tmpFolder = getExecutable().getParentPath();
		List<YTDLFormat> rv = new ArrayList<>();
		Lines lines = new Lines() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				if (!(t.startsWith("[youtube]") || t.startsWith("[info]") || t.startsWith("format code"))) {
					YTDLFormat f = new YTDLFormat();
					f.formatCode = t.substring(0, 13).trim();
					f.extension = t.substring(13, 24).trim();
					f.resolution = t.substring(24, 35).trim();
					f.note = t.substring(35).trim();
					String tmp = t.substring(t.lastIndexOf(",") + 1).trim();
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
								f.size = (long) (Double.parseDouble(tmp.substring(0, p)) * 1024.0 * 1024.0 * 1024.0);
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
				super.accept(t);
			}
		};
		call(processHolder, lines, tmpFolder, command, log, listener, throwExitValue, paths);
		return rv;
	}

	public FilePath downloadFormat(String url, YTDLFormat format, FilePath tmpFolder, FilePath targetFolder, String cookiesLoc) {
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
		command.add("--no-check-certificate");
		command.add("--encoding");
		command.add(UTF_8);
		command.add("--format");
		command.add(format.formatCode);
		command.add(url);
		dl(tmpFolder, command, dl);
		return null;
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

		@Override
		public String toString() {
			return "YTDLFormat [height=" + this.height + ", width=" + this.width + ", formatCode=" + this.formatCode + ", extension=" + this.extension + ", resolution="
					+ this.resolution + ", note=" + this.note + ", size=" + this.size + "]";
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
	}

	public static void main(String[] args) {
		new YTDL().formats("https://www.youtube.com/watch?v=Jm932Sqwf5E", null).forEach(System.out::println);
	}
}
