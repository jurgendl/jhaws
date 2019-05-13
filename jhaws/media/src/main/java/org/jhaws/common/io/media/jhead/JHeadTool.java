package org.jhaws.common.io.media.jhead;

import static org.jhaws.common.io.console.Processes.callProcess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;

// http://www.sentex.net/~mwandel/jhead/
public class JHeadTool {
    protected FilePath executable;

    protected String command(FilePath f) {
        return "\"" + f.getAbsolutePath() + "\"";
    }

    public void fix(FilePath image) {
        List<String> command = Arrays.asList(command(executable), "-autorot", "-v", command(image));
        System.out.println(command.stream().collect(Collectors.joining(" ")));
        callProcess(null, false, command, executable.getParentPath(), new Lines()).lines().forEach(System.out::println);
    }

    public FilePath getExecutable() {
        return this.executable;
    }

    public void setExecutable(FilePath executable) {
        this.executable = executable;
    }
}
