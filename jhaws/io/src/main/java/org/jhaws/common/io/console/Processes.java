package org.jhaws.common.io.console;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

	public static class NoOp implements Consumer<String> {
		@Override
		public void accept(String t) {
			//
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

	public static <C extends Consumer<String>> ProcessConfigBuilder<C> config(List<String> command, C consumer) {
		return new ProcessConfigBuilder<>(command, consumer);
	}

	public static DefaultProcessConfigBuilder config(List<String> command) {
		return new DefaultProcessConfigBuilder(command);
	}

	public static ProcessConfigBuilder<Lines> lines(List<String> command) {
		return config(command, new Lines());
	}

	public static ProcessConfigBuilder<Log> log(List<String> command) {
		return config(command, new Log());
	}

	public static ProcessConfigBuilder<Out> out(List<String> command) {
		return config(command, new Out());
	}

	public static ProcessConfigBuilder<Text> text(List<String> command) {
		return config(command, new Text());
	}

	public static class DefaultProcessConfigBuilder extends ProcessConfigBuilder<Consumer<String>> {
		public DefaultProcessConfigBuilder(List<String> command) {
			super(command, null);
		}
	}

	public static class ProcessConfigBuilder<C extends Consumer<String>> {
		Value<Process> processHolder;
		boolean throwExitValue;
		FilePath input;
		Map<String, String> env;
		List<String> command;
		FilePath dir;
		FilePath outputLog;
		FilePath errorLog;
		C consumer;
		Collection<Consumer<String>> consumers;

		public ProcessConfigBuilder(List<String> command, C consumer) {
			this.command = command;
			this.consumer = consumer;
		}

		public ProcessConfigBuilder<C> processHolder(Value<Process> processHolder) {
			this.processHolder = processHolder;
			return this;
		}

		public ProcessConfigBuilder<C> throwExitValue(boolean throwExitValue) {
			this.throwExitValue = throwExitValue;
			return this;
		}

		public ProcessConfigBuilder<C> input(FilePath input) {
			this.input = input;
			return this;
		}

		public ProcessConfigBuilder<C> env(Map<String, String> env) {
			this.env = env;
			return this;
		}

		public ProcessConfigBuilder<C> command(List<String> command) {
			this.command = command;
			return this;
		}

		public ProcessConfigBuilder<C> dir(FilePath dir) {
			this.dir = dir;
			return this;
		}

		public ProcessConfigBuilder<C> outputLog(FilePath outputLog) {
			this.outputLog = outputLog;
			return this;
		}

		public ProcessConfigBuilder<C> errorLog(FilePath errorLog) {
			this.errorLog = errorLog;
			return this;
		}

		public ProcessConfigBuilder<C> consumer(C consumer) {
			this.consumer = consumer;
			return this;
		}

		public ProcessConfigBuilder<C> consumers(Collection<Consumer<String>> consumers) {
			this.consumers = consumers;
			return this;
		}

		public Value<Process> getProcessHolder() {
			return this.processHolder;
		}

		public void setProcessHolder(Value<Process> processHolder) {
			this.processHolder = processHolder;
		}

		public boolean isThrowExitValue() {
			return this.throwExitValue;
		}

		public void setThrowExitValue(boolean throwExitValue) {
			this.throwExitValue = throwExitValue;
		}

		public FilePath getInput() {
			return this.input;
		}

		public void setInput(FilePath input) {
			this.input = input;
		}

		public Map<String, String> getEnv() {
			return this.env;
		}

		public void setEnv(Map<String, String> env) {
			this.env = env;
		}

		public List<String> getCommand() {
			return this.command;
		}

		public void setCommand(List<String> command) {
			this.command = command;
		}

		public FilePath getDir() {
			return this.dir;
		}

		public void setDir(FilePath dir) {
			this.dir = dir;
		}

		public FilePath getOutputLog() {
			return this.outputLog;
		}

		public void setOutputLog(FilePath outputLog) {
			this.outputLog = outputLog;
		}

		public FilePath getErrorLog() {
			return this.errorLog;
		}

		public void setErrorLog(FilePath errorLog) {
			this.errorLog = errorLog;
		}

		public C getConsumer() {
			return this.consumer;
		}

		public void setConsumer(C consumer) {
			this.consumer = consumer;
		}

		public Collection<Consumer<String>> getConsumers() {
			return this.consumers;
		}

		public void setConsumers(Collection<Consumer<String>> consumers) {
			this.consumers = consumers;
		}
	}

	public static <C extends Consumer<String>> ProcessConfigBuilder<C> process(
			ProcessConfigBuilder<C> processConfigBuilder) throws UncheckedIOException {
		if (processConfigBuilder.dir == null) {
			logger.debug("> {}",
					processConfigBuilder.command.stream().filter(Objects::nonNull).collect(Collectors.joining(" ")));
		} else {
			logger.debug("{}> {}", processConfigBuilder.dir.getAbsolutePath(),
					processConfigBuilder.command.stream().filter(Objects::nonNull).collect(Collectors.joining(" ")));
		}

		ProcessBuilder builder = new ProcessBuilder(
				processConfigBuilder.command.stream().filter(Objects::nonNull).collect(Collectors.toList()));

		builder.redirectErrorStream(true);

		if (processConfigBuilder.env != null && processConfigBuilder.env.size() > 0) {
			builder.environment().putAll(processConfigBuilder.env);
		}

		if (processConfigBuilder.dir != null) {
			if (processConfigBuilder.dir.notExists()) {
				processConfigBuilder.dir.createDirectory();
			} else {
				if (!processConfigBuilder.dir.isDirectory()) {
					throw new UncheckedIOException(new IOException(processConfigBuilder.dir + " is not a directory"));
				}
			}
			builder.directory(processConfigBuilder.dir.toFile());
		}

		if (processConfigBuilder.input != null)
			builder.redirectInput(Redirect.from(processConfigBuilder.input.toFile()));
		else {
			// builder.redirectInput(Redirect.INHERIT);
		}

		if (processConfigBuilder.outputLog != null)
			if (processConfigBuilder.outputLog.exists())
				builder.redirectOutput(Redirect.appendTo(processConfigBuilder.outputLog.toFile()));
			else
				builder.redirectOutput(Redirect.to(processConfigBuilder.outputLog.toFile()));
		else {
			// builder.redirectOutput(Redirect.INHERIT);
		}

		if (processConfigBuilder.errorLog != null)
			if (processConfigBuilder.errorLog.exists())
				builder.redirectError(Redirect.appendTo(processConfigBuilder.errorLog.toFile()));
			else
				builder.redirectError(Redirect.to(processConfigBuilder.errorLog.toFile()));
		else {
			builder.redirectError(Redirect.PIPE);
		}

		Process process;
		try {
			process = builder.start();
			if (processConfigBuilder.processHolder != null)
				processConfigBuilder.processHolder.set(process);
		} catch (IOException e1) {
			throw new UncheckedIOException(e1);
		}

		Consumer<String> allConsumers = null;
		if (processConfigBuilder.consumer != null)
			allConsumers = processConfigBuilder.consumer;
		if (processConfigBuilder.consumers != null && !processConfigBuilder.consumers.isEmpty()) {
			Iterator<Consumer<String>> iterator = processConfigBuilder.consumers.iterator();
			if (allConsumers == null)
				allConsumers = iterator.next();
			while (iterator.hasNext())
				allConsumers.andThen(iterator.next());
		}
		if (allConsumers == null)
			allConsumers = new NoOp();

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

		try {
			int exitValue = process.waitFor();
			if (exitValue != 0 && processConfigBuilder.throwExitValue) {
				logger.debug("> {}", processConfigBuilder.command.stream().collect(Collectors.joining(" ")));
				throw new ExitValueException(exitValue);
			}
		} catch (InterruptedException e) {
			//
		} finally {
			try {
				process.destroy();
			} catch (Exception ex) {
				//
			}
		}

		return processConfigBuilder;
	}

	@SuppressWarnings("serial")
	public static class ExitValueException extends RuntimeException {
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
