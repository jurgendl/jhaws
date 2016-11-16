package org.jhaws.common.io.media.ffmpeg;

import static org.jhaws.common.io.console.Processes.callProcess;
import static org.jhaws.common.lang.CollectionUtils8.join;
import static org.jhaws.common.lang.DateTime8.printUpToSeconds;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.io.media.MediaCte;
import org.jhaws.common.io.media.ffmpeg.xml.FfprobeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegTool implements MediaCte {
	private static final Logger logger_ffmpeg = LoggerFactory.getLogger("ffmpeg");

	private static final Logger logger_ffprobe = LoggerFactory.getLogger("ffprobe");

	protected static final JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(org.jhaws.common.io.media.ffmpeg.xml.ObjectFactory.class.getPackage().getName());

	protected FilePath ffmpeg;

	protected FilePath ffprobe;

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
			return FfmpegTool.unmarshall(xml);
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
			command.add("\"" + getFfmpeg() + "\"");
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
			logger_ffmpeg.warn("{}", join(command));
			callProcess(true, command, vid.getParentPath(), new Processes.Out());
			return splashFile.exists();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
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
}
