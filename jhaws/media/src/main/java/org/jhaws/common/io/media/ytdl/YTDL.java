package org.jhaws.common.io.media.ytdl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.console.Processes.ExitValueException;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.Tool;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool;

// https://github.com/ytdl-org/youtube-dl
// https://ytdl-org.github.io/youtube-dl/supportedsites.html
// --write-info-json
public class YTDL extends Tool {
	public static final String EXE = "youtube-dl";

	public static final String URL = "https://yt-dl.org/downloads/latest/";

	protected FilePath executable;

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
			command.add(FfmpegTool.command(executable));
			command.add("-U");
			FfmpegTool.call(null, new Lines(), executable.getParentPath(), command);
		} else {
			String tmp = EXE;
			if (Utils.osgroup == OSGroup.Windows) {
				tmp = tmp + ".exe";
			}
			try (InputStream in = new URL(URL + tmp).openConnection().getInputStream();
					OutputStream out = executable.newBufferedOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = new ArrayList<>();
		command.add(FfmpegTool.command(executable));
		command.add("--version");
		Lines lines = new Lines();
		FfmpegTool.call(null, lines, executable.getParentPath(), command);
		return lines.lines().get(0);
	}

	public FilePath downloadAudio(String url, FilePath tmpFolder, FilePath targetFolder) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		tmpFolder = tmpFolder.child(String.valueOf(System.currentTimeMillis())).createDirectory();
		List<String> command = new ArrayList<>();
		command.add(FfmpegTool.command(executable));
		command.add("-f");
		command.add("bestaudio[ext=m4a]");
		command.add("--embed-thumbnail");
		command.add("--add-metadata");
		command.add(url);
		List<String> dl = new ArrayList<>();
		try {
			dl(tmpFolder, command, dl);
		} catch (ExitValueException ex) {
			//
		}
		if (dl.isEmpty()) {
			throw new NullPointerException();
		}
		if (dl.size() == 1) {
			FilePath from = tmpFolder.child(dl.get(0));
			FilePath to = targetFolder.child(dl.get(0)).newFileIndex();
			from.renameTo(to);
			return to;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public List<FilePath> download(String url, FilePath tmpFolder, FilePath targetFolder) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		tmpFolder = tmpFolder.child(String.valueOf(System.currentTimeMillis())).createDirectory();
		List<String> command = new ArrayList<>();
		command.add(command(executable));
		command.add("--verbose");
		command.add("--encoding");
		command.add("utf-8");
		command.add("-f");
		// command.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
		command.add("bestvideo,bestaudio");
		// command.add("--embed-thumbnail");
		// command.add("--add-metadata");
		command.add("-o");
		command.add("%(title)s.f%(format_id)s.%(ext)s");
		command.add(url);
		List<String> dl = new ArrayList<>();
		try {
			dl(tmpFolder, command, dl);
		} catch (ExitValueException ex) {
			command = new ArrayList<>();
			command.add(command(executable));
			command.add("--verbose");
			command.add("--encoding");
			command.add("utf-8");
			command.add(url);
			dl(tmpFolder, command, dl);
		}
		if (dl.isEmpty()) {
			throw new NullPointerException();
		}
		if (dl.size() == 1) {
			FilePath from = tmpFolder.child(dl.get(0));
			FilePath to = targetFolder.child(dl.get(0)).newFileIndex();
			from.renameTo(to);
			return Arrays.asList(to);
		} else if (dl.size() == 2) {
			FilePath f1 = tmpFolder.child(dl.get(0));
			FilePath f2 = tmpFolder.child(dl.get(1));
			return Arrays.asList(f1, f2);
		} else {
			throw new NullPointerException();
		}
	}

	private void dl(FilePath tmpFolder, List<String> command, List<String> dl) {
		call(null, new Lines() {
			@Override
			public void accept(String t) {
				String prefix = "[download] Destination: ";
				if (t != null && t.startsWith(prefix)) {
					dl.add(t.substring(prefix.length()));
				}
				super.accept(t);
				System.out.println("> " + t);
			}
		}, tmpFolder, command);
	}
}
