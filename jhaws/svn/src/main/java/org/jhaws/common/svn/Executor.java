package org.jhaws.common.svn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;

public class Executor {
    public static class MultiParseProcessOutput implements Executor.ParseProcessOutput {
        /** others */
        private final Executor.ParseProcessOutput[] others;

        public MultiParseProcessOutput(Executor.ParseProcessOutput... others) {
            this.others = others;
        }

        /**
         * @see Deploy.ParseProcessOutput#process(java.lang.String)
         */
        @Override
        public void process(String line) {
            for (Executor.ParseProcessOutput other : others) {
                other.process(line);
            }
        }
    }

    public static interface ParseProcessOutput {
        public void process(String line);
    }

    /** SYSOUT_PROCESSOR */
    public static final Executor.ParseProcessOutput SYSOUT_PROCESSOR = line -> {
        if (line != null && line.trim().length() > 0) {
            // FIXME SystemLog.log("out> " + line);
        }
    };

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

    public static void out(File dir, Map<String, String> env, File out, File err, String... cmd) throws IOException {
        Executor.start(Executor.create(dir, env, cmd).redirectOutput(Redirect.to(out)).redirectError(Redirect.to(err)));
    }

    public static void parseProcessOutput(Process process, ParseProcessOutput processor) throws IOException {
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
    }

    public static void print(File dir, Map<String, String> env, String... cmd) throws IOException {
        ProcessBuilder create = Executor.create(dir, env, cmd)
                .redirectInput(Redirect.INHERIT)
                .redirectOutput(Redirect.INHERIT)
                .redirectError(Redirect.INHERIT);
        Executor.start(create);
    }

    private static void start(ProcessBuilder pb) throws IOException {
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            //
        }
        process.destroy();
    }
}
