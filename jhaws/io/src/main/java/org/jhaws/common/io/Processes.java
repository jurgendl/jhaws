package org.jhaws.common.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath.Iterators.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Processes {
	private static Logger logger = LoggerFactory.getLogger(Processes.class);

	public static class Lines implements Consumer<String> {
		List<String> lines = new ArrayList<>();

		@Override
		public void accept(String t) {
			this.lines.add(t);
		}

		public List<String> lines() {
			return this.lines;
		}

		@Override
		public String toString() {
			return this.lines.toString();
		}
	}

	public static class Text implements Consumer<String> {
		StringBuilder linesText = new StringBuilder();

		@Override
		public void accept(String t) {
			this.linesText.append(t).append("\n");
		}

		public String getText() {
			return this.linesText.toString();
		}

		@Override
		public String toString() {
			return this.getText();
		}
	}

	public static class Out implements Consumer<String> {
		@Override
		public void accept(String t) {
			System.out.println("> " + t);
		}
	}

	@SafeVarargs
	public static <C extends Consumer<String>> C callProcess(List<String> command, FilePath dir, C consumer, Consumer<String>... consumers) throws UncheckedIOException {
		return callProcess(null, new HashMap<>(), command, dir, null, null, consumer, consumers);
	}

	@SafeVarargs
	public static <C extends Consumer<String>> C callProcess(FilePath input, Map<String, String> env, List<String> command, FilePath dir, FilePath outputLog, FilePath errorLog,
			C consumer, Consumer<String>... consumers) throws UncheckedIOException {
		logger.debug("{}> {}", dir.getAbsolutePath(), command.stream().collect(Collectors.joining(" ")));
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		if (env != null && env.size() > 0) {
			builder.environment().putAll(env);
		}
		if (dir != null) {
			if (dir.notExists()) {
				dir.createDirectories();
			} else {
				if (!dir.isDirectory()) {
					throw new UncheckedIOException(new IOException(dir + " is not a directory"));
				}
			}
			builder.directory(dir.toFile());
		}
		if (input != null)
			builder.redirectInput(Redirect.from(input.toFile()));
		else {
			// builder.redirectInput(Redirect.INHERIT);
		}
		if (outputLog != null)
			if (outputLog.exists())
				builder.redirectOutput(Redirect.appendTo(outputLog.toFile()));
			else
				builder.redirectOutput(Redirect.to(outputLog.toFile()));
		else {
			// builder.redirectOutput(Redirect.INHERIT);
		}
		if (errorLog != null)
			if (errorLog.exists())
				builder.redirectError(Redirect.appendTo(errorLog.toFile()));
			else
				builder.redirectError(Redirect.to(errorLog.toFile()));
		else {
			builder.redirectError(Redirect.PIPE);
		}
		Process process;
		try {
			process = builder.start();
		} catch (IOException e1) {
			throw new UncheckedIOException(e1);
		}
		Consumer<String> allConsumers = null;
		if (consumer != null) {
			allConsumers = consumer;
			Arrays.stream(consumers).forEach(allConsumers::andThen);
			try (LineIterator lineIterator = new LineIterator(process.getInputStream())) {
				StreamSupport.stream(Spliterators.spliteratorUnknownSize(lineIterator, 0), false).filter(StringUtils::isNotBlank).forEach(allConsumers);
			} catch (IOException e1) {
				throw new UncheckedIOException(e1);
			}
		}
		try {
			int exitValue = process.waitFor();
			if (allConsumers != null)
				allConsumers.accept("exit value=" + exitValue);
		} catch (InterruptedException e) {
			//
		} finally {
			process.destroy();
		}
		return consumer;
	}
}
