package org.jhaws.common.io.media.ffmpeg;

import static org.jhaws.common.lang.CollectionUtils8.join;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.io.media.MediaCte;
import org.jhaws.common.io.media.ffmpeg.xml.FfprobeType;
import org.jhaws.common.io.media.ffmpeg.xml.StreamType;
import org.jhaws.common.io.media.images.ImageTools;
import org.jhaws.common.lang.BooleanValue;
import org.jhaws.common.lang.DateTime8;
import org.jhaws.common.lang.functions.EFunction;
import org.jhaws.common.pool.Pooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegTool implements MediaCte {
	private static final String VIDEO = "video";

	private static final String AUDIO = "audio";

	protected static class FfmpegDebug implements Consumer<String> {
		@Override
		public void accept(String t) {
			logger.debug("{}", t);
		}
	}

	protected static final Logger logger = LoggerFactory.getLogger("ffmpeg");

	public static final JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(
			org.jhaws.common.io.media.ffmpeg.xml.ObjectFactory.class.getPackage().getName());

	protected FilePath ffmpeg;

	protected FilePath ffprobe;

	protected List<String> hwAccel = null;

	public FfmpegTool() {
		super();
	}

	public FfmpegTool(FilePath root) {
		if (root.child("ffmpeg.exe").exists()) {
			ffmpeg = root.child("ffmpeg.exe");
			ffprobe = root.child("ffprobe.exe");
		} else if (root.child("bin").child("ffmpeg.exe").exists()) {
			ffmpeg = root.child("bin").child("ffmpeg.exe");
			ffprobe = root.child("bin").child("ffprobe.exe");
		} else {

		}
	}

	/** dxva2, qsv, nvenc */
	public synchronized List<String> getHwAccel() {
		if (hwAccel == null) {
			List<String> command = Arrays.asList(command(getFfmpeg()), "-hwaccels", "-hide_banner", "-y");
			Lines lines = silentcall(FilePath.getTempDirectory(), command);
			hwAccel = lines.lines();
			hwAccel.remove("Hardware acceleration methods:");
			{
				// ffmpeg -i input -c:v h264_nvenc -profile high444p
				// -pixel_format
				// yuv444p -preset default output.mp4
				FilePath input = FilePath.createDefaultTempFile("mp4");
				input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("ffmpeg/test.mp4"));
				FilePath output = FilePath.createDefaultTempFile("mp4");
				command = new ArrayList<>();
				command.add(command(getFfmpeg()));
				command.add("-hide_banner");
				command.add("-y");
				command.add("-i");
				command.add(command(input));
				command.add("-c:v");
				command.add("h264_nvenc");
				command.add("-profile");
				command.add("high444p");
				command.add("-pixel_format");
				command.add("yuv444p");
				command.add("-preset");
				command.add("default");
				command.add(command(output));
				logger.info("{}", join(command));
				try {
					lines = silentcall(FilePath.getTempDirectory(), command);
					if (!hwAccel.contains("nvenc")) {
						hwAccel.add("nvenc");
					}
					// if (!hwAccel.contains("nvdec"))
					// hwAccel.add("nvdec");
				} catch (Exception ex) {
					// if (lines.lines().stream().anyMatch(s ->
					// s.contains("Cannot load nvcuda.dll"))) {
					// hwAccel.remove("nvenc");
					// hwAccel.remove("nvdec");
					// }
				}
			}
			if (hwAccel.contains("cuvid")) {
				// ffmpeg -i input -c:v h264_nvenc -profile high444p
				// -pixel_format
				// yuv444p -preset default output.mp4
				FilePath input = FilePath.createDefaultTempFile("mp4");
				input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("ffmpeg/test.mp4"));
				FilePath output = FilePath.createDefaultTempFile("mp4");
				command = new ArrayList<>();
				command.add(command(getFfmpeg()));
				command.add("-hide_banner");
				command.add("-y");
				command.add("-i");
				command.add(command(input));
				command.add("-c:v");
				command.add("h264_cuvid");
				command.add("-profile");
				command.add("high444p");
				command.add("-pixel_format");
				command.add("yuv444p");
				command.add("-preset");
				command.add("default");
				command.add(command(output));
				logger.info("{}", join(command));
				try {
					lines = silentcall(FilePath.getTempDirectory(), command);
					if (!hwAccel.contains("cuvid")) {
						hwAccel.add("cuvid");
					}
				} catch (Exception ex) {
					hwAccel.remove("cuvid");
				}
			}
		}
		return Collections.unmodifiableList(hwAccel);
	}

	public static FfprobeType unmarshall(String xml) {
		return jaxbMarshalling.unmarshall(FfprobeType.class, xml);
	}

	public FfprobeType info(FilePath input) {
		// https://trac.ffmpeg.org/wiki/FFprobeTips
		try {
			List<String> command = new ArrayList<>();
			command.add(command(getFfprobe()));
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
			command.add(command(input));
			Lines lines = silentcall(input.getParentPath(), command);
			String xml = lines.lines().stream().map(String::trim).collect(Collectors.joining());
			logger.info("{}", xml);
			return unmarshall(xml);
		} catch (RuntimeException ex) {
			logger.error("{}", ex);
			return null;
		}
	}

	public Duration duration(FfprobeType info) {
		return Duration.ofSeconds(info.getFormat().getDuration().longValue());
	}

	public static enum SplashPow {
		_1, _2, _3, _4;
	}

	/**
	 * @see https://www.peterbe.com/plog/fastest-way-to-take-screencaps-out-of-videos
	 */
	public boolean splash(FilePath video, FilePath splashFile, Duration duration, long frames, SplashPow pow) {
		// That will seek 10 seconds into the movie, select every 1000th frame,
		// scale it to 320x240 pixels and create 2x3 tiles
		// ffmpeg -ss 00:00:10 -i movie.avi -frames 1 -vf
		// "select=not(mod(n\,1000)),scale=320:240,tile=2x3" out.png
		// wh=1: 1x1=1: parts=1+2=3: 2/3
		// wh=2: 2x2=4: parts=4+2=6: 2/6=1/3, 3/6=1/2, 4/6=2/3, 5/6
		// wh=3: 3x3=9: parts=9+2=11: 2/11, 3/11 ... 9/11, 10/11
		// wh=4: 4x4=16: parts=16+2=18: 2/18, 3/18 ... 16/18, 17/18
		try {
			// List<String> accel = getHwAccel();
			if (duration == null) {
				FfprobeType info = info(video);
				duration = Duration.ofSeconds(info.getFormat().getDuration().longValue());
				frames = frames(video(info));
			}
			long seconds = duration.getSeconds();
			boolean one = seconds < 6 || frames < 180 || pow == null || pow == SplashPow._1;
			int wh = one || pow == null ? 1 : Integer.parseInt(pow.name().substring(1));
			List<FilePath> seperates = new ArrayList<>();
			for (int i = 0; i < wh * wh; i++) {
				List<String> command = new ArrayList<>();
				command.add(command(getFfmpeg()));
				command.add("-hide_banner");
				command.add("-y");
				// // if (accel.contains("nvdec")) {
				// // // "CUDA Video Decoding API" or "CUVID."
				// // command.add("-hwaccel");
				// // // command.add("cuvid");
				// // command.add("nvdec");
				// // // command.add("-threads");
				// // // command.add("1");
				// // } else
				// if (accel.contains("qsv")) {
				// // qsv (intel onboard vid HW accel)
				// command.add("-hwaccel");
				// command.add("qsv");
				// // command.add("-threads");
				// // command.add("1");
				// } else if (accel.contains("dxva2")) {
				// // Direct-X Video Acceleration API, developed by Microsoft
				// // (supports Windows and XBox360)
				// command.add("-hwaccel");
				// command.add("dxva2");
				// // command.add("-threads");
				// // command.add("1");
				// }
				command.add("-ss");
				command.add(DateTime8.printUpToSeconds(duration.dividedBy(wh * wh + 1).multipliedBy(i + 1)));
				command.add("-i");
				command.add(command(video));
				command.add("-vframes");
				command.add("1");
				command.add("-qscale:v");
				command.add("15");// good=1-35=bad, preferred range 2-5
				FilePath seperate = splashFile.appendExtension(String.valueOf(i))
						.appendExtension(splashFile.getExtension());
				seperates.add(seperate);
				command.add(command(seperate));
				@SuppressWarnings("unused")
				Lines silentcall = silentcall(video.getParentPath(), command);
			}
			if (seperates.size() == 1) {
				seperates.get(0).renameTo(splashFile);
			} else {
				BufferedImage bio = ImageTools.tile(
						seperates.stream().map((EFunction<FilePath, BufferedImage>) ImageTools::read)
								.map(bi -> ImageTools.getScaledInstance(bi, 1.0 / wh)).collect(Collectors.toList()),
						wh);
				ImageTools.write(bio, splashFile);
			}
			seperates.stream().forEach(FilePath::deleteIfExists);
			return splashFile.exists();
		} catch (Exception ex) {
			logger.error("{}", ex);
			return false;
		}
		// int parts = wh * wh + 2;
		// try {
		// List<String> accel = getHwAccel();
		// List<String> command = new ArrayList<>();
		// command.add(command(getFfmpeg()));
		// command.add("-hide_banner");
		// command.add("-y");
		// // // if (accel.contains("nvdec")) {
		// // // // "CUDA Video Decoding API" or "CUVID."
		// // // command.add("-hwaccel");
		// // // // command.add("cuvid");
		// // // command.add("nvdec");
		// // // // command.add("-threads");
		// // // // command.add("1");
		// // // } else
		// // if (accel.contains("qsv")) {
		// // // qsv (intel onboard vid HW accel)
		// // command.add("-hwaccel");
		// // command.add("qsv");
		// // // command.add("-threads");
		// // // command.add("1");
		// // } else if (accel.contains("dxva2")) {
		// // // Direct-X Video Acceleration API, developed by Microsoft
		// // // (supports Windows and XBox360)
		// // command.add("-hwaccel");
		// // command.add("dxva2");
		// // // command.add("-threads");
		// // // command.add("1");
		// // }
		// command.add("-ss");
		// if (one) {
		// command.add(printUpToSeconds(duration.dividedBy(2)));
		// } else {
		// command.add(printUpToSeconds(duration.dividedBy(parts).multipliedBy(2)));
		// }
		// command.add("-i");
		// command.add(command(video));
		// if (one) {
		// command.add("-vframes");
		// command.add("1");
		// command.add("-qscale:v");
		// command.add("15");// good=1-35=bad, preferred range 2-5
		// } else {
		// command.add("-frames");
		// command.add("1");
		// command.add("-vf");
		// command.add("\"select=not(mod(n\\," + (frames / parts) +
		// ")),scale=iw/" + wh + ":ih/" + wh + ",tile=" + wh + "x" + wh + "\"");
		// }
		// command.add(command(splashFile));
		// call(command, true);
		// return splashFile.exists();
		// } catch (Exception ex) {
		// logger.error("{}", ex);
		// return false;
		// }
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

	public FilePath cut(FilePath input, String suffix, String from, String length) {
		FilePath output = input.getParentPath().child(input.getShortFileName());
		if (StringUtils.isNotBlank(suffix)) {
			output = output.appendExtension(suffix);
		}
		if (StringUtils.countMatches(from, ":") == 2) {
			output = output.appendExtension(
					from.replaceFirst(":", "h").replaceFirst(":", "m") + "-" + length.replaceFirst(":", "m") + "s");
		} else {
			output = output.appendExtension(from.replaceFirst(":", "m") + "-" + length.replaceFirst(":", "m") + "s");
		}
		output = output.appendExtension(MP4);
		try {
			List<String> command = new ArrayList<>();
			command.add(command(getFfmpeg()));
			command.add("-hide_banner");
			command.add("-y");
			command.add("-i");
			command.add(command(input));
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
			command.add(command(output));
			call(input.getParentPath(), command);
			if (output.exists() && output.getFileSize() > 500) {
				logger.info("done {}", output);
			} else {
				logger.error("error {}", output);
				output.deleteIfExists();
				output = null;
			}
		} catch (Exception ex) {
			logger.error("error {}", input, ex);
			if (output != null) {
				output.deleteIfExists();
			}
			output = null;
		}
		return output;
	}

	@Pooled
	public RemuxCfg remux(RemuxDefaultsCfg defaults, Function<RemuxCfg, Consumer<String>> listener, FilePath input,
			FilePath output, Consumer<RemuxCfg> cfgEdit) {
		RemuxCfg cfg = config(defaults, input, output, cfgEdit);
		RuntimeException exception = null;
		List<String> command = cfg.defaults.isTwopass() ? command(1, cfg) : command(Integer.MAX_VALUE, cfg);
		Lines lines = new Lines();
		try {
			lines = call(input.getParentPath(), command, true, listener == null ? null : listener.apply(cfg));
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
			command = cfg.defaults.isTwopass() ? command(1, cfg) : command(Integer.MAX_VALUE, cfg);
			lines = call(input.getParentPath(), command, true, listener == null ? null : listener.apply(cfg));
		}
		if (lines.lines().stream().anyMatch(s -> s.contains("Conversion failed"))) {
			throw new UncheckedIOException(new IOException("Conversion failed"));
		}
		if (cfg.defaults.isTwopass()) {
			command = command(2, cfg);
			lines = call(input.getParentPath(), command, true, listener == null ? null : listener.apply(cfg));
		}
		return cfg;
	}

	private RemuxCfg config(RemuxDefaultsCfg defaults, FilePath input, FilePath output, Consumer<RemuxCfg> cfgEdit) {
		RemuxCfg cfg = new RemuxCfg();
		if (defaults != null)
			cfg.defaults = defaults;
		cfg.input = input;
		cfg.output = output;
		cfg.info = info(input);
		cfg.hq = input.getFileSize() > 100 * 1024 * 1024;
		StreamType videostreaminfo = video(cfg.info);
		StreamType audiostreaminfo = audio(cfg.info);
		cfg.vr = videostreaminfo.getBitRate() == null ? (int) (cfg.info.getFormat().getBitRate() / 1000)
				: videostreaminfo.getBitRate() / 1000;
		cfg.vt = videostreaminfo.getCodecName();
		cfg.ar = audiostreaminfo == null || audiostreaminfo.getBitRate() == null ? -1
				: audiostreaminfo.getBitRate() / 1000;
		cfg.at = audiostreaminfo == null ? null : audiostreaminfo.getCodecName();
		cfg.wh = new int[] { videostreaminfo.getWidth(), videostreaminfo.getHeight() };
		try {
			String[] fps = videostreaminfo.getRFrameRate().split("/");
			cfg.fps = (int) Math.round((float) Integer.parseInt(fps[0]) / Integer.parseInt(fps[1]));
		} catch (Exception ex) {
			//
		}
		cfg.vcopy = cfg.vt.contains(AVC) || cfg.vt.contains(H264);
		cfg.acopy = cfg.at != null && (cfg.at.contains(MP3) || cfg.at.contains(AAC)) && !cfg.at.contains(MONO);
		cfg.fixes.fixNotHighProfile = true;
		cfg.fixes.fixAudioRate = false;
		cfg.fixes.fixAudioStrict = false;
		cfg.fixes.fixDiv2 = false;
		if (cfgEdit != null) {
			cfgEdit.accept(cfg);
		}
		return cfg;
	}

	protected void handle(RemuxCfg cfg, Lines lines, BooleanValue needsFixing, BooleanValue resetException) {
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
		// if (lines.lines().stream().anyMatch(s -> s.contains("Cannot load
		// nvcuda.dll"))) {
		// needsFixing.set(true);
		// resetException.set(true);
		// disableHwAccelNvenc();
		// }
	}

	protected static class RemuxFixes {
		boolean fixNotHighProfile;
		boolean fixAudioRate;
		boolean fixAudioStrict;
		boolean fixDiv2;

		public boolean isFixNotHighProfile() {
			return this.fixNotHighProfile;
		}

		public void setFixNotHighProfile(boolean fixNotHighProfile) {
			this.fixNotHighProfile = fixNotHighProfile;
		}

		public boolean isFixAudioRate() {
			return this.fixAudioRate;
		}

		public void setFixAudioRate(boolean fixAudioRate) {
			this.fixAudioRate = fixAudioRate;
		}

		public boolean isFixAudioStrict() {
			return this.fixAudioStrict;
		}

		public void setFixAudioStrict(boolean fixAudioStrict) {
			this.fixAudioStrict = fixAudioStrict;
		}

		public boolean isFixDiv2() {
			return this.fixDiv2;
		}

		public void setFixDiv2(boolean fixDiv2) {
			this.fixDiv2 = fixDiv2;
		}
	}

	public static class RemuxDefaultsCfg {
		// HQ 18-23-28 LQ
		int cfrHQ = 20;
		int cfrLQ = 23;
		int vidRateHQ = 3000;
		int vidRateLQ = 1000;
		String presetHQ = "slow";
		String presetLQ = "medium";
		List<String> tune = Arrays.asList("film", "zerolatency");
		int qmin = 10;
		int qmax = 51;
		int qdiff = 4;
		/* LQ 1-4 HQ */
		float vidRateQHQ = 2.7f;
		float vidRateQLQ = 2.2f;
		float vidRateQ = 0.7f;
		boolean twopass = false;

		public int getCfrHQ() {
			return this.cfrHQ;
		}

		public void setCfrHQ(int cfrHQ) {
			this.cfrHQ = cfrHQ;
		}

		public int getCfrLQ() {
			return this.cfrLQ;
		}

		public void setCfrLQ(int cfrLQ) {
			this.cfrLQ = cfrLQ;
		}

		public int getVidRateHQ() {
			return this.vidRateHQ;
		}

		public void setVidRateHQ(int vidRateHQ) {
			this.vidRateHQ = vidRateHQ;
		}

		public int getVidRateLQ() {
			return this.vidRateLQ;
		}

		public void setVidRateLQ(int vidRateLQ) {
			this.vidRateLQ = vidRateLQ;
		}

		public String getPresetHQ() {
			return this.presetHQ;
		}

		public void setPresetHQ(String presetHQ) {
			this.presetHQ = presetHQ;
		}

		public String getPresetLQ() {
			return this.presetLQ;
		}

		public void setPresetLQ(String presetLQ) {
			this.presetLQ = presetLQ;
		}

		public List<String> getTune() {
			return this.tune;
		}

		public void setTune(List<String> tune) {
			this.tune = tune;
		}

		public int getQmin() {
			return this.qmin;
		}

		public void setQmin(int qmin) {
			this.qmin = qmin;
		}

		public int getQmax() {
			return this.qmax;
		}

		public void setQmax(int qmax) {
			this.qmax = qmax;
		}

		public int getQdiff() {
			return this.qdiff;
		}

		public void setQdiff(int qdiff) {
			this.qdiff = qdiff;
		}

		public float getVidRateQHQ() {
			return this.vidRateQHQ;
		}

		public void setVidRateQHQ(float vidRateQHQ) {
			this.vidRateQHQ = vidRateQHQ;
		}

		public boolean isTwopass() {
			return this.twopass;
		}

		public void setTwopass(boolean twopass) {
			this.twopass = twopass;
		}

		public float getVidRateQLQ() {
			return this.vidRateQLQ;
		}

		public void setVidRateQLQ(float vidRateQLQ) {
			this.vidRateQLQ = vidRateQLQ;
		}

		public float getVidRateQ() {
			return this.vidRateQ;
		}

		public void setVidRateQ(float vidRateQ) {
			this.vidRateQ = vidRateQ;
		}
	}

	public static class RemuxCfg {
		String at;
		int ar;
		String vt;
		int vr;
		int tvr;
		boolean hq;
		FfprobeType info;
		FilePath parentDir;
		FilePath input;
		boolean vcopy;
		boolean acopy;
		int[] wh;
		Integer fps;
		FilePath output;
		RemuxFixes fixes = new RemuxFixes();
		List<List<String>> commands = new ArrayList<>();
		RemuxDefaultsCfg defaults = new RemuxDefaultsCfg();

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("RemuxCfg [");
			if (this.at != null) {
				builder.append("at=").append(this.at).append(", ");
			}
			builder.append("ar=").append(this.ar).append(", ");
			if (this.vt != null) {
				builder.append("vt=").append(this.vt).append(", ");
			}
			builder.append("vr=").append(this.vr).append(", tvr=").append(this.tvr).append(", hq=").append(this.hq)
					.append(", ");
			builder.append("vcopy=").append(this.vcopy).append(", acopy=").append(this.acopy).append(", ");
			if (this.wh != null) {
				builder.append("wh=").append(Arrays.toString(this.wh)).append(", ");
			}
			if (this.fps != null) {
				builder.append("fps=").append(fps).append(", ");
			}
			builder.append("defaults=").append(this.defaults).append(", ");
			builder.append("]");
			return builder.toString();
		}

		public int getVr() {
			return this.vr;
		}

		public int getTvr() {
			return this.tvr;
		}

		public String getAt() {
			return this.at;
		}

		public void setAt(String at) {
			this.at = at;
		}

		public int getAr() {
			return this.ar;
		}

		public void setAr(int ar) {
			this.ar = ar;
		}

		public String getVt() {
			return this.vt;
		}

		public void setVt(String vt) {
			this.vt = vt;
		}

		public boolean isHq() {
			return this.hq;
		}

		public void setHq(boolean hq) {
			this.hq = hq;
		}

		public FfprobeType getInfo() {
			return this.info;
		}

		public void setInfo(FfprobeType info) {
			this.info = info;
		}

		public FilePath getParentDir() {
			return this.parentDir;
		}

		public void setParentDir(FilePath parentDir) {
			this.parentDir = parentDir;
		}

		public FilePath getInput() {
			return this.input;
		}

		public void setInput(FilePath input) {
			this.input = input;
		}

		public boolean isVcopy() {
			return this.vcopy;
		}

		public void setVcopy(boolean vcopy) {
			this.vcopy = vcopy;
		}

		public boolean isAcopy() {
			return this.acopy;
		}

		public void setAcopy(boolean acopy) {
			this.acopy = acopy;
		}

		public int[] getWh() {
			return this.wh;
		}

		public void setWh(int[] wh) {
			this.wh = wh;
		}

		public FilePath getOutput() {
			return this.output;
		}

		public void setOutput(FilePath output) {
			this.output = output;
		}

		public RemuxFixes getFixes() {
			return this.fixes;
		}

		public void setFixes(RemuxFixes fixes) {
			this.fixes = fixes;
		}

		public List<List<String>> getCommands() {
			return this.commands;
		}

		public void setCommands(List<List<String>> commands) {
			this.commands = commands;
		}

		public RemuxDefaultsCfg getDefaults() {
			return this.defaults;
		}

		public void setDefaults(RemuxDefaultsCfg defaults) {
			this.defaults = defaults;
		}

		public void setVr(int vr) {
			this.vr = vr;
		}

		public void setTvr(int tvr) {
			this.tvr = tvr;
		}

		public Integer getFps() {
			return this.fps;
		}

		public void setFps(Integer fps) {
			this.fps = fps;
		}
	}

	protected List<String> command(int pass, RemuxCfg cfg) {
		List<String> accel = getHwAccel();
		List<String> command = new ArrayList<>();
		cfg.commands.add(command);
		{
			command.add(command(getFfmpeg()));
			command.add("-hide_banner");
			command.add("-y");
		}
		{
			// command.add("-loglevel");
			// command.add("error"); //
			// quiet,panic,fatal,error,warning,info,verbose,debug,trace
		}
		// if (!cfg.vcopy) {
		// // if (accel.contains("nvdec")) {
		// // // "CUDA Video Decoding API" or "CUVID."
		// // command.add("-hwaccel");
		// // // command.add("cuvid");
		// // command.add("nvdec");
		// // // command.add("-threads");
		// // // command.add("1");
		// // } else
		// if (accel.contains("qsv")) {
		// // qsv (intel onboard vid HW accel)
		// command.add("-hwaccel");
		// command.add("qsv");
		// // command.add("-threads");
		// // command.add("1");
		// } else if (accel.contains("dxva2")) {
		// // Direct-X Video Acceleration API, developed by Microsoft
		// // (supports Windows and XBox360)
		// command.add("-hwaccel");
		// command.add("dxva2");
		// // command.add("-threads");
		// // command.add("1");
		// }
		// }
		{
			command.add("-i");
			command.add(command(cfg.input));
		}
		cfg.defaults.tune.forEach(tune -> {
			command.add("-tune");
			command.add(tune);
		});
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
				// -pix_fmt nv12
			} else if (accel.contains("cuvid")) {
				command.add("h264_cuvid");
			} else if (accel.contains("qsv")) {
				// h264_qsv (intel onboard vid HW accel)
				command.add("h264_qsv");
			} else if (accel.contains("dxva2")) {
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
			// command.add(String.valueOf(Runtime.getRuntime().availableProcessors()
			// / 2));
		}
		if (!cfg.vcopy && cfg.fixes.fixNotHighProfile) {
			command.add("-profile:v");
			command.add("high");
			command.add("-level");
			command.add("4.2");
		}
		if (!cfg.vcopy) {
			command.add("-preset");
			command.add(cfg.hq ? cfg.defaults.presetHQ : cfg.defaults.presetLQ);
			command.add("-crf");
			// HQ 18-23-28 LQ
			command.add("" + (cfg.hq ? cfg.defaults.cfrHQ : cfg.defaults.cfrLQ));
			command.add("-b:v");
			// ...What_bitrate_should_I_use...
			int newVidRate = -1;
			if (cfg.wh != null) {
				newVidRate = (int) ((cfg.hq ? cfg.defaults.vidRateQHQ : cfg.defaults.vidRateQLQ) * cfg.wh[0]
						+ cfg.wh[1] * (cfg.getFps() == null ? 24 : cfg.getFps()) * cfg.defaults.vidRateQ / 1000);
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
						newVidRate = cfg.defaults.vidRateHQ;
					} else {
						newVidRate = cfg.defaults.vidRateLQ;
					}
				}
			}
			command.add(newVidRate + "k");
			command.add("-qmin");
			command.add(String.valueOf(cfg.defaults.qmin));
			command.add("-qmax");
			command.add(String.valueOf(cfg.defaults.qmax));
			command.add("-qdiff");
			command.add(String.valueOf(cfg.defaults.qdiff));
		}
		if (pass != 1) {
			command.add("-movflags");
			command.add("+faststart");
		}
		if (pass == 1 || pass == 2) {
			command.add("-pass");
			command.add(String.valueOf(pass));
		}
		if (pass == 1) {
			command.add("-an");
		} else {
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
		}
		if (cfg.fixes.fixDiv2) {
			command.add("-pix_fmt");
			command.add("yuv420p");
			command.add("-vf");
			command.add("\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
		}
		if (pass == 1) {
			command.add("-f");
			command.add("mp4");
			command.add("NUL");
		} else {
			command.add(command(cfg.output));
		}
		return command;
	}

	public boolean merge(FilePath video, FilePath audio, FilePath output) {
		if (video == null || video.notExists() || audio == null || audio.notExists()) {
			return false;
		}
		if (output.exists()) {
			throw new UncheckedIOException(new FileExistsException(output.getAbsolutePath()));
		}
		Lines lines = null;
		try {
			List<String> command = new ArrayList<>();
			command.add(command(getFfmpeg()));
			command.add("-hide_banner");
			command.add("-y");
			command.add("-i");
			command.add(command(video));
			command.add("-i");
			command.add(command(audio));
			command.add("-c:v");
			command.add("copy");
			command.add("-c:a");
			command.add("copy");
			command.add("-strict");
			command.add("experimental");
			// command.add("-map");
			// command.add("0:v:0");
			// command.add("-map");
			// command.add("0:a:0");
			command.add("-shortest");
			command.add("-movflags");
			command.add("+faststart");
			command.add(command(output));
			lines = call(output.getParentPath(), command);
			return true;
		} catch (RuntimeException ex) {
			logger.error("", ex);
			if (lines != null) {
				lines.lines().forEach(l -> logger.error("{}", l));
			}
			output.deleteIfExists();
			throw ex;
		}
	}

	public void slideshow(Integer secondsPerFrame, Integer framesPerSecondIn, Integer framesPerSecondOut, String images,
			FilePath output, Consumer<String> listener) {
		// In this example each image will have a duration of 5 seconds (the
		// inverse of 1/5 frames per second). The video stream will have a frame
		// rate of 30 fps by duplicating the
		// frames accordingly:
		// ffmpeg -framerate 1/5 -i img%03d.png -c:v libx264 -r 30 -pix_fmt
		// yuv420p out.mp4
		// -start_number
		List<String> command = new ArrayList<>();
		command.add(command(getFfmpeg()));
		command.add("-hide_banner");
		command.add("-y");
		command.add("-framerate");
		if (secondsPerFrame != null) {
			command.add("1/" + secondsPerFrame);
		} else if (framesPerSecondIn != null) {
			command.add("" + framesPerSecondIn);
		}
		command.add("-i");
		command.add(images);
		command.add("-movflags");
		command.add("+faststart");
		command.add("-c:v");
		command.add("libx264");
		command.add("-tune");
		command.add("film");
		command.add("-tune");
		command.add("zerolatency");
		command.add("-profile:v");
		command.add("high");
		command.add("-level");
		command.add("4.2");
		command.add("-vf");
		if (framesPerSecondOut == null) {
			if (secondsPerFrame != null)
				framesPerSecondOut = 1;
			else if (framesPerSecondIn != null)
				framesPerSecondOut = framesPerSecondIn;
		}
		command.add("\"fps=" + framesPerSecondOut + ",format=yuv420p\",\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
		command.add("-crf");
		command.add("15");// HQ 18-23-28 LQ
		command.add(command(output));
		command.forEach(System.out::println);
		call(output.getParentPath(), command, true, listener);
	}

	protected Lines call(FilePath dir, List<String> command) {
		return call(dir, command, true, null);
	}

	protected Lines silentcall(FilePath dir, List<String> command) {
		return call(dir, command, false, null);
	}

	protected Lines call(FilePath dir, List<String> command, boolean log, Consumer<String> listener) {
		Lines lines = new Lines();
		Consumer<String> consumers = log ? lines.andThen(new FfmpegDebug()) : lines;
		if (listener != null) {
			consumers = consumers.andThen(listener);
		}
		if (log) {
			logger.info("start - {}", join(command));
		}
		long start = System.currentTimeMillis();
		Processes.callProcess(true, command, dir, consumers);
		if (log) {
			logger.info("end - {}s :: {}", (System.currentTimeMillis() - start) / 1000, join(command));
		}
		return lines;
	}

	public StreamType video(FfprobeType finfo) {
		return videos(finfo).stream().findAny().orElse(null);
	}

	public StreamType audio(FfprobeType finfo) {
		return audios(finfo).stream().findAny().orElse(null);
	}

	public List<StreamType> videos(FfprobeType finfo) {
		return finfo.getStreams().getStream().stream().filter(stream -> VIDEO.equalsIgnoreCase(stream.getCodecType()))
				.collect(Collectors.toList());
	}

	public List<StreamType> audios(FfprobeType finfo) {
		return finfo.getStreams().getStream().stream().filter(stream -> AUDIO.equalsIgnoreCase(stream.getCodecType()))
				.collect(Collectors.toList());
	}

	protected String command(FilePath f) {
		return "\"" + f.getAbsolutePath() + "\"";
	}

	public long frames(StreamType videostreaminfo) {
		return videostreaminfo == null || videostreaminfo.getNbFrames() == null ? 0
				: videostreaminfo.getNbFrames().longValue();
	}
}
