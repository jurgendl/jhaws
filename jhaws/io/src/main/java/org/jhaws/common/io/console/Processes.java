package org.jhaws.common.io.console;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Iterators.LineIterator;
import org.jhaws.common.lang.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Processes {
	private static Logger logger = LoggerFactory.getLogger(Processes.class);

	public static class Lines implements Consumer<String> {
		protected List<String> lines = new ArrayList<>();

		@Override
		public void accept(String t) {
			this.lines.add(t);
		}

		public List<String> lines() {
			return this.lines;
		}

		@Override
		public String toString() {
			return this.lines.stream().collect(Collectors.joining("\n"));
		}
	}

	public static class Text implements Consumer<String> {
		protected StringBuilder linesText = new StringBuilder();

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

	public static class Log implements Consumer<String> {
		@Override
		public void accept(String t) {
			logger.info("> " + t);
		}
	}

	@SafeVarargs
	public static <C extends Consumer<String>> C callProcess(Value<Process> processHolder, boolean throwExitValue,
			List<String> command, FilePath dir, C consumer, Consumer<String>... consumers) throws UncheckedIOException {
		return callProcess(processHolder, throwExitValue, null, new HashMap<>(), command, dir, null, null, consumer,
				consumers);
	}

	@SafeVarargs
	public static <C extends Consumer<String>> C callProcess(Value<Process> processHolder, boolean throwExitValue,
			FilePath input, Map<String, String> env, List<String> command, FilePath dir, FilePath outputLog,
			FilePath errorLog, C consumer, Consumer<String>... consumers) throws UncheckedIOException {
		if (dir == null) {
			logger.debug("> {}", command.stream().filter(Objects::nonNull).collect(Collectors.joining(" ")));
		} else {
			logger.debug("{}> {}", dir.getAbsolutePath(),
					command.stream().filter(Objects::nonNull).collect(Collectors.joining(" ")));
		}
		ProcessBuilder builder = new ProcessBuilder(
				command.stream().filter(Objects::nonNull).collect(Collectors.toList()));
		builder.redirectErrorStream(true);
		if (env != null && env.size() > 0) {
			builder.environment().putAll(env);
		}
		if (dir != null) {
			if (dir.notExists()) {
				dir.createDirectory();
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
			if (processHolder != null)
				processHolder.set(process);
		} catch (IOException e1) {
			throw new UncheckedIOException(e1);
		}
		Consumer<String> allConsumers = null;
		if (consumer != null) {
			allConsumers = consumer;
			Arrays.stream(consumers).forEach(allConsumers::andThen);
			try (LineIterator lineIterator = new LineIterator(process.getInputStream())) {
				StreamSupport.stream(Spliterators.spliteratorUnknownSize(lineIterator, 0), false)
						.filter(StringUtils::isNotBlank).forEach(allConsumers);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new UncheckedIOException(e1);
			} catch (RuntimeException e1) {
				e1.printStackTrace();
				throw e1;
			} finally {
				try {
					process.destroy();
				} catch (Exception ex) {
					//
				}
			}
		}
		try {
			int exitValue = process.waitFor();
			// if (allConsumers != null)
			// allConsumers.accept("exit value=" + exitValue);
			if (exitValue != 0 && throwExitValue) {
				logger.debug("> {}", command.stream().collect(Collectors.joining(" ")));
				throw new ExitValueException(exitValue);
			}
		} catch (InterruptedException e) {
			//
		} finally {
			process.destroy();
		}
		return consumer;
	}

	public static class ExitValueException extends RuntimeException {
		private static final long serialVersionUID = 1501172862513358486L;

		private int exitValue;

		public ExitValueException(int exitValue) {
			super(String.valueOf(exitValue));
			this.exitValue = exitValue;
		}

		public int getExitValue() {
			return this.exitValue;
		}
	}

	public static List<String> split(String command) {
		List<String> list = new ArrayList<>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
		while (m.find())
			list.add(m.group(1).replace("\"", ""));
		return list;
	}
}
