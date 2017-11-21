package org.jhaws.common.io.media.ffmpeg;

import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxCfg;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxDefaultsCfg;
import org.junit.BeforeClass;
import org.junit.Test;

public class FToolTest {
    static FfmpegTool t;

    @BeforeClass
    public static void beforeClass() throws Exception {
        FilePath f = new FilePath(System.getenv("FFMPEG"));
        t = new FfmpegTool(f.child("bin/ffmpeg.exe"), f.child("bin/ffprobe.exe"));
    }

    @Test
    public void test1() {
        try {
            System.out.println(t.getHwAccel());
            FilePath input = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".hevc.mp4");
            input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("hevc.mp4"));
            FilePath output = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".mp4");
            RemuxDefaultsCfg def = new RemuxDefaultsCfg();
            def.twopass = true;
            RemuxCfg cfg = t.remux(def, x -> System.out::println, input, output, null);
            cfg.commands.forEach(System.out::println);
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }

    @Test
    public void test2() {
        try {
            int repeat = 10;
            Integer fpsI = 30;
            int fpsO = 30;
            // runtime config VM arguments -Dsld=
            FilePath h = new FilePath(System.getProperty("sld"));
            FilePath sourcedir = h.child("sources");
            FilePath tmp = h.child("tmp");
            tmp.delete();
            tmp.createDirectory();
            List<FilePath> sources = sourcedir.list().stream().sorted().collect(Collectors.toList());
            int nr = sources.size();
            if (nr == 0) return;
            if (fpsI == null) fpsI = nr;
            int index = 0;
            int sindex = 0;
            for (int i = 0; i < nr * repeat; i++) {
                for (int j = 0; j < repeat; j++) {
                    String ii = "";
                    if (index < 1000) {
                        ii = "0" + ii;
                    }
                    if (index < 100) {
                        ii = "0" + ii;
                    }
                    if (index < 10) {
                        ii = "0" + ii;
                    }
                    ii = ii + index;
                    sources.get(sindex).copyTo(tmp.child("imgs_" + ii + ".png"));
                }
                index++;
                sindex++;
                if (sindex >= nr) {
                    sindex = 0;
                }
            }
            t.slideshow(null, fpsI, fpsO, new FilePath(tmp.getAbsolutePath()), "imgs_%04d.png",
                    h.child("output").createDirectory().child("test.mp4"), System.out::println);
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }

    @Test
    public void test3() {
        try {
            FilePath input = new FilePath(System.getProperty("test3"));
            FilePath outputa = input.appendExtension("a.mp4");
            outputa.delete();
            FilePath outputb = input.appendExtension("b.mp4");
            outputb.delete();
            RemuxDefaultsCfg def = new RemuxDefaultsCfg();
            RemuxCfg cfg = t.remux(def, x -> System.out::println, input, outputa, null);
            cfg.commands.forEach(System.out::println);
            def.twopass = true;
            cfg = t.remux(def, x -> System.out::println, input, outputb, null);
            cfg.commands.forEach(System.out::println);
            System.out.println();
            System.out.println(outputa.getAbsolutePath() + " > " + FilePath.getHumanReadableFileSize(input.getFileSize(), 2) + " > "
                    + FilePath.getHumanReadableFileSize(outputa.getFileSize(), 2));
            System.out.println(outputb.getAbsolutePath() + " > " + FilePath.getHumanReadableFileSize(input.getFileSize(), 2) + " > "
                    + FilePath.getHumanReadableFileSize(outputb.getFileSize(), 2));
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }
}