package org.jhaws.common.io.media;

import static org.jhaws.common.lang.CollectionUtils8.join;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    protected static final Logger logger = LoggerFactory.getLogger("tool");

    protected FilePath executable;

    protected String path;

    protected String version;

    public Tool(String path) {
        setPath(path);
    }

    public Tool(boolean disableAuto) {
        //
    }

    public final FilePath getExecutable() {
        return this.executable;
    }

    public final void setExecutable(FilePath executable) {
        this.executable = executable;
    }

    public final void setPath(String path) {
        if (new EqualsBuilder().append(path, this.path).isEquals()) return;
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

    public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir, List<String> command) {
        return call(processHolder, lines, dir, command, true, null);
    }

    public static <C extends Consumer<String>> C silentcall(Value<Process> processHolder, C lines, FilePath dir, List<String> command) {
        return call(processHolder, lines, dir, command, false, null);
    }

    public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir, List<String> command, boolean log, Consumer<String> listener) {
        return call(processHolder, lines, dir, command, log, listener, true, null);
    }

    public static <C extends Consumer<String>> C call(Value<Process> processHolder, C lines, FilePath dir, List<String> command, boolean log, Consumer<String> listener, boolean throwExitValue, List<FilePath> paths) {
        if (lines == null) lines = (C) new Lines();
        Consumer<String> consumers = log ? lines.andThen(new Lines()) : lines;
        if (listener != null) {
            consumers = consumers.andThen(listener);
        }
        if (log) {
            logger.info("start - {}", join(command));
        }
        long start = System.currentTimeMillis();
        HashMap<String, String> env = new HashMap<>();
        if (paths != null) env.put("Path", paths.stream().map(FilePath::getAbsolutePath).collect(Collectors.joining(";")));
        Processes.callProcess(processHolder, throwExitValue, null, env, command, dir, null, null, consumers, consumers);
        if (log) {
            logger.info("end - {}s :: {}", (System.currentTimeMillis() - start) / 1000, join(command));
        }
        return lines;
    }
}
