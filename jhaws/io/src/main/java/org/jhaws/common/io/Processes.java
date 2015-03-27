package org.jhaws.common.io;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
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
		callProcess(new HashMap<>(), command, dir, consumers);
	}

	@SafeVarargs
	public static void callProcess(Map<String, String> env, List<String> command, FilePath dir, Consumer<String>... consumers) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(command);
		if (env != null) {
			builder.environment().putAll(env);
		}
		if (dir != null) {
			builder.directory(dir.checkExists().checkDirectory().toFile());
		}
		builder.redirectInput(Redirect.INHERIT);
		builder.redirectOutput(Redirect.INHERIT); // Redirect.to(file)
		builder.redirectError(Redirect.INHERIT); // Redirect.to(file)
		Process process = builder.start();
		Consumer<String> consumer = consumers[0];
		for (int i = 1; i < consumers.length; i++) {
			consumer = consumer.andThen(consumers[i]);
		}
		try (LineIterator lineIterator = new LineIterator(process.getInputStream())) {
			StreamSupport.stream(Spliterators.spliteratorUnknownSize(lineIterator, 0), false).filter(StringUtils::isNotBlank).forEach(consumer);
		}
		process.destroy();
	}
}
