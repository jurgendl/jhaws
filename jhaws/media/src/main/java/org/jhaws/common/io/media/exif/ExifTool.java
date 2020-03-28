package org.jhaws.common.io.media.exif;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jhaws.common.io.console.Processes.callProcess;
import static org.jhaws.common.lang.CollectionUtils8.collectList;
import static org.jhaws.common.lang.CollectionUtils8.join;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.MediaCte;
import org.jhaws.common.io.media.Tool;
import org.jhaws.common.lang.CollectionUtils8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see http://www.sno.phy.queensu.ca/~phil/exiftool/
 */
public class ExifTool extends Tool implements MediaCte {
	public static final String EXE = "exiftool";

	public static final String URL = "?";

	private static final String UNKNOWN = "unknown";

	private static final String MIME1 = "MIME Type";

	private static final String DURATION1 = "Media Duration";

	private static final String DURATION2 = "Duration";

	private static final String DURATION3 = "Play Duration";

	private static final String DURATION4 = "Send Duration";

	private static final String VIDEO1 = "Compressor ID";

	private static final String VIDEO2 = "Video Codec";

	private static final String VIDEO3 = "Video Encoding";

	private static final String VIDEO4 = "Video Codec Name";

	private static final String VIDEO5 = "VideoCodecID";

	private static final String VIDEO6 = "CodecID";

	private static final String AUDIO1 = "Audio Format";

	private static final String AUDIO2 = "Audio Codec";

	private static final String AUDIO3 = "Audio Encoding";

	private static final String AUDIO4 = "Audio Codec Name";

	private static final String AUDIO5 = "AudioCodecID";

	private static final String VFR1 = "Video Frame Rate";

	private static final String VFR2 = "Frame Rate";

	private static final String AVGBITRATE1 = "Avg Bitrate";

	private static final String AVGBITRATE2 = "Avg Bytes Per Sec";

	private static final String H = "Source Image Height";

	private static final String W = "Source Image Width";

	private static final String IH = "Image Height";

	private static final String IW = "Image Width";

	private static final Logger logger = LoggerFactory.getLogger("exif");

	public ExifTool() {
		super(System.getenv("EXIF"));
	}

	public ExifTool(String path) {
		super(path);
	}

	public ExifTool(boolean disableAuto) {
		super(disableAuto);
	}

	public static interface ExifInfo {
		boolean isWHKnown();

		Integer getW();

		void setW(Integer w);

		Integer getH();

		void setH(Integer h);

		String getVideo();

		void setVideo(String video);

		String getAudio();

		void setAudio(String audio);

		Double getVfr();

		void setVfr(Double vfr);

		String getBitrate();

		void setBitrate(String bitrate);

		String getMimetype();

		void setMimetype(String mimetype);

		String getDuration();

		void setDuration(String duration);

		Double getWh();

		void setWh(Double wh);

		void setAll(Map<String, String> all);

		Map<String, String> getAll();

		String getOrientation();

		void setOrientation(String orientation);

		default <T> T value(String key) {
			return value(key, null);
		}

		@SuppressWarnings("unchecked")
		default <T> T values(T defaultValue, String... keys) {
			return (T) Arrays.stream(keys).map(this::value).filter(CollectionUtils8.isNotBlankAndNotNull()).findFirst()
					.orElse(defaultValue);
		}

		@SuppressWarnings("unchecked")
		default <T> T value(String key, T defaultValue) {
			T value = (T) getAll().get(key);
			if (value == null)
				value = (T) getAll().get(key.replace(" ", ""));
			if (value == null)
				value = defaultValue;
			return value;
		}
	}

	public static class ExifInfoImpl implements ExifInfo, Serializable {
		private static final long serialVersionUID = 5089869911597040740L;

		private Integer w = 0;

		private Integer h = 0;

		private String video = "";

		private String audio = "";

		private Double vfr = 0.0;

		private String bitrate = "";

		private String mimetype = "";

		private String duration = "";

		private Double wh = 0.0;

		private Map<String, String> all = new HashMap<>();

		private String orientation;

		/**
		 * @see services.impl.ExifInfo#isWHKnown()
		 */
		@Override
		public boolean isWHKnown() {
			return getW() != 0 && getH() != 0;
		}

		/**
		 * @see services.impl.ExifInfo#getW()
		 */
		@Override
		public Integer getW() {
			return w;
		}

