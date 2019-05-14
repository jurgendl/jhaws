package org.jhaws.common.io.media.ffmpeg;

import static org.jhaws.common.lang.CollectionUtils8.join;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.console.Processes.LinesLog;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.io.media.MediaCte;
import org.jhaws.common.io.media.Tool;
import org.jhaws.common.io.media.ffmpeg.xml.FfprobeType;
import org.jhaws.common.io.media.ffmpeg.xml.StreamType;
import org.jhaws.common.io.media.images.ImageTools;
import org.jhaws.common.lang.BooleanValue;
import org.jhaws.common.lang.DateTime8;
import org.jhaws.common.lang.KeyValue;
import org.jhaws.common.pool.Pooled;

/**
 * @see https://ffmpeg.org/
 * @see https://ffmpeg.zeranoe.com/builds/
 * @see https://trac.ffmpeg.org/
 */
public class FfmpegTool extends Tool implements MediaCte {
	public static final String FIX_DIV2 = "scale=trunc(iw/2)*2:trunc(ih/2)*2";

	public static final String FORMAT_YUV420P = "format=yuv420p";

	public static final String VIDEO = "video";

	public static final String AUDIO = "audio";

	public static final JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(
			org.jhaws.common.io.media.ffmpeg.xml.ObjectFactory.class.getPackage().getName());

	protected FilePath ffmpeg;

	protected FilePath ffprobe;

	protected List<String> hwAccel;

	protected List<KeyValue<ProcessInfo, Process>> actions = new ArrayList<>();

	public static class ProcessInfo {
		public FilePath getInput() {
			return this.input;
		}

		public void setInput(FilePath input) {
			this.input = input;
		}

		public String getAction() {
			return this.action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public Object getContext() {
			return this.context;
		}

		public void setContext(Object context) {
			this.context = context;
		}

		public String getId() {
			return this.id;
		}

		public final String id = UUID.randomUUID().toString();

		public ProcessInfo(String action) {
			this.action = action;
		}

		public ProcessInfo(String action, FilePath input) {
			this.input = input;
			this.action = action;
		}

		public ProcessInfo(Object context, String action, FilePath input) {
			this.context = context;
			this.input = input;
			this.action = action;
		}

		public FilePath input;

		public String action;

		public Object context;
	}

	public FfmpegTool(String s) {
		super(s);
	}

	@Override
	protected void setPathImpl(String s) {
		if (StringUtils.isBlank(s)) {
			new NullPointerException().printStackTrace();
			return;
		}
		FilePath root = new FilePath(s);
		if (root.child("ffmpeg.exe").exists()) {
			ffmpeg = root.child("ffmpeg.exe");
			ffprobe = root.child("ffprobe.exe");
		} else if (root.child("bin").child("ffmpeg.exe").exists()) {
			ffmpeg = root.child("bin").child("ffmpeg.exe");
			ffprobe = root.child("bin").child("ffprobe.exe");
		} else {
			ffmpeg = root;
			ffmpeg = root.getParentPath().child("ffprobe.exe");
		}
		executable = ffmpeg;
	}

	public FfmpegTool() {
		super(System.getenv("FFMPEG"));
	}

	public FfmpegTool(boolean disableAuto) {
		super(disableAuto);
	}

