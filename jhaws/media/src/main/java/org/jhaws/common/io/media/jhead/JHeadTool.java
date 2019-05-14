package org.jhaws.common.io.media.jhead;

import static org.jhaws.common.io.console.Processes.callProcess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.console.Processes.LinesLog;
import org.jhaws.common.io.media.Tool;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool;

// http://www.sentex.net/~mwandel/jhead/
public class JHeadTool extends Tool {
    public JHeadTool() {
        super(System.getenv("JHEAD"));
    }

    public JHeadTool(String path) {
        super(path);
    }

    public JHeadTool(boolean disableAuto) {
        super(disableAuto);
    }

    public void fix(FilePath image) {
        List<String> command = Arrays.asList(command(executable), "-autorot", "-v", command(image));
        System.out.println(command.stream().collect(Collectors.joining(" ")));
        callProcess(null, false, command, executable.getParentPath(), new Lines()).lines().forEach(System.out::println);
    }

    @Override
    protected String getVersionImpl() {
        List<String> command = Arrays.asList(command(executable), "-V");
        LinesLog lines = new LinesLog();
        FfmpegTool.call(null, lines, executable.getParentPath(), command);
        return lines.lines().get(0);
    }

    @Override
    protected void setPathImpl(String path) {
        if (StringUtils.isBlank(path)) {
            new NullPointerException().printStackTrace();
            return;
        }
        FilePath f = new FilePath(path);
        if (f.exists()) {
            if (f.isFile()) {
                executable = f;
            } else {
                if (Utils.osgroup == OSGroup.Windows) {
                    executable = f.child("jhead").appendExtension("exe");
                } else {
                    executable = f.child("jhead");
                }
            }
        } else {
            new IllegalArgumentException().printStackTrace();
            return;
        }
    }
}
