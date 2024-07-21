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
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
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
import org.jhaws.common.lang.IntegerValue;

/**
 * @see http://www.sno.phy.queensu.ca/~phil/exiftool/
 */
//
// https://exiftool.org/forum/index.php?topic=3916.0
// .ExifTool_config
// %Image::ExifTool::UserDefined::Options = (
// LargeFileSupport => 1,
// );
public class ExifTool extends Tool implements MediaCte {
	public static final String EXE = "exiftool";

	public static final String URL = "?";

	private static final String UNKNOWN = "unknown";

	private static final Pattern MIME1 = Pattern.compile("^MIME[ ]?Type$", Pattern.CASE_INSENSITIVE);

	private static final Pattern DURATION1 = Pattern.compile("^Duration$", Pattern.CASE_INSENSITIVE);

	private static final Pattern DURATION2 = Pattern.compile("^Media[ ]?Duration$", Pattern.CASE_INSENSITIVE);

	private static final Pattern DURATION3 = Pattern.compile("^Play[ ]?Duration$", Pattern.CASE_INSENSITIVE);

	private static final Pattern DURATION4 = Pattern.compile("^Send[ ]?Duration$", Pattern.CASE_INSENSITIVE);

	private static final Pattern DURATION5 = Pattern.compile("^Track[ ]?Duration$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO1 = Pattern.compile("^Compressor[ ]?ID$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO2 = Pattern.compile("^Video[ ]?Codec$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO3 = Pattern.compile("^Video[ ]?Encoding$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO4 = Pattern.compile("^Video[ ]?Codec[ ]?Name$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO5 = Pattern.compile("^Video[ ]?Codec[ ]?ID$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VIDEO6 = Pattern.compile("^Codec[ ]?ID$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AUDIO1 = Pattern.compile("^Audio[ ]?Format$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AUDIO2 = Pattern.compile("^Audio[ ]?Codec$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AUDIO3 = Pattern.compile("^Audio[ ]?Encoding$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AUDIO4 = Pattern.compile("^Audio[ ]?Codec[ ]?Name$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AUDIO5 = Pattern.compile("^Audio[ ]?Codec[ ]?ID$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VFR1 = Pattern.compile("^Video[ ]?Frame[ ]?Rate$", Pattern.CASE_INSENSITIVE);

	private static final Pattern VFR2 = Pattern.compile("^Frame[ ]?Rate$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AVGBITRATE1 = Pattern.compile("^Avg Bitrate$", Pattern.CASE_INSENSITIVE);

	private static final Pattern AVGBITRATE2 = Pattern.compile("^Avg Bytes Per Sec$", Pattern.CASE_INSENSITIVE);

	private static final Pattern H = Pattern.compile("^Source[ ]?Image[ ]?Height$", Pattern.CASE_INSENSITIVE);

	private static final Pattern W = Pattern.compile("^Source[ ]?Image[ ]?Width$", Pattern.CASE_INSENSITIVE);

	private static final Pattern IH = Pattern.compile("^Image[ ]?Height$", Pattern.CASE_INSENSITIVE);

	private static final Pattern IW = Pattern.compile("^Image[ ]?Width$", Pattern.CASE_INSENSITIVE);

	protected boolean usePerl = false;

	protected String perl = "perl";

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

		default String values(Pattern... keys) {
			return values(null, keys);
		}

		default String values(String defaultValue, Pattern... keys) {
			return getAll()//
					.entrySet()//
					.stream()//
					.filter(e -> Arrays.stream(keys).anyMatch(k -> k.matcher(e.getKey()).find()))//
					.map(Map.Entry::getValue)//
					.findAny()//
					.orElse(defaultValue);
		}

		default String value(Pattern key) {
			return value(null, key);
		}

		default String value(String defaultValue, Pattern key) {
			return getAll()//
					.entrySet()//
					.stream()//
					.filter(e -> key.matcher(e.getKey()).find())//
					.map(Map.Entry::getValue)//
					.findAny()//
					.orElse(defaultValue);
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
			return "ExifInfoImpl [" + (this.w != null ? "w=" + this.w + ", " : "") + (this.h != null ? "h=" + this.h + ", " : "")
					+ (this.video != null ? "video=" + this.video + ", " : "") + (this.audio != null ? "audio=" + this.audio + ", " : "")
					+ (this.vfr != null ? "vfr=" + this.vfr + ", " : "") + (this.bitrate != null ? "bitrate=" + this.bitrate + ", " : "")
					+ (this.mimetype != null ? "mimetype=" + this.mimetype + ", " : "") + (this.duration != null ? "duration=" + this.duration + ", " : "")
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

	public void comment(FilePath p, String comment) {
		if (p.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(perl(), command(executable), "-Comment=\"" + comment + "\"", p.getAbsolutePath());
		String jc = join(command);
		loggeri.trace("{}", jc);
		callProcess(null, false, command, p.getParentPath(), new Lines()).lines().stream().collect(collectList());
		p.getParentPath().child(p.getFileNameString() + "_original").delete();
	}

	protected String perl() {
		return usePerl ? perl : null;
	}

	public String comment(FilePath p) {
		if (p.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(perl(), command(executable), "-Comment", p.getAbsolutePath());
		String jc = join(command);
		loggeri.trace("{}", jc);
		return callProcess(null, false, command, p.getParentPath(), new Lines()).lines().stream().filter(l -> l.toLowerCase().startsWith("comment"))
				.map(l -> l.split(":")[1].trim()).findFirst().orElse(null);
	}

	public void program(FilePath p, String program) {
		if (p.notExists() || executable.notExists()) {
			throw new IllegalArgumentException();
		}
		List<String> command = Arrays.asList(perl(), command(executable), "-Software=\"" + program + "\"", p.getAbsolutePath());
		String jc = join(command);
		loggeri.trace("{}", jc);
		callProcess(null, false, command, p.getParentPath(), new Lines()).lines().stream().collect(collectList());
		p.getParentPath().child(p.getFileNameString() + "_original").delete();
	}

	public String program(FilePath p) {
		if (p.notExists() || executable.notExists()) {
			return null;
		}
		List<String> command = Arrays.asList(perl(), command(executable), "-Software", p.getAbsolutePath());
		String jc = join(command);
		loggeri.trace("{}", jc);
		return callProcess(null, false, command, p.getParentPath(), new Lines()).lines().stream().filter(l -> l.toLowerCase().contains("software")).map(l -> l.split(":")[1].trim())
				.findFirst().orElse(null);
	}

	public List<ExifInfo> exifInfoMulti(FilePath sameDriveTmp, List<FilePath> p) {
		List<ExifInfo> exifinfo = new ArrayList<>(p.size());
		for (int i = 0; i < p.size(); i++) {
			exifinfo.add(new ExifInfoImpl());
		}
		return exifInfoMulti(sameDriveTmp, p, exifinfo);
	}

	public static boolean containsNonUTF8Characters(String input) {
		try {
			String clean = input.replace(" ", "_").replace("%", "_").replace("/", "_").replace("\\", "_").replace(":", "_");
			String encoded = URLEncoder.encode(clean, "utf-8");
			return !clean.equals(encoded);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<ExifInfo> exifInfoMulti(FilePath sameDriveTmp, List<FilePath> p, List<ExifInfo> exifinfos) {
		if (p.size() != exifinfos.size())
			throw new IllegalArgumentException();
		for (FilePath pp : p)
			if (pp.notExists())
				throw new IllegalArgumentException();
		if (executable.notExists())
			throw new IllegalArgumentException();
		if (p.size() == 1)
			return Arrays.asList(exifInfo(p.get(0), exifinfos.get(0)));
		FilePath tmpFolder = FilePath.createTempDirectory();
		tmpFolder.createDirectory();

		FilePath jsonFile = tmpFolder.child(System.currentTimeMillis() + ".json");
		List<String> command = new ArrayList<String>(Arrays.asList(//
				command(executable) //
				, "-q"//
				, "-json"//
				, "-W+!"//
				, "\"" + jsonFile.getAbsolutePath() + "\""//
		));
		Map<String, FilePath> nameMapping = new HashMap<>();
		List<Path> links = new ArrayList<>();
		IntegerValue ii = new IntegerValue();
		p.stream().forEach(a -> {
			String absolutePath = a.getAbsolutePath();
			if (containsNonUTF8Characters(absolutePath)) {
				FilePath tmp = sameDriveTmp.child(ii.add() + "-" + System.currentTimeMillis());
				try {
					links.add(Files.createLink(tmp.toPath(), a.toPath()));
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
				absolutePath = tmp.getAbsolutePath();
			}
			absolutePath = absolutePath.replace("\\", "/");
			nameMapping.put(absolutePath, a);
			command.add("\"" + absolutePath + "\"");
		});
		String jc = join(command);
		try {
			loggeri.trace(jc);

			List<String> lines = Processes.callProcess(null, false, null, System.getenv(), command, executable.getParentPath(), null, null, new Lines()).lines().stream()
					.collect(collectList());

			lines.forEach(System.out::println);

			String jsonString = jsonFile.readAll();

			JsonStructure jso = Json.createReader(new InputStreamReader(new ByteArrayInputStream(jsonString.getBytes()))).read();
			JsonArray arr = jso.asJsonArray();
			for (int i = 0; i < arr.size(); i++) {
				JsonValue it = arr.get(i);
				@SuppressWarnings("unchecked")
				Map<String, Object> all = (Map<String, Object>) it;
				String sourceFile = ((JsonString) all.get("SourceFile")).getString().replace("\\", "/");
				FilePath corr = nameMapping.get(sourceFile);
				int indexOf = p.indexOf(corr);
				ExifInfo exifinfo = exifinfos.get(indexOf);
				Map<String, String> all2 = exifinfo.getAll();
				extracted(p.get(indexOf), exifinfo, all, all2);
			}
		} catch (Exception ex) {
			loggeri.error("{}", p, ex);
		}

		links.forEach(x -> {
			try {
				Files.delete(x);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		for (FilePath pp : p)
			if (pp.notExists())
				throw new IllegalArgumentException();

		return exifinfos;
	}

	public ExifInfo exifInfo(FilePath p) {
		return exifInfo(p, new ExifInfoImpl());
	}

	// -X = xml, -json
	public ExifInfo exifInfo(FilePath p, ExifInfo exifinfo) {
		if (p.notExists()) {
			throw new IllegalArgumentException();
		}
		if (executable.notExists()) {
			throw new IllegalArgumentException();
		}
		if (exifinfo == null) {
			exifinfo = new ExifInfoImpl();
		}

		loggeri.debug("exif {}", p);

		try {
			if (webImageFilter.accept(p) || videoFilter.accept(p) || html5Videofilter.accept(p) || qtFilter.accept(p)) {
				List<String> command = exifCommand(p);

				List<String> lines = Processes.callProcess(null, false, null, System.getenv(), command, p.getParentPath(), null, null, new Lines()).lines().stream()
						.collect(collectList());
				// lines.forEach(System.out::println);

				if (lines.stream().anyMatch(x -> //
				x.startsWith("No file specified")//
						|| x.startsWith("No matching files")//
						|| x.startsWith("Error: File not found")//
				)) {
					FilePath tmp = p.getParentPath().child("" + System.currentTimeMillis());
					try {
						Files.createLink(tmp.toPath(), p.toPath());
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
						lines = Processes.callProcess(null, false, null, System.getenv(), command, p.getParentPath(), null, null, new Lines()).lines().stream()
								.collect(collectList());
						// lines.forEach(System.out::println);
					} catch (Exception x) {
						System.out.println("could not create " + tmp.getAbsolutePath() + " from " + p.getAbsolutePath());
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

				String jsonString = lines.stream().collect(Collectors.joining(" "));
				JsonStructure jso = Json.createReader(new InputStreamReader(new ByteArrayInputStream(jsonString.getBytes()))).read();
				@SuppressWarnings("unchecked")
				Map<String, Object> all = (Map<String, Object>) jso.asJsonArray().get(0);
				Map<String, String> all2 = exifinfo.getAll();
				extracted(p, exifinfo, all, all2);
			}
		} catch (Exception ex) {
			loggeri.error("{}", p, ex);
		}

		return exifinfo;
	}

	private void extracted(FilePath p, ExifInfo exifinfo, Map<String, Object> all, Map<String, String> all2) {
		all.entrySet().forEach(e -> all2.put(e.getKey(), toString(e.getValue())));

		exifinfo.setW(Integer.parseInt(exifinfo.values("0", IW, W)));
		exifinfo.setH(Integer.parseInt(exifinfo.values("0", IH, H)));
		exifinfo.setOrientation(exifinfo.value(Pattern.compile("^Orientation$", Pattern.CASE_INSENSITIVE)));

		exifinfo.setMimetype(exifinfo.value(MIME1));

		if (videoFilter.accept(p) || html5Videofilter.accept(p) || qtFilter.accept(p)) {
			if (isBlank(exifinfo.getMimetype())) {
				exifinfo.setMimetype("video/" + p.getExtension().toLowerCase()//
						.replace(FLV, "flash")//
						.replace("ogv", "ogg")//
						.replace(M4V, MP4)//
						.replace(MOV, "quicktime")//
						.replace("3gp", "3gpp")//
						.replace("3g2", "3gpp2")//
						.replace("m2v", "mpeg"));//
			}

			exifinfo.setDuration(exifinfo.values(UNKNOWN, DURATION1, DURATION2, DURATION3, DURATION4, DURATION5));
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
				exifinfo.setVfr(Double.parseDouble(vfr.replace("Mbps", "").replace("kbps", "").replace("fps", "").trim()));
			}
		}

		if (exifinfo.isWHKnown()) {
			exifinfo.setWh((double) exifinfo.getW().intValue() / exifinfo.getH().intValue());
		}
	}

	public List<String> exifCommand(FilePath p) {
		List<String> command = Arrays.asList(//
				command(executable) //
				// , "-charset" //
				// , "FileName=" +
				// System.getProperty("sun.jnu.encoding")//
				// , "FileName=utf-8"//
				, "-q"//
				, "-json"//
				, "\"./" + p.getFileNameString() + "\""//
		);
		String jc = join(command);
		loggeri.trace(jc);
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
			return Arrays.stream(JsonArray.class.cast(value).toArray()).map(this::toString).collect(Collectors.joining(";"));
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
		return parseDuration(exif.getDuration());
	}

	public static Duration parseDuration(String duration) {
		Duration duration = Duration.ofMillis(0);
		{
			Matcher m = Pattern.compile("(\\d++)\\.(\\d++) s").matcher(duration);
			if (m.find()) {
				int ss = Integer.parseInt(m.group(1));
				int ms = Integer.parseInt(m.group(2));
				duration = duration.plusSeconds(ss);
				duration = duration.plusMillis(ms);
			}
		}
		{
			Matcher m = Pattern.compile("(\\d++):(\\d++):(\\d++).(\\d++)").matcher(duration);
			if (m.find()) {
				int hh = Integer.parseInt(m.group(1));
				int mm = Integer.parseInt(m.group(2));
				int ss = Integer.parseInt(m.group(3));
				String s100 = m.group(4);
				duration = duration.plusHours(hh);
				duration = duration.plusMinutes(mm);
				duration = duration.plusSeconds(ss);
				duration = duration.plusMillis(Integer.parseInt(s100) * (s100.length() == 2 ? 10 : 1));
			}
		}
		{
			Matcher m = Pattern.compile("(\\d++):(\\d++):(\\d++)").matcher(duration);
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
		List<String> command = Arrays.asList(perl(), command(executable), "-ver");
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
			try (InputStream in = new URL(URL + tmp).openConnection().getInputStream(); OutputStream out = executable.newBufferedOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	public void keywords(FilePath p, boolean overwrite, String... keywords) {
		List<String> command = new ArrayList<>();
		command.add(command(getExecutable()));
		// command.add("-v3");
		command.add("-preserve");
		command.add("-m");
		if (!overwrite) {
			// command.add("-if");
			// command.add("'not defined $IPTC:Keywords'");
		}
		command.add("-overwrite_original");
		Arrays.stream(keywords).map(s -> "-IPTC:Keywords=" + s).forEach(command::add);
		command.add(command(p));
		List<String> lines = Processes.callProcess(null, false, null, System.getenv(), command, getExecutable().getParentPath(), null, null, new Lines()).lines().stream()
				.collect(collectList());
		lines.forEach(System.out::println);
		return;
	}

	public boolean isUsePerl() {
		return this.usePerl;
	}

	public void setUsePerl(boolean usePerl) {
		this.usePerl = usePerl;
	}

	public String getPerl() {
		return this.perl;
	}

	public void setPerl(String perl) {
		this.perl = perl;
	}
}
