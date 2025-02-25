package org.jhaws.common.io.media.jhead;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.Lines;
import org.jhaws.common.io.media.Tool;

// http://jpegclub.org/jpegtran/
public class JpegTran extends Tool {
	public static final String URL = "https://jpegclub.org/";

	public static final String EXE = "jpegtran";

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
		List<String> command = Arrays.asList(command(executable), "-debug", "-rotate", "" + nr, command(image),
				command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	public void flipLeftRight(FilePath image) {
		FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
		List<String> command = Arrays.asList(command(executable), "-debug", "-flip", "horizontal", command(image),
				command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	public void flipTopBottom(FilePath image) {
		FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
		List<String> command = Arrays.asList(command(executable), "-debug", "-flip", "vertical", command(image),
				command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	public void transpose(FilePath image) {
		FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
		List<String> command = Arrays.asList(command(executable), "-debug", "-transpose", command(image), command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	public void transverse(FilePath image) {
		FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
		List<String> command = Arrays.asList(command(executable), "-debug", "-transverse", command(image),
				command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	public void grayscale(FilePath image) {
		FilePath tmp = FilePath.getTempDirectory().child(image.getFileNameString());
		List<String> command = Arrays.asList(command(executable), "-debug", "-grayscale", command(image), command(tmp));
		System.out.println(command.stream().collect(Collectors.joining(" ")));
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
		image.delete();
		tmp.moveTo(image);
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = Arrays.asList(command(executable), "-verbose");
		Lines lines = new Lines();
		call(null, lines, executable.getParentPath(), command, true, null, false, null);
		return lines.lines().stream().filter(l -> l.contains("version")).map(l -> l.substring(l.indexOf("version")))
				.findAny().get();
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
					executable = f.child(EXE).appendExtension("exe");
				} else {
					executable = f.child(EXE);
				}
			}
		} else {
			f.createDirectory();
			if (Utils.osgroup == OSGroup.Windows) {
				executable = f.child(EXE).appendExtension("exe");
			} else {
				executable = f.child(EXE);
			}
		}

		if (executable.exists()) {
			//
		} else {
			String tmp = EXE;
			if (Utils.osgroup == OSGroup.Windows) {
				tmp = tmp + ".exe";
			}
			try (InputStream in = new URL(URL + tmp).openConnection().getInputStream();
					OutputStream out = executable.newBufferedOutputStream()) {
				IOUtils.copy(in, out);
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
}