	/** dxva2, qsv, nvenc */
	public synchronized List<String> getHwAccel() {
		if (hwAccel == null) {
			List<String> command = Arrays.asList(command(getFfmpeg()), "-hwaccels", "-hide_banner", "-y");
			Lines lines = new Lines();
			silentcall(null, lines, FilePath.getTempDirectory(), command);
			hwAccel = new ArrayList<>(lines.lines());
			hwAccel.remove("Hardware acceleration methods:");
			{
				// ffmpeg -i input -c:v h264_nvenc -profile high444p
				// -pixel_format
				// yuv444p -preset default output.mp4
				FilePath input = FilePath.createTempFile("mp4");
				input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("ffmpeg/test.mp4"));
				FilePath output = FilePath.createTempFile("mp4");
				command = new ArrayList<>();
				command.add(command(getFfmpeg()));
				command.add("-hide_banner");
				command.add("-y");
				command.add("-i");
				command.add(command(input));
				command.add("-c:v");
				command.add("h264_nvenc"); // hevc_nvenc=h265, h264_nvenc=h264
				// command.add("-profile");
				// command.add("high444p");
				command.add("-pixel_format");
				command.add("yuv444p");
				command.add("-preset");
				command.add("default");
				command.add(command(output));
				logger.info("{}", join(command));// FIXME JOIN FALSE
				try {
					silentcall(null, lines, FilePath.getTempDirectory(), command);
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
				FilePath input = FilePath.createTempFile("mp4");
				input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("ffmpeg/test.mp4"));
				FilePath output = FilePath.createTempFile("mp4");
				command = new ArrayList<>();
				command.add(command(getFfmpeg()));
				command.add("-hide_banner");
				command.add("-y");
				command.add("-i");
				command.add(command(input));
				command.add("-c:v");
				command.add("h264_cuvid");
				// command.add("-profile");
				// command.add("high444p");
				command.add("-pixel_format");
				command.add("yuv444p");
				command.add("-preset");
				command.add("default");
				command.add(command(output));
				logger.info("{}", join(command));// FIXME JOIN FALSE
				try {
					lines = new Lines();
					silentcall(null, lines, FilePath.getTempDirectory(), command);
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

	public FfprobeType info(FilePath input, Lines lines) {
		// https://trac.ffmpeg.org/wiki/FFprobeTips
		if (lines == null)
			lines = new Lines();
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
			System.out.println(command.stream().collect(Collectors.joining(" ")));
			silentcall(null, lines, input.getParentPath(), command);
			String xml = lines.lines().stream().map(String::trim).collect(Collectors.joining());
			logger.info("{}", xml);
			return unmarshall(xml);
		} catch (RuntimeException ex) {
			logger.error("{}", ex);
			lines.lines().forEach(l -> logger.error("{}", l));
			return null;
		}
	}

	public Duration duration(FfprobeType info) {
		return info == null ? null : Duration.ofSeconds(info.getFormat().getDuration().longValue());
	}

	public static enum SplashPow {
		_1, _2, _3, _4;
	}

	protected KeyValue<ProcessInfo, Process> act(Object context, FilePath input, String action) {
		KeyValue<ProcessInfo, Process> act = new KeyValue<ProcessInfo, Process>(new ProcessInfo(context, action, input),
				null);
		actions.add(act);
		return act;
	}

	/**
	 * @see https://www.peterbe.com/plog/fastest-way-to-take-screencaps-out-of-videos
	 */
	public boolean splash(FilePath video, FilePath splashFile, Duration duration, long frames, SplashPow pow,
			Lines lines) {
		Map<String, Object> context = new HashMap<>();
		context.put("start", System.currentTimeMillis());
		KeyValue<ProcessInfo, Process> act = act(context, video, "splash");
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
				FfprobeType info = info(video, new Lines());
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
				silentcall(act, lines, video.getParentPath(), command);
			}
			if (seperates.size() == 1) {
				seperates.get(0).renameTo(splashFile);
			} else {
				BufferedImage bio = ImageTools.tile(seperates.stream().map(ImageTools::read)
						.map(bi -> ImageTools.getScaledInstance(bi, 1.0 / wh)).collect(Collectors.toList()), wh);
				ImageTools.write(bio, splashFile);
			}
			seperates.stream().forEach(FilePath::delete);
			boolean success = splashFile.exists();
			context.put("end", System.currentTimeMillis());
			context.put("success", success);
			return success;
		} catch (Exception ex) {
			logger.error("{}", ex);
			context.put("end", System.currentTimeMillis());
			context.put("success", false);
			context.put("exception", "" + ex);
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
			call(null, new Lines(), input.getParentPath(), command);
			if (output.exists() && output.getFileSize() > 500) {
				logger.info("done {}", output);
			} else {
				logger.error("error {}", output);
				output.delete();
				output = null;
			}
		} catch (Exception ex) {
			logger.error("error {}", input, ex);
			if (output != null) {
				output.delete();
			}
			output = null;
		}
		return output;
	}