		/**
		 * @see services.impl.ExifInfo#setW(java.lang.Integer)
		 */
		@Override
		public void setW(Integer w) {
			this.w = w;
		}

		/**
		 * @see services.impl.ExifInfo#getH()
		 */
		@Override
		public Integer getH() {
			return h;
		}

		/**
		 * @see services.impl.ExifInfo#setH(java.lang.Integer)
		 */
		@Override
		public void setH(Integer h) {
			this.h = h;
		}

		/**
		 * @see services.impl.ExifInfo#getVideo()
		 */
		@Override
		public String getVideo() {
			return video;
		}

		/**
		 * @see services.impl.ExifInfo#setVideo(java.lang.String)
		 */
		@Override
		public void setVideo(String video) {
			this.video = video;
		}

		/**
		 * @see services.impl.ExifInfo#getAudio()
		 */
		@Override
		public String getAudio() {
			return audio;
		}

		/**
		 * @see services.impl.ExifInfo#setAudio(java.lang.String)
		 */
		@Override
		public void setAudio(String audio) {
			this.audio = audio;
		}

		/**
		 * @see services.impl.ExifInfo#getVfr()
		 */
		@Override
		public Double getVfr() {
			return vfr;
		}

		/**
		 * @see services.impl.ExifInfo#setVfr(java.lang.Double)
		 */
		@Override
		public void setVfr(Double vfr) {
			this.vfr = vfr;
		}

		/**
		 * @see services.impl.ExifInfo#getBitrate()
		 */
		@Override
		public String getBitrate() {
			return bitrate;
		}

		/**
		 * @see services.impl.ExifInfo#setBitrate(java.lang.String)
		 */
		@Override
		public void setBitrate(String bitrate) {
			this.bitrate = bitrate;
		}

		/**
		 * @see services.impl.ExifInfo#getMimetype()
		 */
		@Override
		public String getMimetype() {
			return mimetype;
		}

		/**
		 * @see services.impl.ExifInfo#setMimetype(java.lang.String)
		 */
		@Override
		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}

		/**
		 * @see services.impl.ExifInfo#getDuration()
		 */
		@Override
		public String getDuration() {
			return duration;
		}

		/**
		 * @see services.impl.ExifInfo#setDuration(java.lang.String)
		 */
		@Override
		public void setDuration(String duration) {
			this.duration = duration;
		}

		/**
		 * @see services.impl.ExifInfo#getWh()
		 */
		@Override
		public Double getWh() {
			return wh;
		}

		/**
		 * @see services.impl.ExifInfo#setWh(java.lang.Double)
		 */
		@Override
		public void setWh(Double wh) {
			this.wh = wh;
		}

		@Override
		public Map<String, String> getAll() {
			return this.all;
		}

		@Override
		public void setAll(Map<String, String> all) {
			this.all = all;
		}

		@Override
		public String toString() {
			return "ExifInfoImpl [" + (this.w != null ? "w=" + this.w + ", " : "")
					+ (this.h != null ? "h=" + this.h + ", " : "")
					+ (this.video != null ? "video=" + this.video + ", " : "")
					+ (this.audio != null ? "audio=" + this.audio + ", " : "")
					+ (this.vfr != null ? "vfr=" + this.vfr + ", " : "")
					+ (this.bitrate != null ? "bitrate=" + this.bitrate + ", " : "")
					+ (this.mimetype != null ? "mimetype=" + this.mimetype + ", " : "")
					+ (this.duration != null ? "duration=" + this.duration + ", " : "")
					+ (this.wh != null ? "wh=" + this.wh : "") + "]";
		}

		@Override
		public String getOrientation() {
			return this.orientation;
		}

