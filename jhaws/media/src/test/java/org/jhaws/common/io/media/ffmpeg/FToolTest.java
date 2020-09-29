package org.jhaws.common.io.media.ffmpeg;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxCfg;
import org.jhaws.common.io.media.ffmpeg.FfmpegTool.RemuxDefaultsCfg;
import org.jhaws.common.net.client.GetRequest;
import org.jhaws.common.net.client.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FToolTest {
	static FfmpegTool t;

	@BeforeAll
	public static void beforeClass() throws Exception {
		t = new FfmpegTool();
	}

	@Test
	public void test_getContainersDeMuxing() {
		t.getContainersDeMuxing().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void test_getContainersMuxing() {
		t.getContainersMuxing().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void test_getVideoEncoders() {
		t.getVideoEncoders().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void test_getAudioEncoders() {
		t.getAudioEncoders().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void test_getVideoDecoders() {
		t.getVideoDecoders().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void test_getAudioDecoders() {
		t.getAudioDecoders().entrySet().stream().forEach(System.out::println);
	}

	@Test
	public void testH265ToH264() {
		try {
			System.out.println(t.getHwAccel());
			FilePath input = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".Tears_400_x265.mp4");
			input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("Tears_400_x265.mp4"));
			FilePath output = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".Tears_400_x264.mp4");
			RemuxCfg cfg = t.remux(null, null, x -> System.out::println, input, output, c -> {
				c.vcopy = false;
				c.hq = true;
			});
			cfg.commands.forEach(System.out::println);
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	@Test
	public void testSmallFileToHq() {
		try {
			FilePath input = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".file_example_MP4_480_1_5MG.mp4");
			input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("file_example_MP4_480_1_5MG.mp4"));
			FilePath output = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".file_example_MP4_480_1_5MG.mp4.mp4");
			RemuxCfg cfg = t.remux(null, null, x -> System.out::println, input, output, c -> {
				c.vcopy = false;
				c.hq = true;
			});
			cfg.commands.forEach(System.out::println);
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	// @Test
	// public void test2() {
	// try {
	// int repeat = 10;
	// Integer fpsI = 30;
	// int fpsO = 30;
	// // runtime config VM arguments -Dsld=
	// FilePath h = new FilePath(System.getProperty("sld"));
	// FilePath sourcedir = h.child("sources");
	// FilePath tmp = h.child("tmp");
	// tmp.delete();
	// tmp.createDirectory();
	// List<FilePath> sources = sourcedir.list().stream().sorted().collect(Collectors.toList());
	// int nr = sources.size();
	// if (nr == 0)
	// return;
	// if (fpsI == null)
	// fpsI = nr;
	// int index = 0;
	// int sindex = 0;
	// for (int i = 0; i < nr * repeat; i++) {
	// for (int j = 0; j < repeat; j++) {
	// String ii = "";
	// if (index < 1000) {
	// ii = "0" + ii;
	// }
	// if (index < 100) {
	// ii = "0" + ii;
	// }
	// if (index < 10) {
	// ii = "0" + ii;
	// }
	// ii = ii + index;
	// sources.get(sindex).copyTo(tmp.child("imgs_" + ii + ".png"));
	// }
	// index++;
	// sindex++;
	// if (sindex >= nr) {
	// sindex = 0;
	// }
	// }
	// t.slideshow(null, fpsI, fpsO, new FilePath(tmp.getAbsolutePath()), "imgs_%04d.png",
	// h.child("output").createDirectory().child("test.mp4"), System.out::println);
	// } catch (RuntimeException ex) {
	// ex.printStackTrace(System.out);
	// throw ex;
	// }
	// }

	// @Test
	// public void test3() {
	// try {
	// FilePath input = new FilePath(System.getProperty("test3"));
	// FilePath outputa = input.appendExtension("a.mp4");
	// outputa.delete();
	// FilePath outputb = input.appendExtension("b.mp4");
	// outputb.delete();
	// RemuxDefaultsCfg def = new RemuxDefaultsCfg();
	// RemuxCfg cfg = t.remux(null, def, x -> System.out::println, input, outputa, null);
	// cfg.commands.forEach(System.out::println);
	// def.twopass = true;
	// cfg = t.remux(null, def, x -> System.out::println, input, outputb, null);
	// cfg.commands.forEach(System.out::println);
	// System.out.println();
	// System.out.println(
	// outputa.getAbsolutePath() + " > " + FilePath.getHumanReadableFileSize(input.getFileSize(), 2)
	// + " > " + FilePath.getHumanReadableFileSize(outputa.getFileSize(), 2));
	// System.out.println(
	// outputb.getAbsolutePath() + " > " + FilePath.getHumanReadableFileSize(input.getFileSize(), 2)
	// + " > " + FilePath.getHumanReadableFileSize(outputb.getFileSize(), 2));
	// } catch (RuntimeException ex) {
	// ex.printStackTrace(System.out);
	// throw ex;
	// }
	// }

	// @Test
	// public void test4() {
	// try {
	// FilePath sourcedir = new FilePath("c:/tmp").child("loop").delete().createDirectory();
	// {
	// BufferedImage bi = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
	// Graphics2D g = (Graphics2D) bi.getGraphics();
	// g.setColor(Color.white);
	// int x = 8;
	// for (int i = 1; i <= 60; i++) {
	// g.fillRect((i + x) * x, (i + x) * x, x, x);
	// String other = (i < 10 ? "0" : "") + i + ".png";
	// System.out.println(other);
	// ImageIO.write(bi, "png", sourcedir.child(other).newBufferedOutputStream());
	// }
	// }
	// FilePath tmp = sourcedir.child("tmp");
	// FilePath single = tmp.child("test.mp4");
	// {
	// int repeat = 1;
	// Integer fpsI = 30;
	// int fpsO = 30;
	// tmp.delete();
	// tmp.createDirectory();
	// List<FilePath> sources = sourcedir.list().stream().filter(FilePath::isFile).sorted()
	// .collect(Collectors.toList());
	// IntegerValue idx = new IntegerValue(1);
	// for (int i = 0; i < repeat; i++) {
	// sources.forEach(fp -> {
	// int index = idx.get();
	// String ii = "";
	// if (index < 1000) {
	// ii = "0" + ii;
	// }
	// if (index < 100) {
	// ii = "0" + ii;
	// }
	// if (index < 10) {
	// ii = "0" + ii;
	// }
	// ii = ii + index;
	// FilePath cccc = tmp.child("imgs_" + ii + ".png");
	// System.out.println(fp);
	// System.out.println(cccc);
	// fp.copyTo(cccc);
	// idx.add();
	// });
	// }
	// t.slideshow(null, fpsI, fpsO, tmp, "imgs_%04d.png", single, System.out::println);
	// }
	// {
	// t.loop(single, 10, single.appendExtension(".multiple.mp4"), System.out::println);
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace(System.out);
	// }
	// }

	@Test
	public void testHwAccel() {
		try {
			System.out.println(t.getHwAccel());
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	@Test
	public void testToH264Hi10p() {
		FilePath target = FilePath.getTempDirectory().child("file_example_MP4_480_1_5MG.mp4");
		if (target.notExists()) {
			FilePath source = new FilePath(getClass(), "file_example_MP4_480_1_5MG.mp4");
			source.copyTo(target);
		}
		FilePath test = target.appendExtension("hi10p.mp4");
		test.delete();
		t.remux(null, null, cfg -> System.out::println, target, test, cfg -> {
			cfg.vcopy = false;
			cfg.hi10p = true;
			cfg.hq = true;
		});
	}

	@Test
	public void testTwoPass() {
		try {
			System.out.println(t.getHwAccel());
			FilePath input = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".Tears_400_x265.mp4");
			input.write(FfmpegTool.class.getClassLoader().getResourceAsStream("Tears_400_x265.mp4"));
			FilePath output = FilePath.getTempDirectory().child(System.currentTimeMillis() + ".Tears_400_x264.mp4");
			RemuxDefaultsCfg def = new RemuxDefaultsCfg();
			def.twopass = true;
			RemuxCfg cfg = t.remux(null, def, x -> System.out::println, input, output, c -> {
				c.vcopy = false;
				c.hq = true;
			});
			cfg.commands.forEach(System.out::println);
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	@Test
	public void testLargeFileToHq() {
		FilePath target = FilePath.getTempDirectory().child("file_example_MP4_1920_18MG.mp4");
		if (target.notExists()) {
			GetRequest get = new GetRequest("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1920_18MG.mp4");
			get.setOut(target.newOutputStream());
			try (org.jhaws.common.net.client.HTTPClient hc = new org.jhaws.common.net.client.HTTPClient()) {
				Response response = hc.get(get);
				if (response.getStatusCode() != 200) {
					throw new IllegalArgumentException();
				}
				if (!response.getContentType().startsWith("video/")) {
					throw new IllegalArgumentException();
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.out);
			}
		}
		all(target);
	}

	@Test
	public void testHevc10ToAll() {
		FilePath target = FilePath.getTempDirectory().child("jellyfish-3-mbps-hd-hevc-10bit.mkv");
		if (target.notExists()) {
			GetRequest get = new GetRequest("http://jell.yfish.us/media/jellyfish-3-mbps-hd-hevc-10bit.mkv");
			get.setOut(target.newOutputStream());
			try (org.jhaws.common.net.client.HTTPClient hc = new org.jhaws.common.net.client.HTTPClient()) {
				hc.get(get);
			} catch (Exception ex) {
				ex.printStackTrace(System.out);
			}
		}
		all(target);
	}

	@Test
	public void testHevcToAll() {
		FilePath target = FilePath.getTempDirectory().child("jellyfish-3-mbps-hd-hevc.mkv");
		if (target.notExists()) {
			GetRequest get = new GetRequest("http://jell.yfish.us/media/jellyfish-3-mbps-hd-hevc.mkv");
			get.setOut(target.newOutputStream());
			try (org.jhaws.common.net.client.HTTPClient hc = new org.jhaws.common.net.client.HTTPClient()) {
				hc.get(get);
			} catch (Exception ex) {
				ex.printStackTrace(System.out);
			}
		}
		all(target);
	}

	@Test
	public void testH264ToAll() {
		FilePath target = FilePath.getTempDirectory().child("jellyfish-3-mbps-hd-h264.mkv");
		if (target.notExists()) {
			GetRequest get = new GetRequest("http://jell.yfish.us/media/jellyfish-3-mbps-hd-h264.mkv");
			get.setOut(target.newOutputStream());
			try (org.jhaws.common.net.client.HTTPClient hc = new org.jhaws.common.net.client.HTTPClient()) {
				hc.get(get);
			} catch (Exception ex) {
				ex.printStackTrace(System.out);
			}
		}
		all(target);
	}

	@Test
	public void testSmallFileToAll() {
		try {
			FilePath target = FilePath.getTempDirectory().child("file_example_MP4_480_1_5MG.mp4");
			if (target.notExists())
				target.write(FfmpegTool.class.getClassLoader().getResourceAsStream("file_example_MP4_480_1_5MG.mp4"));
			all(target);
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	private void all(FilePath target) {
		{
			FilePath test = target.appendExtension("mp4");
			// test.delete();
			if (test.notExists())
				t.remux(null, null, cfg -> System.out::println, target, test, cfg -> {
					cfg.vcopy = false;
					cfg.hq = true;
				});
		}
		{
			FilePath test = target.appendExtension("hi10p.mp4");
			// test.delete();
			if (test.notExists())
				t.remux(null, null, cfg -> System.out::println, target, test, cfg -> {
					cfg.vcopy = false;
					cfg.hi10p = true;
					cfg.hq = true;
				});
		}
		{
			FilePath test = target.appendExtension("hevc.mp4");
			// test.delete();
			if (test.notExists())
				t.remux(null, null, cfg -> System.out::println, target, test, cfg -> {
					cfg.vcopy = false;
					cfg.hevc = true;
					cfg.hq = true;
				});
		}
		{
			FilePath test = target.appendExtension("hevc.hi10p.mp4");
			// test.delete();
			if (test.notExists())
				t.remux(null, null, cfg -> System.out::println, target, test, cfg -> {
					cfg.vcopy = false;
					cfg.hevc = true;
					cfg.hi10p = true;
					cfg.hq = true;
				});
		}
	}
}