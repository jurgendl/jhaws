package org.jhaws.common.io.sevenzip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.console.Processes.ExitValueException;
import org.jhaws.common.io.console.Processes.Log;

//
// Usage: 7za <command> [<switches>...] <archive_name> [<file_names>...]
// [<@listfiles...>]
//
// <Commands>
// a : Add files to archive
// b : Benchmark
// d : Delete files from archive
// e : Extract files from archive (without using directory names)
// h : Calculate hash values for files
// i : Show information about supported formats
// l : List contents of archive
// rn : Rename files in archive
// t : Test integrity of archive
// u : Update files to archive
// x : eXtract files with full paths
//
// <Switches>
// -- : Stop switches parsing
// -ai[r[-|0]]{@listfile|!wildcard} : Include archives
// -ax[r[-|0]]{@listfile|!wildcard} : eXclude archives
// -ao{a|s|t|u} : set Overwrite mode
// -an : disable archive_name field
// -bb[0-3] : set output log level
// -bd : disable progress indicator
// -bs{o|e|p}{0|1|2} : set output stream for output/error/progress line
// -bt : show execution time statistics
// -i[r[-|0]]{@listfile|!wildcard} : Include filenames
// -m{Parameters} : set compression Method
// -mmt[N] : set number of CPU threads
// -o{Directory} : set Output directory
// -p{Password} : set Password
// -r[-|0] : Recurse subdirectories
// -sa{a|e|s} : set Archive name mode
// -scc{UTF-8|WIN|DOS} : set charset for for console input/output
// -scs{UTF-8|UTF-16LE|UTF-16BE|WIN|DOS|{id}} : set charset for list files
// -scrc[CRC32|CRC64|SHA1|SHA256|*] : set hash function for x, e, h commands
// -sdel : delete files after compression
// -seml[.] : send archive by email
// -sfx[{name}] : Create SFX archive
// -si[{name}] : read data from stdin
// -slp : set Large Pages mode
// -slt : show technical information for l (List) command
// -snh : store hard links as links
// -snl : store symbolic links as links
// -sni : store NT security information
// -sns[-] : store NTFS alternate streams
// -so : write data to stdout
// -spd : disable wildcard matching for file names
// -spe : eliminate duplication of root folder for extract command
// -spf : use fully qualified file paths
// -ssc[-] : set sensitive case mode
// -ssw : compress shared files
// -stl : set archive timestamp from the most recently modified file
// -stm{HexMask} : set CPU thread affinity mask (hexadecimal number)
// -stx{Type} : exclude archive type
// -t{Type} : Set type of archive
// -u[-][p#][q#][r#][x#][y#][z#][!newArchiveName] : Update options
// -v{Size}[b|k|m|g] : Create volumes
// -w[{path}] : assign Work directory. Empty path means a temporary directory
// -x[r[-|0]]{@listfile|!wildcard} : eXclude filenames
// -y : assume Yes on all queries
//
/** https://sevenzip.osdn.jp/chm/cmdline/ */
public class SevenZip {
	public static class ListFiles implements Consumer<String>, Iterable<String> {
		final List<String> contents = new ArrayList<>();

		int idx = 0;

		@Override
		public void accept(String _line) {
			String prefixnon = "   Date      Time    Attr         Size   Compressed  ";
			String prefix = prefixnon + "Name";
			if (_line.startsWith(prefix)) {
				idx++;
				return;
			}
			if (idx == 1 && _line.startsWith("--")) {
				idx++;
				return;
			}
			if (idx == 2 && _line.startsWith("--")) {
				idx++;
				return;
			}
			if (idx == 2) {
				// String[] parts = line.split("\\s+"); // if
				// (!parts[2].startsWith("D")) {
				String filename = _line.substring(prefixnon.length());
				contents.add(filename);
			}
		}

		@Override
		public Iterator<String> iterator() {
			return contents.iterator();
		}

		public List<String> getContents() {
			return this.contents;
		}
	}

	private FilePath executable;

	public SevenZip(FilePath executable) {
		this.executable = executable;
	}

	public FilePath getExecutable() {
		return this.executable;
	}

	public void setExecutable(FilePath executable) {
		this.executable = executable;
	}

	public void compress(FilePath archive, String password, String... sources) {
		List<String> cmd = new ArrayList<>();
		cmd.add(executable.getAbsolutePath());
		cmd.add("a");
		cmd.add("-bd");
		cmd.add("-r");
		cmd.add("-y");
		if (StringUtils.isNotBlank(password))
			cmd.add("-p" + password);
		cmd.add(archive.getAbsolutePath());
		cmd.addAll(Arrays.asList(sources));
		try {
			Processes.callProcess(false, cmd, archive.getParentPath(), new Log());
		} catch (ExitValueException ex) {
			throw translate(ex.getExitValue());
		}
	}

	public void extract(FilePath archive, String password, FilePath target, String... filters) {
		if (filters == null)
			filters = new String[] { "*.*" };
		List<String> cmd = new ArrayList<>();
		cmd.add(executable.getAbsolutePath());
		cmd.add("x");
		cmd.add("-bd");
		cmd.add("-r");
		cmd.add("-y");
		if (StringUtils.isNotBlank(password))
			cmd.add("-p" + password);
		cmd.add(archive.getAbsolutePath());
		cmd.add(target.getAbsolutePath());
		cmd.addAll(Arrays.asList(filters));
		try {
			Processes.callProcess(false, cmd, archive.getParentPath(), new Log());
		} catch (ExitValueException ex) {
			throw translate(ex.getExitValue());
		}
	}

	public List<String> list(FilePath archive, String password) {
		List<String> cmd = new ArrayList<>();
		cmd.add(executable.getAbsolutePath());
		cmd.add("l");
		cmd.add("-bd");
		cmd.add("-r");
		cmd.add("-y");
		if (StringUtils.isNotBlank(password))
			cmd.add("-p" + password);
		cmd.add(archive.getAbsolutePath());
		try {
			return Processes.callProcess(false, cmd, archive.getParentPath(), new ListFiles(), new Log()).getContents();
		} catch (ExitValueException ex) {
			throw translate(ex.getExitValue());
		}
	}

	private RuntimeException translate(int exitValue) {
		// 0 No error
		// 1 Warning (Non fatal error(s)). For example, one or more files were
		// locked by some other application, so they were not compressed.
		// 2 Fatal error
		// 7 Command line error
		// 8 Not enough memory for operation
		// 255 User stopped the process
		if (exitValue == 1) {
			return new RuntimeException(
					"Warning (Non fatal error(s)). For example, one or more files were locked by some other application, so they were not compressed.");
		}
		if (exitValue == 2) {
			return new RuntimeException("Fatal error");
		}
		if (exitValue == 7) {
			return new RuntimeException("Command line error");
		}
		if (exitValue == 8) {
			return new RuntimeException("Not enough memory for operation");
		}
		return null;
	}
}
