package org.jhaws.common.io.media.ytdl;

import java.util.ArrayList;
import java.util.List;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxDefaultsCfg;
import org.jhaws.common.io.media.ffmpeg.xml.FfprobeType;

// https://github.com/ytdl-org/youtube-dl
public class YTDL {
	protected FilePath executable;

	protected FfmpegTool ffmpegTool;

	protected String command(FilePath f) {
		return "\"" + f.getAbsolutePath() + "\"";
	}

	public FilePath getExecutable() {
		return this.executable;
	}

	public void setExecutable(FilePath executable) {
		this.executable = executable;
	}

	public FilePath download(String url, FilePath tmpFolder, FilePath targetFolder) {
		if (executable == null || executable.notExists())
			throw new NullPointerException();
		if (tmpFolder == null)
			tmpFolder = FilePath.getTempDirectory();
		if (targetFolder == null)
			targetFolder = FilePath.getTempDirectory();
		List<String> command = new ArrayList<>();
		command.add(FfmpegTool.command(executable));
		command.add("--verbose");
		command.add("-f");
		// command.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
		command.add("bestvideo,bestaudio");
		command.add("-o");
		command.add("%(title)s.f%(format_id)s.%(ext)s");
		command.add(url);
		List<String> dl = new ArrayList<>();
		FfmpegTool.call(null, new Lines() {
			@Override
			public void accept(String t) {
				String prefix = "[download] Destination: ";
				if (t != null && t.startsWith(prefix)) {
					dl.add(t.substring(prefix.length()));
				}
				System.out.println("> " + t);
				super.accept(t);
			}
		}, tmpFolder, command);
		if (dl.isEmpty()) {
			throw new NullPointerException();
		}
		if (dl.size() == 1) {
			FilePath from = new FilePath(tmpFolder, dl.get(0));
			FilePath to = from.moveTo(targetFolder).newFileIndex();
			return to;
		} else if (dl.size() == 2) {
			FilePath f1 = new FilePath(tmpFolder, dl.get(0));
			FilePath f2 = new FilePath(tmpFolder, dl.get(1));
			return merge(targetFolder, f1, f2);
		} else {
			throw new NullPointerException();
		}
	}

	protected FilePath merge(FilePath targetFolder, FilePath f1, FilePath f2) {
		FfprobeType i1 = ffmpegTool.info(f1, new Lines());
		FfprobeType i2 = ffmpegTool.info(f2, new Lines());
		List<FilePath> as = new ArrayList<>();
		List<FilePath> vs = new ArrayList<>();
		if (ffmpegTool.video(i1) != null) {
			vs.add(f1);
		}
		if (ffmpegTool.audio(i1) != null) {
			as.add(f1);
		}
		if (ffmpegTool.video(i2) != null) {
			vs.add(f2);
		}
		if (ffmpegTool.audio(i2) != null) {
			as.add(f2);
		}
		if (as.isEmpty() || vs.isEmpty()) {
			throw new NullPointerException();
		}
		if (as.size() == 2 && vs.size() == 2) {
			throw new NullPointerException();
		}
		FilePath a;
		FilePath v;
		if (as.size() == 1) {
			a = as.get(0);
			vs.remove(a);
			v = vs.get(0);
		} else if (vs.size() == 1) {
			v = vs.get(0);
			as.remove(v);
			a = as.get(0);
		} else {
			throw new NullPointerException();
		}
		if (v.getExtension().equalsIgnoreCase("mp4")) {
			FilePath to = targetFolder.child(v.getName() + ".mp4").newFileIndex();
			ffmpegTool.merge(v, a, to, new Lines() {
				@Override
				public void accept(String l) {
					System.out.println("> " + l);
					super.accept(l);
				}
			});
			v.delete();
			a.delete();
			return to;
		} else {
			FilePath to1 = targetFolder.child(v.getName() + ".mkv").newFileIndex();
			ffmpegTool.merge(v, a, to1, new Lines() {
				@Override
				public void accept(String l) {
					System.out.println("> " + l);
					super.accept(l);
				}
			});
			FilePath to2 = to1.appendExtension("mp4").newFileIndex();
			ffmpegTool.remux(null, new RemuxDefaultsCfg(), x -> System.out::println, to1, to2, cfg -> {
			});
			to1.delete();
			v.delete();
			a.delete();
			return to2;
		}
	}

	public FfmpegTool getFfmpegTool() {
		return this.ffmpegTool;
	}

	public void setFfmpegTool(FfmpegTool ffmpegTool) {
		this.ffmpegTool = ffmpegTool;
	}
}
