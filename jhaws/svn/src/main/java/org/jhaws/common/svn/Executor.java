package org.jhaws.common.svn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Executor {
	private static final Logger logger = LoggerFactory.getLogger(Executor.class);

	public static class MultiParseProcessOutput implements ParseProcessOutput {
		/** others */
		private final ParseProcessOutput[] others;

		public MultiParseProcessOutput(ParseProcessOutput... others) {
			this.others = others;
		}

		/**
		 * @see Deploy.ParseProcessOutput#process(java.lang.String)
		 */
		@Override
		public void process(String line) {
			for (ParseProcessOutput other : others) {
				other.process(line);
			}
		}
	}

	public static interface ParseProcessOutput {
		public void process(String line);
	}

	public static final ParseProcessOutput CONSOLE_PROCESSOR = line -> {
		if (line != null && line.trim().length() > 0) {
			System.out.println("out> " + line);
		}
	};

	public static final ParseProcessOutput LOGGING_PROCESSOR = line -> {
		if (line != null && line.trim().length() > 0) {
			logger.debug("out> " + line);
		}
	};

	public static class OutParseProcessOutput implements ParseProcessOutput {
		protected final OutputStream out;

		public OutParseProcessOutput(OutputStream out) {
			this.out = out;
		}

		@Override
		public void process(String line) {
			try {
				out.write(line.getBytes());
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
	}

	public static ProcessBuilder create(File dir, Map<String, String> env, String... cmd) {
		ProcessBuilder pb = new ProcessBuilder(cmd);
		if (dir != null) {
			pb.directory(dir);
		}
		if (env != null) {
			for (Map.Entry<String, String> entry : env.entrySet()) {
				pb.environment().put(entry.getKey(), entry.getValue());
			}
		}
		return pb;
	}

	public static int out(File dir, Map<String, String> env, File out, File err, String... cmd) throws IOException {
		return start(create(dir, env, cmd).redirectOutput(Redirect.to(out)).redirectError(Redirect.to(err)));
	}

	public static int parseProcessOutput(Process process, ParseProcessOutput processor) throws IOException {
		InputStreamReader tempReader = new InputStreamReader(new BufferedInputStream(process.getInputStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			processor.process(line);
		}
		process.destroy();
		return process.exitValue();
	}

	public static int print(File dir, Map<String, String> env, String... cmd) throws IOException {
		ProcessBuilder create = create(dir, env, cmd).redirectInput(Redirect.INHERIT).redirectOutput(Redirect.INHERIT).redirectError(Redirect.INHERIT);
		return start(create);
	}

	private static int start(ProcessBuilder pb) throws IOException {
		Process process = pb.start();
		int returnValue = -1;
		try {
			returnValue = process.waitFor();
		} catch (InterruptedException ex) {
			//
		}
		process.destroy();
		return returnValue;
	}
}
