package org.jhaws.common.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	@SafeVarargs
	public static void callProcess(List<String> command, FilePath dir, Consumer<String>... consumers) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.directory(dir.checkExists().checkDirectory().toFile());
		pb.redirectErrorStream(true);
		Process p = pb.start();
		Consumer<String> consumer = consumers[0];
		for (int i = 1; i < consumers.length; i++) {
			consumer = consumer.andThen(consumers[i]);
		}
		try (LineIterator lineIterator = new LineIterator(p.getInputStream())) {
			StreamSupport.stream(Spliterators.spliteratorUnknownSize(lineIterator, 0), false).filter(StringUtils::isNotBlank).forEach(consumer);
		}
	}
}
