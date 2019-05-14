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
import org.jhaws.common.io.media.Tool;

// http://jpegclub.org/jpegtran/
public class JpegTran extends Tool {

    public JpegTran() {
        super(System.getenv("JPEGTRAN"));
    }

    public JpegTran(String path) {
        super(path);
    }

    public JpegTran(boolean disableAuto) {
        super(disableAuto);
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

    @Override
    protected String getVersionImpl() {
        return executable.getShortFileName();
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
                    executable = f.child("jpegtran").appendExtension("exe");
                } else {
                    executable = f.child("jpegtran");
                }
            }
        } else {
            new IllegalArgumentException().printStackTrace();
            return;
        }
    }
}