	@Pooled
	public RemuxCfg remux(Object context, RemuxDefaultsCfg defaults, Function<RemuxCfg, Consumer<String>> listener,
			FilePath input, FilePath output, Consumer<RemuxCfg> cfgEdit) {
		try {
			output.checkNotExists();
		} catch (FileAlreadyExistsException ex) {
			throw new UncheckedIOException(ex);
		}
		RemuxCfg cfg = config(defaults, input, output, cfgEdit);
		RuntimeException exception = null;
		List<String> command = cfg.defaults.twopass ? command(1, cfg) : command(Integer.MAX_VALUE, cfg);
		Lines lines = new Lines();
		try {
			call(act(context, input, "remux"), lines, input != null ? input.getParentPath() : output.getParentPath(),
					command, true, listener == null ? null : listener.apply(cfg));
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
			command = cfg.defaults.twopass ? command(1, cfg) : command(Integer.MAX_VALUE, cfg);
			lines = new Lines();
			call(act(context, input, "remux-fix"), lines, input.getParentPath(), command, true,
					listener == null ? null : listener.apply(cfg));
		}
		if (lines.lines().stream().anyMatch(s -> s.contains("Conversion failed"))) {
			throw new UncheckedIOException(new IOException("Conversion failed"));
		}
		if (cfg.defaults.twopass) {
			command = command(2, cfg);
			lines = new Lines();
			call(act(context, input, "remux-two-pass"), lines, input.getParentPath(), command, true,
					listener == null ? null : listener.apply(cfg));
			input.getParentPath().child("ffmpeg2pass-0.log").delete();
			input.getParentPath().child("ffmpeg2pass-0.log.mbtree").delete();
			output.getParentPath().child("ffmpeg2pass-0.log").delete();
			output.getParentPath().child("ffmpeg2pass-0.log.mbtree").delete();
		}
		return cfg;
	}

	protected RemuxCfg config(RemuxDefaultsCfg defaults, FilePath input, FilePath output, Consumer<RemuxCfg> cfgEdit) {
		if (defaults == null) {
			defaults = new RemuxDefaultsCfg();
		}
		RemuxCfg cfg = new RemuxCfg();
		if (defaults != null) {
			cfg.defaults = defaults;
		}
		cfg.output = output;
		if (input != null) {
			cfg.input = new RemuxInput();
			cfg.input.input = input;
			cfg.input.info = info(input, new Lines());
			cfg.input.hq = input.getFileSize() >= defaults.hqTreshold;
			StreamType videostreaminfo = video(cfg.input.info);
			StreamType audiostreaminfo = audio(cfg.input.info);
			cfg.input.vr = videostreaminfo.getBitRate() == null ? (int) (cfg.input.info.getFormat().getBitRate() / 1000)
					: videostreaminfo.getBitRate() / 1000;
			cfg.input.vt = videostreaminfo.getCodecName();
			cfg.input.ar = audiostreaminfo == null || audiostreaminfo.getBitRate() == null ? -1
					: audiostreaminfo.getBitRate() / 1000;
			cfg.input.at = audiostreaminfo == null ? null : audiostreaminfo.getCodecName();
			cfg.input.wh = new int[] { videostreaminfo.getWidth(), videostreaminfo.getHeight() };
			try {
				String[] fps = videostreaminfo.getRFrameRate().split("/");
				cfg.input.fps = (int) Math.round((float) Integer.parseInt(fps[0]) / Integer.parseInt(fps[1]));
			} catch (Exception ex) {
				//
			}
			cfg.input.vcopy = cfg.input.vt.contains(AVC) || cfg.input.vt.contains(H264);
			cfg.input.acopy = cfg.input.at != null && (cfg.input.at.contains(MP3) || cfg.input.at.contains(AAC))
					&& !cfg.input.at.contains(MONO);
		}
		{
			cfg.fixes.fixNotHighProfile = true;
			cfg.fixes.fixAudioRate = false;
			cfg.fixes.fixAudioStrict = false;
			cfg.fixes.fixDiv2 = false;
		}
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
		if (lines.lines().stream()
				.anyMatch(s -> s.contains("No device available for encoder (device type qsv for codec h264_qsv)"))) {
			hwAccel.remove("qsv");
			needsFixing.set(true);
		}
		if (lines.lines().stream().anyMatch(s -> s.contains("InitializeEncoder failed: invalid param (8)"))) {
			needsFixing.set(true);
			cfg.fixes.fixNotHighProfile = false;
		}
		// Cannot load nvcuda.dll
		// if (lines.lines().stream().anyMatch(s -> s.contains("Cannot load
		// nvcuda.dll"))) {
		// needsFixing.set(true);
		// resetException.set(true);
		// disableHwAccelNvenc();
		// }
	}

	public static class RemuxFixes {
		public boolean fixNotHighProfile;

		public boolean fixAudioRate;

		public boolean fixAudioStrict;

		public boolean fixDiv2;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[fixNotHighProfile=");
			builder.append(this.fixNotHighProfile);
			builder.append(", fixAudioRate=");
			builder.append(this.fixAudioRate);
			builder.append(", fixAudioStrict=");
			builder.append(this.fixAudioStrict);
			builder.append(", fixDiv2=");
			builder.append(this.fixDiv2);
			builder.append("]");
			return builder.toString();
		}
	}

