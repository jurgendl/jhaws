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

// http://www.sentex.net/~mwandel/jhead/
public class JHeadTool extends Tool {
	public static final String URL = "http://www.sentex.net/~mwandel/jhead/";

	public static final String EXE = "jhead";

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
		Processes.process(Processes.lines(command).dir(executable.getParentPath())).getConsumer().lines()
				.forEach(System.out::println);
	}

	@Override
	protected String getVersionImpl() {
		List<String> command = Arrays.asList(command(executable), "-V");
		Lines lines = new Lines();
		Tool.call(null, lines, executable.getParentPath(), command);
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