		@Override
		public void setOrientation(String orientation) {
			this.orientation = orientation;
		}
	}

	public void comment(FilePath path, String comment) {
		if (path.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(command(executable), "-Comment=\"" + comment + "\"",
				path.getAbsolutePath());
		String jc = join(command);
		logger.trace("{}", jc);
		callProcess(null, false, command, path.getParentPath(), new Lines()).lines().stream().collect(collectList());
		path.getParentPath().child(path.getFileNameString() + "_original").delete();
	}

	public String comment(FilePath path) {
		if (path.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(command(executable), "-Comment", path.getAbsolutePath());
		String jc = join(command);
		logger.trace("{}", jc);
		return callProcess(null, false, command, path.getParentPath(), new Lines()).lines().stream()
				.filter(l -> l.toLowerCase().startsWith("comment")).map(l -> l.split(":")[1].trim()).findFirst()
				.orElse(null);
	}

	public void program(FilePath path, String program) {
		if (path.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(command(executable), "-Software=\"" + program + "\"",
				path.getAbsolutePath());
		String jc = join(command);
		logger.trace("{}", jc);
		callProcess(null, false, command, path.getParentPath(), new Lines()).lines().stream().collect(collectList());
		path.getParentPath().child(path.getFileNameString() + "_original").delete();
	}

	public String program(FilePath path) {
		if (path.notExists() || executable.notExists()) {
			return null;
		}
		List<String> command = Arrays.asList(command(executable), "-Software", path.getAbsolutePath());
		String jc = join(command);
		logger.trace("{}", jc);
		return callProcess(null, false, command, path.getParentPath(), new Lines()).lines().stream()
				.filter(l -> l.toLowerCase().contains("software")).map(l -> l.split(":")[1].trim()).findFirst()
				.orElse(null);
	}

	public ExifInfo exifInfo(FilePath path) {
		return exifInfo(path, new ExifInfoImpl());
	}

	// -X = xml, -json
	public ExifInfo exifInfo(FilePath path, ExifInfo exifinfo) {
		if (path.notExists()) {
			throw new IllegalArgumentException();
		}
		if (executable.notExists()) {
			throw new IllegalArgumentException();
		}
		if (exifinfo == null) {
			exifinfo = new ExifInfoImpl();
		}

		logger.debug("exif {}", path);

		try {
			if (webImageFilter.accept(path) || videoFilter.accept(path) || html5Videofilter.accept(path)
					|| qtFilter.accept(path)) {
				List<String> command = exifCommand(path);

				List<String> lines = Processes.callProcess(null, false, null, System.getenv(), command,
						path.getParentPath(), null, null, new Lines()).lines().stream().collect(collectList());
				// lines.forEach(System.out::println);

				if (lines.stream().anyMatch(x -> //
				x.startsWith("No file specified")//
						|| x.startsWith("No matching files")//
						|| x.startsWith("Error: File not found")//
				)) {
					FilePath tmp = path.getParentPath().child("" + System.currentTimeMillis());
					try {
						Files.createLink(tmp.toPath(), path.toPath());
						command = Arrays.asList(//
								command(executable) //
								// , "-charset" //
								// , "FileName=" +
								// System.getProperty("sun.jnu.encoding")//
								// , "FileName=utf-8"//
								, "-q"//
								, "-json"//
								, "\"" + tmp.getFileNameString() + "\""//
						);
						lines = Processes.callProcess(null, false, null, System.getenv(), command, path.getParentPath(),
								null, null, new Lines()).lines().stream().collect(collectList());
						// lines.forEach(System.out::println);
					} catch (Exception x) {
						System.out.println(
								"could not create " + tmp.getAbsolutePath() + " from " + path.getAbsolutePath());
						x.printStackTrace(System.out);
					} finally {
						if (tmp.exists()) {
							try {
								tmp.delete();
							} catch (Exception x) {
								System.out.println("could not delete " + tmp.getAbsolutePath());
								x.printStackTrace(System.out);
							}
						}
					}
				}

				JsonStructure jso = Json
						.createReader(new InputStreamReader(
								new ByteArrayInputStream(lines.stream().collect(Collectors.joining(" ")).getBytes())))
						.read();
				@SuppressWarnings("unchecked")
				Map<String, Object> all = (Map<String, Object>) jso.asJsonArray().get(0);
				Map<String, String> all2 = exifinfo.getAll();
				all.entrySet().forEach(e -> all2.put(e.getKey(), toString(e.getValue())));

				exifinfo.setW(Integer.parseInt(exifinfo.values("0", IW, W)));
				exifinfo.setH(Integer.parseInt(exifinfo.values("0", IH, H)));
				exifinfo.setOrientation(exifinfo.value("Orientation"));

				if (videoFilter.accept(path) || html5Videofilter.accept(path) || qtFilter.accept(path)) {
					exifinfo.setMimetype(exifinfo.value(MIME1));
					if (isBlank(exifinfo.getMimetype())) {
						exifinfo.setMimetype("video/" + path.getExtension().toLowerCase()//
								.replace(FLV, "flash")//
								.replace("ogv", "ogg")//
								.replace(M4V, MP4)//
								.replace(MOV, "quicktime")//
								.replace("3gp", "3gpp")//
								.replace("3g2", "3gpp2")//
								.replace("m2v", "mpeg"));//
					}

					exifinfo.setDuration(exifinfo.values(UNKNOWN, DURATION1, DURATION2, DURATION3, DURATION4));
					exifinfo.setVideo(exifinfo.values(UNKNOWN, VIDEO1, VIDEO3, VIDEO2, VIDEO4, VIDEO5, VIDEO6));
					exifinfo.setAudio(exifinfo.values(UNKNOWN, AUDIO1, AUDIO3, AUDIO2, AUDIO4, AUDIO5));
					String vfr = exifinfo.value(VFR1);
					if (StringUtils.isBlank(vfr) || "0".equals(vfr)) {
						vfr = exifinfo.value(VFR2);
					}
					if (StringUtils.isBlank(vfr) || "0".equals(vfr)) {
						vfr = exifinfo.value(AVGBITRATE1);
					}
					if (StringUtils.isBlank(vfr) || "0".equals(vfr)) {
						vfr = exifinfo.value(AVGBITRATE2);
					}
					if (StringUtils.isNotBlank(vfr)) {
						exifinfo.setVfr(Double
								.parseDouble(vfr.replace("Mbps", "").replace("kbps", "").replace("fps", "").trim()));
					}
				}

				if (exifinfo.isWHKnown()) {
					exifinfo.setWh((double) exifinfo.getW().intValue() / exifinfo.getH().intValue());
				}
			}
		} catch (Exception ex) {
			logger.error("{}", path, ex);
		}

		return exifinfo;
	}

	public List<String> exifCommand(FilePath path) {
		List<String> command = Arrays.asList(//
				command(executable) //
				// , "-charset" //
				// , "FileName=" +
				// System.getProperty("sun.jnu.encoding")//
				// , "FileName=utf-8"//
				, "-q"//
				, "-json"//
				, "\"" + path.getFileNameString() + "\""//
		);
		String jc = join(command);
		logger.trace(jc);
		return command;
	}

	@SuppressWarnings("incomplete-switch")
	private String toString(Object value) {
		if (value instanceof JsonNumber) {
			return String.valueOf(JsonNumber.class.cast(value).numberValue());
		}
		if (value instanceof JsonString) {
			return JsonString.class.cast(value).getString();
		}
		if (value instanceof JsonArray) {
			return Arrays.stream(JsonArray.class.cast(value).toArray()).map(this::toString)
					.collect(Collectors.joining(";"));
		}
		if (value instanceof JsonObject) {
			return toString(JsonObject.class.cast(value).asJsonArray());
		}
		if (value instanceof JsonValue) {
			switch (JsonValue.class.cast(value).getValueType()) {
			case FALSE:
				return "false";
			case TRUE:
				return "true";
			}
		}
		throw new IllegalArgumentException(value.getClass().getName());
	}

	public static Duration parseDuration(ExifInfo exif) {
		Duration duration = Duration.ofMillis(0);
		{
			Matcher m = Pattern.compile("(\\d++)\\.(\\d++) s").matcher(exif.getDuration());
			if (m.find()) {
				int ss = Integer.parseInt(m.group(1));
				int ms = Integer.parseInt(m.group(2));
				duration = duration.plusSeconds(ss);
				duration = duration.plusMillis(ms);
			}
		}
		{
			Matcher m = Pattern.compile("(\\d++):(\\d++):(\\d++)").matcher(exif.getDuration());
			if (m.find()) {
				int hh = Integer.parseInt(m.group(1));
				int mm = Integer.parseInt(m.group(2));
				int ss = Integer.parseInt(m.group(3));
				duration = duration.plusHours(hh);
				duration = duration.plusMinutes(mm);
				duration = duration.plusSeconds(ss);
			}
		}
		return duration;
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = Arrays.asList(command(executable), "-ver");
		Lines lines = new Lines();
		Tool.call(null, lines, executable.getParentPath(), command);
		return lines.lines().get(0);
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
			//
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
}
