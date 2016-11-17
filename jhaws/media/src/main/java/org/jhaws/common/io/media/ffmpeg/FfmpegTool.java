package org.jhaws.common.io.media.ffmpeg;

import static org.jhaws.common.io.console.Processes.callProcess;
import static org.jhaws.common.lang.CollectionUtils8.join;
import static org.jhaws.common.lang.DateTime8.printUpToSeconds;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.io.FileExistsException;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.io.media.MediaCte;
import org.jhaws.common.io.media.ffmpeg.xml.FfprobeType;
import org.jhaws.common.io.media.ffmpeg.xml.StreamType;
import org.jhaws.common.lang.BooleanValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegTool implements MediaCte {
	protected static class FfmpegDebug implements Consumer<String> {
		@Override
		public void accept(String t) {
			logger_ffmpeg.debug("{}", t);
		}
	}

	protected static final Logger logger_ffmpeg = LoggerFactory.getLogger("ffmpeg");

	protected static final Logger logger_ffprobe = LoggerFactory.getLogger("ffprobe");

	public static final JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(org.jhaws.common.io.media.ffmpeg.xml.ObjectFactory.class.getPackage().getName());

	protected FilePath ffmpeg;

	protected FilePath ffprobe;

	protected List<String> hwAccel = null;

	/** dxva2, qsv, nvenc */
	public List<String> getHwAccel() {
		if (hwAccel == null) {
			Lines lines = new Lines();
			Consumer<String> c = lines.andThen(new FfmpegDebug());
			List<String> command = Arrays.asList("\"" + getFfmpeg().getAbsolutePath() + "\"", "-hwaccels", "y", "-hide_banner");
			logger_ffmpeg.info("{}", join(command));
			callProcess(true, command, null, c);
			hwAccel = lines.lines();
			hwAccel.remove("Hardware acceleration methods:");
			hwAccel.add("nvenc");
		}
		return Collections.unmodifiableList(hwAccel);
	}

	public static FfprobeType unmarshall(String xml) {
		return jaxbMarshalling.unmarshall(xml);
	}

	public FfprobeType info(Map<String, Object> meta, FilePath parentDir, FilePath input) {
		// https://trac.ffmpeg.org/wiki/FFprobeTips
		try {
			List<String> command = new ArrayList<>();
			command.add("\"" + getFfprobe().getAbsolutePath() + "\"");
			command.add("-v");
			command.add("quiet");
			command.add("-print_format");
			command.add("xml");
			command.add("-show_format");
			command.add("-show_streams");
			command.add("-show_chapters");
			// command.add("-show_packets");
			// command.add("-show_frames");
			// command.add("-unit");
			// command.add("-prefix");
			// command.add("-byte_binary_prefix");
			// command.add("-sexagesimal");
			command.add("-i");
			command.add("\"" + input.getAbsolutePath() + "\"");
			logger_ffprobe.info("{}", join(command));
			Lines lines = new Lines();
			callProcess(true, command, parentDir, lines);
			String xml = lines.lines().stream().collect(Collectors.joining());
			logger_ffprobe.info("{}", xml);
			return unmarshall(xml);
		} catch (RuntimeException ex) {
			logger_ffprobe.error("{}", ex);
			return null;
		}
	}

	public Duration duration(FilePath file) {
		FfprobeType info = info(new HashMap<>(), file.getParentPath(), file);
		return Duration.ofSeconds(info.getFormat().getDuration().longValue());
	}

	public boolean splash(FilePath vid, FilePath splashFile, Duration loc) {
		try {
			List<String> command = new ArrayList<>();
			command.add("\"" + getFfmpeg().getAbsolutePath() + "\"");
			command.add("-y");
			command.add("-hide_banner");
			command.add("-ss");
			command.add(printUpToSeconds(loc));
			command.add("-i");
			command.add("\"" + vid.getAbsolutePath() + "\"");
			command.add("-vframes");
			command.add("1");
			command.add("-qscale:v");
			command.add("15");// good=1-35=bad, preferred range 2-5
			command.add("\"" + splashFile.getAbsolutePath() + "\"");
			logger_ffmpeg.info("{}", join(command));
			callProcess(true, command, vid.getParentPath(), new Processes.Out());
			return splashFile.exists();
		} catch (Exception ex) {
			logger_ffmpeg.error("{}", ex);
			return false;
		}
	}

	public FilePath getFfmpeg() {
		return this.ffmpeg;
	}

	public void setFfmpeg(FilePath ffmpeg) {
		this.ffmpeg = ffmpeg;
	}

	public FilePath getFfprobe() {
		return this.ffprobe;
	}

	public void setFfprobe(FilePath ffprobe) {
		this.ffprobe = ffprobe;
	}

	public void disableHwAccelNvenc() {
		getHwAccel().remove("nvenc");
	}

	public FilePath cut(FilePath input, String suffix, String from, String length) {
		FilePath parentDir = input.getParentPath();
		FilePath output = input.changeExtension(suffix).appendExtension(MP4);
		try {
			List<String> command = new ArrayList<>();
			command.add("\"" + getFfmpeg().getAbsolutePath() + "\"");
			command.add("-y");
			command.add("-hide_banner");
			command.add("-i");
			command.add("\"" + input.getAbsolutePath() + "\"");
			command.add("-acodec");
			command.add("copy");
			command.add("-vcodec");
			command.add("copy");
			command.add("-async");
			command.add("1");
			command.add("-strict");
			command.add("-2");
			command.add("-movflags");
			command.add("+faststart");
			command.add("-ss");
			command.add(from);
			command.add("-t");
			command.add(length);
			command.add("\"" + output.getAbsolutePath() + "\"");
			logger_ffmpeg.info("{}", join(command));
			callProcess(true, command, parentDir, new FfmpegDebug());
			if (output.exists() && output.getFileSize() > 500) {
				logger_ffmpeg.info("done {}", output);
			} else {
				logger_ffmpeg.error("error {}", output);
				output.deleteIfExists();
				output = null;
			}
		} catch (Exception ex) {
			logger_ffmpeg.error("error {}", input, ex);
			if (output != null) {
				output.deleteIfExists();
			}
			output = null;
		}
		return output;
	}

	public void remux(Map<String, Object> meta, Supplier<Consumer<String>> listener, FilePath parentDir, FilePath input, FilePath output) {
		Cfg cfg = new Cfg();
		cfg.info = info(meta, parentDir, input);
		cfg.hq = input.getFileSize() > 100 * 1024 * 1024;
		StreamType videostreaminfo = cfg.info.getStreams().getStream().stream().filter(stream -> "video".equalsIgnoreCase(stream.getCodecType())).findAny().orElse(null);
		StreamType audiostreaminfo = cfg.info.getStreams().getStream().stream().filter(stream -> "audio".equalsIgnoreCase(stream.getCodecType())).findAny().orElse(null);
		cfg.vr = videostreaminfo.getBitRate() == null ? (int) (cfg.info.getFormat().getBitRate() / 1000) : videostreaminfo.getBitRate() / 1000;
		cfg.vt = videostreaminfo.getCodecName();
		cfg.ar = audiostreaminfo == null || audiostreaminfo.getBitRate() == null ? -1 : audiostreaminfo.getBitRate() / 1000;
		cfg.at = audiostreaminfo == null ? null : audiostreaminfo.getCodecName();
		cfg.wh = new int[] { videostreaminfo.getWidth(), videostreaminfo.getHeight() };
		cfg.vcopy = cfg.vt.contains(AVC) || cfg.vt.contains(H264);
		cfg.acopy = cfg.at != null && (cfg.at.contains(MP3) || cfg.at.contains(AAC)) && !cfg.at.contains(MONO);
		cfg.fixes.fixNotHighProfile = true;
		cfg.fixes.fixAudioRate = false;
		cfg.fixes.fixAudioStrict = false;
		cfg.fixes.fixDiv2 = false;
		RuntimeException exception = null;
		List<String> command = command(cfg);
		logger_ffmpeg.info("{}", join(command));
		Lines lines = new Lines();
		try {
			callProcess(true, command, parentDir, lines.andThen(listener.get()));
		} catch (RuntimeException ex) {
			exception = ex;
		}
		BooleanValue resetException = new BooleanValue();
		BooleanValue needsFixing = new BooleanValue();
		handle(cfg, lines, needsFixing, resetException);
		if (needsFixing.is()) {
			exception = null;
		}
		if (exception != null) {
			throw exception;
		}
		if (needsFixing.is()) {
			command = command(cfg);
			logger_ffmpeg.info("{}", join(command));
			lines = new Lines();
			callProcess(true, command, parentDir, lines.andThen(listener.get()));
		}
		if (lines.lines().stream().anyMatch(s -> s.contains("Conversion failed"))) {
			throw new UncheckedIOException(new IOException("Conversion failed"));
		}
	}

	protected void handle(Cfg cfg, Lines lines, BooleanValue needsFixing, BooleanValue resetException) {
		if (lines.lines().stream().anyMatch(s -> s.startsWith("x264 [error]: high profile doesn't support"))) {
			needsFixing.set(true);
			cfg.fixes.fixNotHighProfile = false;
		}
		// muxing mp3 at 11025hz is not supported
		if (lines.lines().stream().anyMatch(s -> s.contains("muxing mp3") && s.contains("hz is not supported"))) {
			needsFixing.set(true);
			cfg.acopy = false;
			cfg.fixes.fixAudioRate = true;
		}
		// muxing mp3 at 8000hz is not standard, to mux anyway set strict to -1
		if (lines.lines().stream().anyMatch(s -> s.contains("muxing mp3") && s.contains("hz is not standard"))) {
			needsFixing.set(true);
			cfg.acopy = false;
			cfg.fixes.fixAudioStrict = true;
		}
		// height/width not divisible by 2 (1197x812)
		if (lines.lines().stream().anyMatch(s -> s.contains("not divisible by 2"))) {
			needsFixing.set(true);
			cfg.fixes.fixDiv2 = true;
		}
		// Cannot load nvcuda.dll
		if (lines.lines().stream().anyMatch(s -> s.contains("Cannot load nvcuda.dll"))) {
			needsFixing.set(true);
			resetException.set(true);
			disableHwAccelNvenc();
		}
	}

	protected static class CfgFixes {
		boolean fixNotHighProfile;
		boolean fixAudioRate;
		boolean fixAudioStrict;
		boolean fixDiv2;
	}

	protected static class Cfg {
		String at;
		int ar;
		String vt;
		int vr;
		boolean hq;
		FfprobeType info;
		FilePath parentDir;
		FilePath input;
		boolean vcopy;
		boolean acopy;
		int[] wh;
		FilePath output;
		CfgFixes fixes = new CfgFixes();
	}

	protected List<String> command(Cfg cfg) {
		List<String> accel = getHwAccel();
		List<String> command = new ArrayList<>();
		{
			command.add("\"" + getFfmpeg() + "\"");
			command.add("-y");
			command.add("-hide_banner");
		}
		// command.add("-loglevel");
		// command.add("error"); // quiet,panic,fatal,error,warning,info,verbose,debug,trace
		// if (!copyVideo && (true || hwAccel().contains("nvenc"))) {
		// // "CUDA Video Decoding API" or "CUVID."
		// command.add("-hwaccel");
		// command.add("cuvid");
		// }
		{
			command.add("-i");
			command.add("\"" + cfg.input.getAbsolutePath() + "\"");
		}
		{
			command.add("-tune");
			command.add("film");
		}
		if (cfg.vcopy) {
			command.add("-vcodec");
			command.add("copy");
		} else {
			command.add("-c:v");
			// ...HWAccelIntro...
			if (accel.contains("nvenc")) {
				// h264_nvenc (nvidia HW accel)
				command.add("h264_nvenc");
				// No NVENC capable devices found
				// -profile high444p -pixel_format yuv444p
			} else if (getHwAccel().contains("qsv")) {
				// h264_qsv (intel onboard vid HW accel)
				command.add("h264_qsv");
			} else if (getHwAccel().contains("dxva2")) {
				command.add("h264_dxva2");
			} else {
				command.add("libx264");
			}
		}
		{
			// deinterlace
			// command.add("-vf");
			// command.add("yadif");
		}
		{
			command.add("-threads");
			command.add("2");
			// command.add(String.valueOf(Runtime.getRuntime().availableProcessors() / 2));
		}
		{
			command.add("-tune");
			command.add("zerolatency");
		}
		if (!cfg.vcopy && cfg.fixes.fixNotHighProfile) {
			command.add("-profile:v");
			command.add("high");
			command.add("-level");
			command.add("4.2");
		}
		if (!cfg.vcopy) {
			command.add("-preset");
			command.add(cfg.hq ? "slow" : "medium");
			command.add("-crf");
			command.add(cfg.hq ? "20" : "23"); // HQ 18-23-28 LQ
			command.add("-b:v");
			// ...What_bitrate_should_I_use...
			int newVidRate = -1;
			if (cfg.wh != null) {
				newVidRate = (int) (2.5 /* LQ 1-4 HQ */ * cfg.wh[0] + cfg.wh[1] * 24 /* fps */ * .7 / 1000);
				if (cfg.vr != -1) {
					if (newVidRate > cfg.vr) {
						newVidRate = cfg.vr;
					} else {
						if (3000 < cfg.vr && newVidRate < 2500) {
							newVidRate *= 1.25;
						}
						if (cfg.vr < 1000 && 1500 < newVidRate) {
							newVidRate *= 0.75;
						}
					}
				}
			} else {
				if (cfg.vr != -1) {
					newVidRate = cfg.vr;
				} else {
					if (cfg.hq) {
						newVidRate = 3000;
					} else {
						newVidRate = 1000;
					}
				}
			}
			command.add(newVidRate + "k");
			command.add("-qmin");
			command.add("10");
			command.add("-qmax");
			command.add("51");
			command.add("-qdiff");
			command.add("4");
		}
		{
			command.add("-movflags");
			command.add("+faststart");
		}
		// if (pass == 1 || pass == 2) {
		// command.add("-pass");
		// command.add(String.valueOf(pass));
		// command.add("-b:v");
		// command.add(vidRate + "k");
		// if (pass == 1) {
		// command.add("-an");
		// } else if (audRate > 0) {
		// command.add("-b:a");
		// command.add(audRate + "k");
		// }
		//
		if (cfg.ar > 0) {
			if (cfg.acopy) {
				command.add("-acodec");
				command.add("copy");
			} else {
				// command.add("-strict");
				// command.add("experimental");
				command.add("-c:a");
				// command.add(AAC);
				command.add(MP3C);
				if (cfg.fixes.fixAudioRate) {
					// muxing mp3 at 11025hz is not supported
					command.add("-ar");
					command.add("44100");
				}
				if (cfg.fixes.fixAudioStrict) {
					command.add("-strict");
					command.add("-1");
				}
			}
		}
		if (cfg.fixes.fixDiv2) {
			command.add("-pix_fmt");
			command.add("yuv420p");
			command.add("-vf");
			command.add("\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
		}
		// }
		// if (pass == 1) {
		// command.add("-f");
		// command.add("mp4");
		// command.add("NUL");
		// } else {
		command.add("\"" + cfg.output.getAbsolutePath() + "\"");
		// }
		return command;
	}

	public boolean merge(FilePath video, FilePath audio, FilePath out) {
		if (video == null || video.notExists() || audio == null || audio.notExists()) {
			return false;
		}
		if (out.exists()) {
			throw new UncheckedIOException(new FileExistsException(out.getAbsolutePath()));
		}
		try {
			List<String> command = new ArrayList<>();
			command.add("\"" + getFfmpeg().getAbsolutePath() + "\"");
			command.add("-i");
			command.add("\"" + video.getAbsolutePath() + "\"");
			command.add("-i");
			command.add("\"" + audio.getAbsolutePath() + "\"");
			command.add("-c:v");
			command.add("copy");
			command.add("-c:a");
			command.add("copy");
			command.add("-strict");
			command.add("experimental");
			command.add("-map");
			command.add("0:v:0");
			command.add("-map");
			command.add("0:a:0");
			command.add("-shortest");
			command.add("-movflags");
			command.add("+faststart");
			command.add("\"" + out.getAbsolutePath() + "\"");
			logger_ffmpeg.info("{}", join(command));
			callProcess(true, command, null, new Lines());
			return true;
		} catch (RuntimeException ex) {
			out.delete();
			throw ex;
		}
	}
}
