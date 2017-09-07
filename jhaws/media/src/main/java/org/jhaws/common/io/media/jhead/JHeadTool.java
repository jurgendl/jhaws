package org.jhaws.common.io.media.jhead;

import static org.jhaws.common.io.console.Processes.callProcess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;

// http://www.sentex.net/~mwandel/jhead/
public class JHeadTool {
    protected FilePath jhead;

    public FilePath getJhead() {
        return this.jhead;
    }

    public void setJhead(FilePath jhead) {
        this.jhead = jhead;
    }

    protected String command(FilePath f) {
        return "\"" + f.getAbsolutePath() + "\"";
    }

    public void fix(FilePath image) {
        List<String> command = Arrays.asList(command(jhead), "-autorot", "-v", image.getAbsolutePath());
        System.out.println(command.stream().collect(Collectors.joining(" ")));
        callProcess(false, command, jhead.getParentPath(), new Lines()).lines().forEach(System.out::println);
    }
}