	public static class RemuxDefaultsCfg implements Serializable {
		private static final long serialVersionUID = -5591850342277728402L;

		final public List<String> tune = new ArrayList<>(Arrays.asList("film", "zerolatency"));

		// HQ 18-23-28 LQ
		public int cfrHQ = 18;

		// HQ 18-23-28 LQ
		public int cfrLQ = 23;

		public int vidRateHQ = 3000;

		public int vidRateLQ = 1000;

		public String presetHQ = "slow";

		public String presetLQ = "medium";

		public int qmin = 10;

		public int qmax = 51;

		public int qdiff = 4;

		/* LQ 1-4 HQ */
		public float vidRateQHQ = 2.7f;

		/* LQ 1-4 HQ */
		public float vidRateQLQ = 2.2f;

		public float vidRateQ = 0.7f;

		public boolean twopass = false;

		public int hqTreshold = 100 * 1024 * 1024;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[cfrHQ=");
			builder.append(this.cfrHQ);
			builder.append(", cfrLQ=");
			builder.append(this.cfrLQ);
			builder.append(", vidRateHQ=");
			builder.append(this.vidRateHQ);
			builder.append(", vidRateLQ=");
			builder.append(this.vidRateLQ);
			builder.append(", ");
			if (this.presetHQ != null) {
				builder.append("presetHQ=");
				builder.append(this.presetHQ);
				builder.append(", ");
			}
			if (this.presetLQ != null) {
				builder.append("presetLQ=");
				builder.append(this.presetLQ);
				builder.append(", ");
			}
			if (this.tune != null) {
				builder.append("tune=");
				builder.append(this.tune);
				builder.append(", ");
			}
			builder.append("qmin=");
			builder.append(this.qmin);
			builder.append(", qmax=");
			builder.append(this.qmax);
			builder.append(", qdiff=");
			builder.append(this.qdiff);
			builder.append(", vidRateQHQ=");
			builder.append(this.vidRateQHQ);
			builder.append(", vidRateQLQ=");
			builder.append(this.vidRateQLQ);
			builder.append(", vidRateQ=");
			builder.append(this.vidRateQ);
			builder.append(", twopass=");
			builder.append(this.twopass);
			builder.append("]");
			return builder.toString();
		}
	}

	public static class SlideshowCfg {
		public Integer secondsPerFrame;

		public Integer framesPerSecondIn;

		public Integer framesPerSecondOut;

		public String images;

		public FilePath input;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append("input=");
			builder.append(this.input);
			builder.append(", ");
			builder.append("images=");
			builder.append(this.images);
			if (this.secondsPerFrame != null) {
				builder.append(", ");
				builder.append("secondsPerFrame=");
				builder.append(this.secondsPerFrame);
			}
			if (this.framesPerSecondIn != null) {
				builder.append(", ");
				builder.append("framesPerSecondIn=");
				builder.append(this.framesPerSecondIn);
			}
			if (this.framesPerSecondOut != null) {
				builder.append(", ");
				builder.append("framesPerSecondOut=");
				builder.append(this.framesPerSecondOut);
			}
			builder.append("]");
			return builder.toString();
		}
	}

	public static class RemuxInput {
		public FilePath input;

		public FfprobeType info;

		public boolean hq;

		public String vt;

		public int vr;

		public String at;

		public int ar;

		public int[] wh;

		public boolean vcopy;

		public boolean acopy;

