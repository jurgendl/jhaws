package org.jhaws.common.io.media.ffmpeg;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxCfg;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxDefaultsCfg;
import org.junit.Test;

public class FfmpegToolTest {
    @Test
    public void test1() {
        try {
            FilePath f = new FilePath(System.getenv("FFMPEG"));
            if (f.isDirectory()) f = f.child("ffmpeg.exe");
            FfmpegTool t = new FfmpegTool(f);
            System.out.println(t.getHwAccel());
            FilePath input = FilePath.createDefaultTempFile("mp4");
            input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("hevc.mp4"));
            FilePath output = FilePath.createDefaultTempFile("mp4");
            RemuxDefaultsCfg def = new RemuxDefaultsCfg();
            def.setTwopass(true);
            RemuxCfg cfg = t.remux(def, null, input, output, null);
            cfg.commands.forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
