package org.jhaws.common.io.media.ffmpeg;

import java.util.List;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxCfg;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxDefaultsCfg;
import org.junit.BeforeClass;
import org.junit.Test;

public class FfmpegToolTest {
    static FfmpegTool t;

    @BeforeClass
    public static void beforeClass() throws Exception {
        FilePath f = new FilePath(System.getenv("FFMPEG"));
        t = new FfmpegTool(f);
    }

    @Test
    public void test1() {
        try {
            System.out.println(t.getHwAccel());
            FilePath input = FilePath.createDefaultTempFile("mp4");
            input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("hevc.mp4"));
            FilePath output = FilePath.createDefaultTempFile("mp4");
            RemuxDefaultsCfg def = new RemuxDefaultsCfg();
            def.twopass = true;
            RemuxCfg cfg = t.remux(def, null, input, output, null);
            cfg.commands.forEach(System.out::println);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
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
            tmp.deleteAllIfExists();
            tmp.createDirectoryIfNotExists();
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
                    h.child("output").createDirectoryIfNotExists().child("test.mp4"), System.out::println);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
