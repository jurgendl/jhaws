package org.jhaws.common.io.media.jhead;

import static org.jhaws.common.io.console.Processes.callProcess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes.Lines;

// http://jpegclub.org/jpegtran/
public class JpegTran {
    protected FilePath executable;

    protected String command(FilePath f) {
        return "\"" + f.getAbsolutePath() + "\"";
    }

    public void rotate90(FilePath image) {
        rotate(image, 90);
    }

    public void rotate180(FilePath image) {
        rotate(image, 180);
    }

    public void rotate270(FilePath image) {
        rotate(image, 270);
    }

    public void rotate(FilePath image, int nr) {
        FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
        List<String> command = Arrays.asList(command(executable), "-debug", "-rotate", "" + nr, command(image), command(tmp));
        System.out.println(command.stream().collect(Collectors.joining(" ")));
        callProcess(null, false, command, executable.getParentPath(), new Lines()).lines().forEach(System.out::println);
        image.delete();
        tmp.moveTo(image);
    }

    public FilePath getExecutable() {
        return this.executable;
    }

    public void setExecutable(FilePath executable) {
        this.executable = executable;
    }
}
