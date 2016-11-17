package org.jhaws.common.io.media.exif;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jhaws.common.io.console.Processes.callProcess;
import static org.jhaws.common.lang.CollectionUtils8.collectList;
import static org.jhaws.common.lang.CollectionUtils8.join;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.MediaCte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see http://www.sno.phy.queensu.ca/~phil/exiftool/
 */
public class ExifTool implements MediaCte {
	private static final Logger logger = LoggerFactory.getLogger("exif");

	private String splite(String s) {
		return s.substring(s.indexOf(":") + 1).trim();
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

	}

	public static class ExifInfoImpl implements ExifInfo {
		private Integer w = 0;

		private Integer h = 0;

		private String video = "";

		private String audio = "";

		private Double vfr = 0.0;

		private String bitrate = "";

		private String mimetype = "";

		private String duration = "";

		private Double wh = 0.0;

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
	}

	public ExifInfo exifInfo(String exifExe, FilePath path) {
		return exifInfo(exifExe, path, new ExifInfoImpl());
	}

	public ExifInfo exifInfo(String exifExe, FilePath path, ExifInfo exifinfo) {
		if (path.notExists() || new FilePath(exifExe).notExists()) {
			throw new IllegalArgumentException();
		}

		logger.trace("exif {}", path);

		try {
			if (webImageFilter.accept(path) || videoFilter.accept(path) || html5Videofilter.accept(path) || qtFilter.accept(path)) {
				List<String> command = Arrays.asList(exifExe, "-q", path.getAbsolutePath());
				String jc = join(command);
				logger.trace("{}", jc);

				List<String> lines = callProcess(false, command, path.getParentPath(), new Lines()).lines().stream().collect(collectList());

				exifinfo.setW(Integer.parseInt(splite(lines.parallelStream().filter(s -> s.startsWith(IW)).findFirst().orElse(TREMAZ))));
				exifinfo.setH(Integer.parseInt(splite(lines.parallelStream().filter(s -> s.startsWith(IH)).findFirst().orElse(TREMAZ))));
				if (!exifinfo.isWHKnown()) {
					exifinfo.setW(Integer.parseInt(splite(lines.parallelStream().filter(s -> s.startsWith(W)).findFirst().orElse(TREMAZ))));
					exifinfo.setH(Integer.parseInt(splite(lines.parallelStream().filter(s -> s.startsWith(H)).findFirst().orElse(TREMAZ))));
				}

				if (videoFilter.accept(path) || html5Videofilter.accept(path) || qtFilter.accept(path)) {
					exifinfo.setMimetype(splite(lines.parallelStream().filter(s -> s.startsWith(MIME1)).findFirst().orElse(TREMAW)));
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

					exifinfo.setDuration(splite(lines.parallelStream().filter(s -> s.startsWith(DURATION1)).findFirst().orElse(TREMAW)));
					if (isBlank(exifinfo.getDuration())) {
						exifinfo.setDuration(splite(lines.parallelStream().filter(s -> s.startsWith(DURATION2)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getDuration())) {
						exifinfo.setDuration(splite(lines.parallelStream().filter(s -> s.startsWith(DURATION3)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getDuration())) {
						exifinfo.setDuration(splite(lines.parallelStream().filter(s -> s.startsWith(DURATION4)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getDuration())) {
						exifinfo.setDuration(UNKNOWN);
					}

					exifinfo.setVideo(splite(lines.parallelStream().filter(s -> s.startsWith(VIDEO1)).findFirst().orElse(TREMAW)));
					if (isBlank(exifinfo.getVideo()) || exifinfo.getVideo().equals(".")) {
						exifinfo.setVideo(splite(lines.parallelStream().filter(s -> s.startsWith(VIDEO3)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getVideo()) || exifinfo.getVideo().equals(".")) {
						exifinfo.setVideo(splite(lines.parallelStream().filter(s -> s.startsWith(VIDEO2)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getVideo()) || exifinfo.getVideo().equals(".")) {
						exifinfo.setVideo(splite(lines.parallelStream().filter(s -> s.startsWith(VIDEO4)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getVideo()) || exifinfo.getVideo().equals(".")) {
						exifinfo.setVideo(UNKNOWN);
					}

					exifinfo.setAudio(splite(lines.parallelStream().filter(s -> s.startsWith(AUDIO1)).findFirst().orElse(TREMAW)));
					if (isBlank(exifinfo.getAudio()) || exifinfo.getAudio().equals(".")) {
						exifinfo.setAudio(splite(lines.parallelStream().filter(s -> s.startsWith(AUDIO3)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getAudio()) || exifinfo.getAudio().equals(".")) {
						exifinfo.setAudio(splite(lines.parallelStream().filter(s -> s.startsWith(AUDIO2)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getAudio()) || exifinfo.getAudio().equals(".")) {
						exifinfo.setAudio(splite(lines.parallelStream().filter(s -> s.startsWith(AUDIO4)).findFirst().orElse(TREMAW)));
					}
					if (isBlank(exifinfo.getAudio()) || exifinfo.getAudio().equals(".")) {
						exifinfo.setAudio(UNKNOWN);
					}

					exifinfo.setVfr(Double.parseDouble(splite(lines.parallelStream().filter(s -> s.startsWith(VFR1)).findFirst().orElse(TREMAZ))));
					if (exifinfo.getVfr() == 0.0) {
						exifinfo.setVfr(Double.parseDouble(splite(lines.parallelStream().filter(s -> s.startsWith(VFR2)).findFirst().orElse(TREMAZ)).replaceAll("fps", "")));
					}

					exifinfo.setBitrate(splite(lines.parallelStream().filter(s -> s.startsWith(AVGBITRATE1)).findFirst().orElse(TREMAZ)));
					if ("0".equals(exifinfo.getBitrate())) {
						exifinfo.setBitrate(splite(lines.parallelStream().filter(s -> s.startsWith(AVGBITRATE2)).findFirst().orElse(TREMAZ)));
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

	public Duration parseDuration(ExifInfo exif) {
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
}
