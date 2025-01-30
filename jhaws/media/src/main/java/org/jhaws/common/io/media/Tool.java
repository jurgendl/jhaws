package org.jhaws.common.io.media;

import static org.jhaws.common.lang.CollectionUtils8.join;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.lang.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Tool {
	protected static final Logger LOGGER = LoggerFactory.getLogger("tool");

	protected Logger loggeri;

	protected FilePath executable;

	protected String path;

	protected String version;

	public Tool(String path) {
		setPath(path);
		loggeri = LoggerFactory.getLogger(getClass());
	}

	public Tool(boolean disableAuto) {
		loggeri = LoggerFactory.getLogger(getClass());
	}

	public final FilePath getExecutable() {
		return this.executable;
	}

	public final void setExecutable(FilePath executable) {
		this.executable = executable;
	}

	public final void setPath(String path) {
		if (new EqualsBuilder().append(path, this.path).isEquals())
			return;
		try {
			setPathImpl(path);
			this.path = path;
			this.version = null;
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		}
	}

	protected abstract void setPathImpl(String path);

	public final String getVersion() {
		if (version == null) {
			try {
				version = getVersionImpl();
			} catch (RuntimeException ex) {
				ex.printStackTrace();
			}

		}
		return version;
	}

	protected abstract String getVersionImpl();

	public static final String and(String... strings) {
		return Arrays.stream(strings).collect(Collectors.joining(","));
	}

	public static final String command(FilePath f) {
		return "\"" + f.getAbsolutePath() + "\"";
	}

	public static final String escape(String string) {
		return "\"" + string + "\"";
	}

	public static void call(FilePath command) {
		call(null, new org.jhaws.common.io.console.Processes.Log(), command.getParentPath(),
				Arrays.asList(command.getAbsolutePath()), true, null);
	}

	public static void call(FilePath dir, String command) {
		call(null, new org.jhaws.common.io.console.Processes.Log(), dir, Arrays.asList(command), true, null);
	}

	public static void call(FilePath dir, List<String> command) {
		call(null, new org.jhaws.common.io.console.Processes.Log(), dir, command, true, null);
	}

	public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir,
			List<String> command) {
		return call(processHolder, lines, dir, command, true, null);
	}

	public static <C extends Consumer<String>> C silentcall(Value<Process> processHolder, C lines, FilePath dir,
			List<String> command) {
		return call(processHolder, lines, dir, command, false, null);
	}

	public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir,
			List<String> command, boolean log, Consumer<String> listener) {
		return call(processHolder, lines, dir, command, log, listener, true, null, null);
	}

	public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir,
			List<String> command, boolean log, Consumer<String> listener, boolean throwExitValue,
			List<FilePath> paths) {
		return call(processHolder, lines, dir, command, log, listener, throwExitValue, paths,
				(Map<String, String>) null);
	}

	@SuppressWarnings("unchecked")
	public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir,
			List<String> command, boolean log, Consumer<String> listener, boolean throwExitValue, List<FilePath> paths,
			Map<String, String> env) {
		if (lines == null)
			lines = (C) new Lines();
		Consumer<String> consumers = log ? lines.andThen(new Lines()) : lines;
		if (listener != null) {
			consumers = consumers.andThen(listener);
		}
		if (log) {
			LOGGER.info("start - {}", join(command));
		}
		long start = System.currentTimeMillis();
		Map<String, String> env0 = env == null ? new HashMap<>() : env;
		if (paths != null)
			env0.put("Path", paths.stream().map(FilePath::getAbsolutePath).collect(Collectors.joining(";")));
		Processes.process(Processes.config(command).processHolder(processHolder).throwExitValue(throwExitValue)
				.env(env0).dir(dir).consumer(consumers));
		if (log) {
			LOGGER.info("end - {}s :: {}", (System.currentTimeMillis() - start) / 1000, join(command));
		}
		return lines;
	}

	public static <C extends Consumer<String>> C call(Duration timeout, Value<Process> processHolder, C lines,
			FilePath dir, List<String> command, boolean log, Consumer<String> listener, boolean throwExitValue,
			List<FilePath> paths) {
		return call(timeout, processHolder, lines, dir, command, log, listener, throwExitValue, paths,
				(Map<String, String>) null);
	}

	public static <C extends Consumer<String>> C call(Duration timeout, Value<Process> processHolder, C lines,
			FilePath dir, List<String> command, boolean log, Consumer<String> listener, boolean throwExitValue,
			List<FilePath> paths, Map<String, String> env) {
		// https://stackoverflow.com/questions/2275443/how-to-timeout-a-thread
		ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		});
		Future<C> future = executor.submit(new Callable<>() {
			@Override
			public C call() throws Exception {
				return Tool.call(processHolder, lines, dir, command, log, listener, throwExitValue, paths, env);
			}
		});
		try {
			LOGGER.info("Started ...");
			C get = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
			LOGGER.info("{}", get);
			LOGGER.info("Finished!");
			return get;
		} catch (TimeoutException ex) {
			future.cancel(true);
			LOGGER.error("{}", ex);
			throw new RuntimeException(ex);
		} catch (InterruptedException ex) {
			LOGGER.error("{}", ex);
			throw new RuntimeException(ex);
		} catch (ExecutionException ex) {
			LOGGER.error("{}", ex);
			throw new RuntimeException(ex);
		} finally {
			try {
				executor.shutdownNow();
			} catch (RuntimeException ex) {
				//
			}
		}
	}
}