		Integer fps;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			if (this.at != null) {
				builder.append(", at=").append(this.at);
			}
			builder.append(", ar=").append(this.ar);
			if (this.vt != null) {
				builder.append(", vt=").append(this.vt);
			}
			builder.append(", vr=").append(this.vr);
			builder.append(", hq=").append(this.hq);
			builder.append(", vcopy=").append(this.vcopy);
			builder.append(", acopy=").append(this.acopy);
			if (this.wh != null) {
				builder.append(", wh=").append(Arrays.toString(this.wh));
			}
			if (this.fps != null) {
				builder.append(", fps=").append(fps);
			}
			builder.append("]");
			return builder.toString();
		}
	}

	public static class RemuxCfg {
		public RemuxInput input;

		public FilePath output;

		public final RemuxFixes fixes = new RemuxFixes();

		public final List<List<String>> commands = new ArrayList<>();

		public RemuxDefaultsCfg defaults = new RemuxDefaultsCfg();

		public SlideshowCfg slideshowCfg;

		public Boolean vcopy;

		public Boolean acopy;

		public Boolean hq;

		public int vidrate = -1;

		public Integer repeat;

		// force if no changes at all (vcopy=true && acopy=true)
		public boolean forceRemux = false;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append("vcopy=").append(this.vcopy);
			builder.append(", acopy=").append(this.acopy);
			builder.append(", vidrate=").append(this.vidrate);
			builder.append(", repeat=").append(this.repeat);
			builder.append(", hq=").append(this.hq);
			if (slideshowCfg != null)
				builder.append(", slideshowCfg=").append(this.slideshowCfg);
			if (input != null)
				builder.append(", info=").append(this.input);
			builder.append("]");
			return builder.toString();
		}
	}

	protected List<String> command(int pass, RemuxCfg cfg) {
		if (cfg.slideshowCfg != null && cfg.defaults.twopass)
			throw new IllegalArgumentException();
		if (cfg.repeat != null && cfg.repeat <= 1)
			cfg.repeat = null;
		if (cfg.acopy == null)
			cfg.acopy = cfg.input != null ? cfg.input.acopy : false;
		if (cfg.vcopy == null)
			cfg.vcopy = cfg.input != null ? cfg.input.vcopy : false;
		if (cfg.forceRemux && cfg.acopy && cfg.vcopy
				&& cfg.input.input.getExtension().equalsIgnoreCase(cfg.output.getExtension()))
			cfg.vcopy = false;
		if (cfg.hq == null)
			cfg.hq = cfg.input != null ? cfg.input.hq : true;
		if (cfg.repeat != null) {
			cfg.vcopy = false;
			cfg.acopy = false;
		}

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
		if (cfg.slideshowCfg != null) {
			command.add("-framerate");
			if (cfg.slideshowCfg.secondsPerFrame != null) {
				command.add("1/" + cfg.slideshowCfg.secondsPerFrame);
			} else if (cfg.slideshowCfg.framesPerSecondIn != null) {
				command.add("" + cfg.slideshowCfg.framesPerSecondIn);
			}
			command.add("-i");
			command.add("\"" + cfg.slideshowCfg.input.getAbsolutePath() + "/" + cfg.slideshowCfg.images + "\"");
		} else {
			command.add("-i");
			command.add(command(cfg.input.input));
		}
		if (cfg.repeat != null) {
			command.add("-filter_complex");
			command.add("loop=" + (cfg.repeat - 1) + ":" + cfg.input.fps + ":0");
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
				logger.debug("choosing HW accell h264_nvenc: " + accel);
				// h264_nvenc (nvidia HW accel)
				command.add("h264_nvenc");
				// No NVENC capable devices found
				// -profile high444p -pixel_format yuv444p
				// -pix_fmt nv12
			} else if (accel.contains("cuvid")) {
				logger.debug("choosing HW accell h264_cuvid: " + accel);
				command.add("h264_cuvid");
			} else if (accel.contains("qsv")) {
				// h264_qsv (intel onboard vid HW accel)
				logger.debug("choosing HW accell h264_qsv: " + accel);
				command.add("h264_qsv");
			} else if (accel.contains("dxva2")) {
				logger.debug("choosing HW accell h264_dxva2: " + accel);
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
			command.add(String.valueOf(Math.max(1, Runtime.getRuntime().availableProcessors() / 4)));
		}
		if (!cfg.vcopy && cfg.fixes.fixNotHighProfile) {
			logger.warn("fix not High Profile 4.2 not possible unless remuxing video");
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
			if (cfg.input != null) {
				command.add("-b:v");
				int newVidRate = vidrate(cfg);
				command.add(newVidRate + "k");
			}
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
		if (cfg.input != null) {
			if (pass == 1 || pass == 2) {
				command.add("-pass");
				command.add(String.valueOf(pass));
			}
		}
		if (cfg.input != null) {
			if (pass == 1) {
				command.add("-an");
			} else {
				if (cfg.input.ar > 0) {
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
							logger.warn("fix for 'muxing mp3 at ?hz is not supported'");
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
		}
		if (cfg.slideshowCfg != null) {
			if (cfg.slideshowCfg.framesPerSecondOut == null) {
				if (cfg.slideshowCfg.secondsPerFrame != null) {
					cfg.slideshowCfg.framesPerSecondOut = 1;
				} else if (cfg.slideshowCfg.framesPerSecondIn != null) {
					cfg.slideshowCfg.framesPerSecondOut = cfg.slideshowCfg.framesPerSecondIn;
				}
			}
			command.add("-vf");
			if (cfg.fixes.fixDiv2) {
				command.add(escape(and("fps=" + cfg.slideshowCfg.framesPerSecondOut, FORMAT_YUV420P)));
			} else {
				command.add(escape(and("fps=" + cfg.slideshowCfg.framesPerSecondOut, FORMAT_YUV420P, FIX_DIV2)));
			}
		} else if (cfg.fixes.fixDiv2) {
			command.add("-vf");
			command.add(escape(and(FORMAT_YUV420P, FIX_DIV2)));
		}
		if (cfg.input != null && pass == 1) {
			command.add("-f");
			command.add("mp4");
			command.add("NUL");
			// command.add("&&");
			// command.add("\\");
			// add second pass command
		} else {
			command.add(command(cfg.output));
		}
		return command;
	}

	protected int vidrate(RemuxCfg cfg) {
		if (cfg.vidrate > 0) {
			return cfg.vidrate;
		}
		// ...What_bitrate_should_I_use...
		if (cfg.input.wh != null) {
			cfg.vidrate = (int) ((cfg.hq ? cfg.defaults.vidRateQHQ : cfg.defaults.vidRateQLQ) * cfg.input.wh[0]
					+ cfg.input.wh[1] * (cfg.input.fps == null ? 24 : cfg.input.fps) * cfg.defaults.vidRateQ / 1000);
			if (cfg.input.vr != -1) {
				if (cfg.vidrate > cfg.input.vr) {
					cfg.vidrate = cfg.input.vr;
				} else {
					if (3000 < cfg.input.vr && cfg.vidrate < 2500) {
						cfg.vidrate *= 1.25;
					}
					if (cfg.input.vr < 1000 && 1500 < cfg.vidrate) {
						cfg.vidrate *= 0.75;
					}
				}
			}
		} else {
			if (cfg.input.vr != -1) {
				cfg.vidrate = cfg.input.vr;
			} else {
				if (cfg.hq) {
					cfg.vidrate = cfg.defaults.vidRateHQ;
				} else {
					cfg.vidrate = cfg.defaults.vidRateLQ;
				}
			}
		}
		return cfg.vidrate;
	}

	public boolean merge(FilePath video, FilePath audio, FilePath output, Lines lines) {
		if (video == null || video.notExists() || audio == null || audio.notExists()) {
			return false;
		}
		if (output.exists()) {
			throw new UncheckedIOException(new FileExistsException(output.getAbsolutePath()));
		}
		// if (!"mp4".equals(video.getExtension())) {
		// FilePath tmp = video.appendExtension("mp4");
		// remux(null, new RemuxDefaultsCfg(), null, video, tmp, null);
		// video = tmp;
		// }
		StreamType a = audio(info(audio, new Lines()));
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
			if ("opus".equals(a.getCodecName()))
				command.add(MP3C);
			else
				command.add("copy");
			command.add("-strict");
			command.add("experimental");
			command.add("-map");
			command.add("0:v:0");
			command.add("-map");
			command.add("1:a:0");
			command.add("-shortest");
			command.add("-movflags");
			command.add("+faststart");
			command.add(command(output));
			call(null, lines, output.getParentPath(), command);
			return true;
		} catch (RuntimeException ex) {
			logger.error("", ex);
			if (lines != null) {
				lines.lines().forEach(l -> logger.error("{}", l));
			}
			output.delete();
			throw ex;
		}
	}

	public void slideshow(Integer secondsPerFrame, Integer framesPerSecondIn, Integer framesPerSecondOut,
			FilePath input, String images, FilePath output, Consumer<String> listener) {
		// In this example each image will have a duration of 5 seconds (the
		// inverse of 1/5 frames per second). The video stream will have a frame
		// rate of 30 fps by duplicating the
		// frames accordingly:
		// ffmpeg -framerate 1/5 -i img%03d.png -c:v libx264 -r 30 -pix_fmt
		// yuv420p out.mp4
		// -start_number
		// List<String> command = new ArrayList<>();
		// command.add(command(getFfmpeg()));
		// command.add("-hide_banner");
		// command.add("-y");
		// command.add("-framerate");
		// if (secondsPerFrame != null) {
		// command.add("1/" + secondsPerFrame);
		// } else if (framesPerSecondIn != null) {
		// command.add("" + framesPerSecondIn);
		// }
		// command.add("-i");
		// command.add(images);
		// command.add("-movflags");
		// command.add("+faststart");
		// command.add("-c:v");
		// command.add("libx264");
		// command.add("-tune");
		// command.add("film");
		// command.add("-tune");
		// command.add("zerolatency");
		// command.add("-profile:v");
		// command.add("high");
		// command.add("-level");
		// command.add("4.2");
		// command.add("-vf");
		// if (framesPerSecondOut == null) {
		// if (secondsPerFrame != null)
		// framesPerSecondOut = 1;
		// else if (framesPerSecondIn != null) framesPerSecondOut =
		// framesPerSecondIn;
		// }
		// command.add("\"fps=" + framesPerSecondOut +
		// ",format=yuv420p\",\"scale=trunc(iw/2)*2:trunc(ih/2)*2\"");
		// command.add("-crf");
		// command.add("15");// HQ 18-23-28 LQ
		// command.add(command(output));
		// command.forEach(System.out::println);
		// call(output.getParentPath(), command, true, listener);
		RemuxDefaultsCfg defaults = new RemuxDefaultsCfg();
		defaults.cfrHQ = 15;
		remux(null, defaults, cfg -> listener, null, output, cfg -> {
			cfg.fixes.fixNotHighProfile = true;
			cfg.slideshowCfg = new SlideshowCfg();
			cfg.slideshowCfg.input = input;
			cfg.slideshowCfg.framesPerSecondIn = framesPerSecondIn;
			cfg.slideshowCfg.framesPerSecondOut = framesPerSecondOut;
			cfg.slideshowCfg.images = images;
			cfg.slideshowCfg.secondsPerFrame = secondsPerFrame;
		});
	}

	public StreamType video(FfprobeType finfo) {
		return finfo == null ? null : videos(finfo).stream().findAny().orElse(null);
	}

	public StreamType audio(FfprobeType finfo) {
		return finfo == null ? null : audios(finfo).stream().findAny().orElse(null);
	}

	public List<StreamType> videos(FfprobeType finfo) {
		return finfo == null ? null
				: finfo.getStreams().getStream().stream()
						.filter(stream -> VIDEO.equalsIgnoreCase(stream.getCodecType())).collect(Collectors.toList());
	}

	public List<StreamType> audios(FfprobeType finfo) {
		return finfo == null ? null
				: finfo.getStreams().getStream().stream()
						.filter(stream -> AUDIO.equalsIgnoreCase(stream.getCodecType())).collect(Collectors.toList());
	}

	public long frames(StreamType videostreaminfo) {
		return videostreaminfo == null || videostreaminfo.getNbFrames() == null ? 0
				: videostreaminfo.getNbFrames().longValue();
	}

	public void loop(FilePath input, int times, FilePath output, Consumer<String> listener) {
		// https://video.stackexchange.com/questions/12905/repeat-loop-input-video-with-ffmpeg
		RemuxDefaultsCfg defaults = new RemuxDefaultsCfg();
		defaults.cfrHQ = 15;
		remux(null, defaults, cfg -> listener, input, output, cfg -> {
			cfg.fixes.fixNotHighProfile = true;
			cfg.repeat = times;
		});
	}

	public List<KeyValue<ProcessInfo, Process>> getActions() {
		return this.actions;
	}

	public void dash(String manifest, FilePath out, Lines lines) {
		List<String> command = new ArrayList<>();
		command.add(command(getFfmpeg()));
		command.add("-hide_banner");
		// command.add("-v");
		// command.add("40");
		command.add("-i");
		command.add(manifest);
		command.add("-c:a");
		command.add("copy");
		command.add("-c:v");
		command.add("copy");
		command.add(command(out));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		call(null, lines, out.getParentPath(), command);
	}

	public void hls(String hls, FilePath out, Lines lines) {
		List<String> command = new ArrayList<>();
		command.add(command(getFfmpeg()));
		command.add("-hide_banner");
		command.add("-i");
		command.add(hls);
		command.add("-c:a");
		command.add("copy");
		command.add("-c:v");
		command.add("copy");
		command.add(command(out));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		call(null, lines, out.getParentPath(), command);
	}

	// public List<String> listDevices() {
	// // https://trac.ffmpeg.org/wiki/DirectShow
	// // ffmpeg -list_devices true -f dshow -i dummy
	// // ffmpeg -f dshow -i video="Integrated Camera" out.mp4
	// // ffmpeg -f dshow -i video="Integrated Camera":audio="Microphone name
	// here" out.mp4
	// // ffmpeg -f dshow -list_options true -i video="Integrated Camera"
	// return null; // TODO
	// }

	public void encodeForYT(FilePath in, FilePath out) {
		// https://gist.github.com/mikoim/27e4e0dc64e384adbcb91ff10a2d3678
		//
		// -movflags faststart moov atom at the front of the file (Fast Start)
		//
		// -profile:v high High Profile
		// -bf 2 2 consecutive B frames
		// -g 30 Closed GOP. GOP of half the frame rate.
		// -coder 1 CABAC
		// -crf 18 Variable bitrate.
		// -pix_fmt yuv420p Chroma subsampling: 4:2:0
		//
		// -c:a aac -profile:a aac_low AAC-LC
		// -b:a 384k Recommended audio bitrates for uploads: Stereo 384 kbps
		//
		List<String> command = new ArrayList<>();
		command.add(command(getFfmpeg()));
		command.add("-hide_banner");
		command.add("-i");
		command.add(command(in));
		command.add("-c:v");
		command.add("libx264");
		command.add("-preset");
		command.add("slow");
		command.add("-profile:v");
		command.add("high");
		command.add("-crf");
		command.add("18");
		command.add("-coder");
		command.add("1");
		command.add("-pix_fmt");
		command.add("yuv420p");
		command.add("-movflags");
		command.add("+faststart");
		command.add("-g");
		command.add("30");
		command.add("-bf");
		command.add("2");
		command.add("-c:a");
		command.add("aac");
		command.add("-b:a");
		command.add("384k");
		command.add("-profile:a");
		command.add("aac_low");
		command.add(command(out));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		call(null, new LinesLog(), out.getParentPath(), command);
	}

	public FilePath mergeUnknowns(FilePath f1, FilePath f2, FilePath output, Lines lines) {
		FfprobeType i1 = info(f1, new Lines());
		FfprobeType i2 = info(f2, new Lines());
		List<FilePath> as = new ArrayList<>();
		List<FilePath> vs = new ArrayList<>();
		if (video(i1) != null) {
			vs.add(f1);
		}
		if (audio(i1) != null) {
			as.add(f1);
		}
		if (video(i2) != null) {
			vs.add(f2);
		}
		if (audio(i2) != null) {
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
			FilePath to = output.child(v.getName() + ".mp4").newFileIndex();
			merge(v, a, to, lines);
			v.delete();
			a.delete();
			return to;
		} else {
			FilePath to1 = output.child(v.getName() + ".mkv").newFileIndex();
			merge(v, a, to1, lines);
			FilePath to2 = to1.appendExtension("mp4").newFileIndex();
			remux(null, new RemuxDefaultsCfg(), x -> System.out::println, to1, to2, cfg -> {
			});
			to1.delete();
			v.delete();
			a.delete();
			return to2;
		}
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = Arrays.asList(command(getFfmpeg()), "-version");
		LinesLog lines = new LinesLog();
		FfmpegTool.call(null, lines, getFfmpeg().getParentPath(), command);
		return lines.lines().get(0);
	}

	// https://www.webtvsolutions.com/support.php?s=ws_webtv_docs&d=clips_video_thumbnails&lang=en
	// https://stackoverflow.com/questions/20022006/generate-all-the-files-vtt-sprite-for-the-tooltip-thumbnails-options-of-jwp
	public void thumbs(FilePath input, FilePath outDir, Integer maxw, Integer perSeconds) {
		if (maxw == null)
			maxw = 200;
		if (perSeconds != null)
			perSeconds = 60;
		FfprobeType info = info(input, null);
		StreamType video = video(info);
		// int h = video.getHeight();
		int w = video.getWidth();
		List<String> command = new ArrayList<>();
		command.add(command(getFfmpeg()));
		command.add("-hide_banner");
		command.add("-i");
		command.add(command(input));
		command.add("-f");
		command.add("image2");
		command.add("-bt");
		command.add("20M");
		command.add("-vf");
		command.add("fps=1/" + perSeconds);
		command.add("\"" + outDir.getAbsolutePath() + "/tv%03d.png" + "\"");
		call(null, null, getFfmpeg().getParentPath(), command);
		int maxw0 = maxw.intValue();
		double scale = (double) maxw0 / w;
		List<BufferedImage> list = outDir.list().stream().sorted()
				.map(f -> ImageTools.getScaledInstance(ImageTools.read(f), scale)).collect(Collectors.toList());
		int cols = (int) Math.ceil(Math.sqrt(list.size()));
		BufferedImage tile = ImageTools.tile(list, cols);
		outDir.list().forEach(FilePath::delete);
		ImageTools.write(tile, outDir.child("tile").createDirectory().child("tile.png"));
	}
}
