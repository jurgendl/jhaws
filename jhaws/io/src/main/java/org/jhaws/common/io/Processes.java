package org.jhaws.common.io;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath.Iterators.LineIterator;

public class Processes {
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
			System.out.println(t);
		}
	}

	@SafeVarargs
	public static void callProcess(List<String> command, FilePath dir, Consumer<String>... consumers) throws IOException {
		callProcess(null, new HashMap<>(), command, dir, null, null, consumers);
	}

	@SafeVarargs
	public static void callProcess(FilePath input, Map<String, String> env, List<String> command, FilePath dir, FilePath outputLog, FilePath errorLog,
			Consumer<String>... consumers) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(command);
		if (env != null) {
			builder.environment().putAll(env);
		}
		if (dir != null) {
			if (dir.notExists()) {
				dir.createDirectories();
			} else {
				if (!dir.isDirectory()) {
					throw new IOException(dir + " is not a directory");
				}
			}
			builder.directory(dir.toFile());
		}
		if (input != null)
			builder.redirectInput(Redirect.from(input.toFile()));
		else
			builder.redirectInput(Redirect.INHERIT);
		if (outputLog != null)
			if (outputLog.exists())
				builder.redirectOutput(Redirect.appendTo(outputLog.toFile()));
			else
				builder.redirectOutput(Redirect.to(outputLog.toFile()));
		else
			builder.redirectOutput(Redirect.INHERIT);
		if (errorLog != null)
			if (errorLog.exists())
				builder.redirectError(Redirect.appendTo(errorLog.toFile()));
			else
				builder.redirectError(Redirect.to(errorLog.toFile()));
		else
			builder.redirectError(Redirect.INHERIT);
		Process process = builder.start();
		Consumer<String> allConsumers = consumers[0];
		Arrays.stream(consumers).skip(1).forEach(allConsumers::andThen);
		try (LineIterator lineIterator = new LineIterator(process.getInputStream())) {
			StreamSupport.stream(Spliterators.spliteratorUnknownSize(lineIterator, 0), false).filter(StringUtils::isNotBlank).forEach(allConsumers);
		}
		process.destroy();
	}
}
