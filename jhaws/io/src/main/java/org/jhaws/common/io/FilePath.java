package org.jhaws.common.io;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.io.FilePath.Visitors.CopyAllFilesVisitor;
import org.jhaws.common.io.FilePath.Visitors.MoveAllFilesVisitor;
import org.jhaws.common.io.Utils.OSGroup;

/**
 * @since 1.8
 * @see http://andreinc.net/
 */
public class FilePath implements Path, Externalizable {
	public abstract static class Comparators implements Comparator<FilePath>,
			Serializable {
		private static final long serialVersionUID = 8900331112954086720L;

		public static class LastModifiedTimeComparator extends Comparators {
			private static final long serialVersionUID = 6476737426344812503L;

			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getLastModifiedTime(),
						o2.getLastModifiedTime()).toComparison();
			}
		}

		public static class SizeComparator extends Comparators {
			private static final long serialVersionUID = 1515835061656115678L;

			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getFileSize(),
						o2.getFileSize()).toComparison();
			}
		}
	}

	public static class Filters implements DirectoryStream.Filter<Path>,
			Serializable {
		private static final long serialVersionUID = 8537116661747478884L;

		public static class ExtensionFilter extends Filters {
			private static final long serialVersionUID = -3873046181234664986L;

			protected List<String> ext;

			public ExtensionFilter() {
				ext = new ArrayList<>();
			}

			public ExtensionFilter(List<String> ext) {
				this();
				this.ext.addAll(ext);
			}

			public ExtensionFilter(String... ext) {
				this(Arrays.asList(ext));
			}

			@Override
			public boolean accept(Path entry) {
				String extension = wrap(entry).getExtension();
				return extension == null ? false : ext.contains(extension
						.toLowerCase());
			}

			public List<String> getExt() {
				return ext;
			}

			public void setExt(List<String> ext) {
				this.ext = ext;
			}

			public Filters add(String ext) {
				this.ext.add(ext);
				return this;
			}

			public Filters remove(String ext) {
				this.ext.remove(ext);
				return this;
			}

			public Filters add(Filters.ExtensionFilter otherFilter) {
				this.ext.addAll(otherFilter.ext);
				return this;
			}
		}

		public static class WebImageFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = 2945116770811812757L;

			public WebImageFilter() {
				super("jpg", "jpeg", "png", "gif", "webp");
			}
		}

		public static class VideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = -7739502555496394554L;

			public VideoFilter() {
				super("flv", "webm", "mp4", "m4v", "mpg", "mpeg", "mpe", "mpv",
						"wmv", "avi", "mov", "qt", "asf", "rm", "divx", "mkv");
			}
		}

		public static class Html5VideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = -7433598725949137242L;

			public Html5VideoFilter() {
				super("flv", "webm", "mp4");
			}
		}

		public static class FlashVideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = 4338488348271169459L;

			public FlashVideoFilter() {
				super("flv");
			}
		}

		public static class IIOImageFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = 5142335960151096949L;

			public IIOImageFilter() {
				super(javax.imageio.ImageIO.getReaderFormatNames());
			}
		}

		public static final class AcceptAllFilter extends Filters {
			private static final long serialVersionUID = 5836187025061354523L;

			@Override
			public boolean accept(Path entry) {
				return true;
			}
		}

		public static class CompatibleFilter extends Filters {
			private static final long serialVersionUID = 7805492934831683710L;

			protected java.io.FileFilter fileFilter;

			public CompatibleFilter(java.io.FileFilter fileFilter) {
				this.fileFilter = fileFilter;
			}

			@Override
			public boolean accept(Path entry) {
				return this.fileFilter.accept(entry.toFile());
			}
		}

		public static class DirectoryFilter extends Filters {
			private static final long serialVersionUID = 5397816998252297502L;

			@Override
			public boolean accept(Path entry) {
				return Files.isDirectory(entry);
			}
		}

		public static class FileFilter extends Filters {
			private static final long serialVersionUID = 6174016893380146941L;

			@Override
			public boolean accept(Path entry) {
				return Files.isRegularFile(entry);
			}
		}

		protected DirectoryStream.Filter<? super Path>[] filters;

		protected boolean and;

		protected boolean not;

		@SuppressWarnings("unchecked")
		@SafeVarargs
		public Filters(boolean and, boolean not,
				DirectoryStream.Filter<? super Path>... filters) {
			this.filters = filters.length == 0 ? new DirectoryStream.Filter[] { this }
					: filters;
			this.and = and;
			this.not = not;
		}

		@SafeVarargs
		public Filters(boolean and,
				DirectoryStream.Filter<? super Path>... filters) {
			this(and, false, filters);
		}

		@SafeVarargs
		public Filters(DirectoryStream.Filter<? super Path>... filters) {
			this(true, false, filters);
		}

		@Override
		public boolean accept(Path entry) {
			for (DirectoryStream.Filter<? super Path> filter : this.filters) {
				try {
					if (!this.and /* or */&& filter.accept(entry)) {
						return this.not(true);
					}
				} catch (IOException e) {
					return false;
				}
				try {
					if (this.and && !filter.accept(entry)) {
						return this.not(false);
					}
				} catch (IOException e) {
					return false;
				}
			}
			return this.not(this.and);
		}

		protected boolean not(boolean b) {
			return this.not ? !b : b;
		}
	}

	public abstract static class Iterators implements Serializable {
		private static final long serialVersionUID = 6762741764613317464L;

		public static class FileByteIterator implements Iterator<Byte>,
				Closeable {
			protected transient final FilePath path;

			protected transient Byte b;

			protected transient BufferedInputStream in;

			protected transient boolean openened = false;

			public FileByteIterator(FilePath path) {
				this.path = path;
			}

			@Override
			public void close() throws IOException {
				this.openened = true;
				BufferedInputStream br = this.in();
				if (br != null) {
					br.close();
				}
			}

			@Override
			public boolean hasNext() {
				this.optionalRead();
				return this.b != null;
			}

			protected BufferedInputStream in() throws IOException {
				if (!this.openened) {
					this.openened = true;
					this.in = this.path.newBufferedInputStream();
				}
				return this.in;
			}

			@Override
			public Byte next() {
				this.optionalRead();
				if (this.b == null) {
					throw new NoSuchElementException();
				}
				Byte tmp = this.b;
				this.b = null;
				return tmp;
			}

			protected void optionalRead() {
				if (this.b == null) {
					try {
						BufferedInputStream br = this.in();
						if (br != null) {
							this.b = (byte) br.read();
							if (this.b == -1) {
								this.b = null;
							}
							if (this.b == null) {
								this.close();
							}
						}
					} catch (IOException ex) {
						this.b = null;
					}
				}
			}

			@Override
			public void remove() {
				this.b = null;
			}
		}

		public static class FileLineIterator implements Iterator<String>,
				Closeable {
			protected transient final FilePath path;

			protected transient final Charset charset;

			protected transient String line;

			protected transient BufferedReader in;

			protected transient boolean openened = false;

			public FileLineIterator(FilePath path) {
				this(path, Charset.defaultCharset());
			}

			public FileLineIterator(FilePath path, Charset charset) {
				this.path = path;
				this.charset = charset;
			}

			@Override
			public void close() throws IOException {
				this.openened = true;
				BufferedReader br = this.in();
				if (br != null) {
					br.close();
				}
			}

			@Override
			public boolean hasNext() {
				this.optionalRead();
				return this.line != null;
			}

			protected BufferedReader in() throws IOException {
				if (!this.openened) {
					this.openened = true;
					this.in = this.path.newBufferedReader();
				}
				return this.in;
			}

			@Override
			public String next() {
				this.optionalRead();
				if (this.line == null) {
					throw new NoSuchElementException();
				}
				String tmp = this.line;
				this.line = null;
				return tmp;
			}

			protected void optionalRead() {
				if (this.line == null) {
					try {
						BufferedReader br = this.in();
						if (br != null) {
							this.line = br.readLine();
							if (this.line == null) {
								this.close();
							}
						}
					} catch (IOException ex) {
						this.line = null;
					}
				}
			}

			@Override
			public void remove() {
				this.line = null;
			}
		}

		public static class LineIterator implements Iterator<String>, Closeable {
			protected transient String line;

			protected transient BufferedReader in;

			protected transient boolean openened = true;

			public LineIterator(BufferedReader in) {
				this.in = in;
			}

			public LineIterator(InputStream in) {
				this(in, Charset.defaultCharset());
			}

			public LineIterator(InputStream in, Charset charset) {
				this(new BufferedReader(new InputStreamReader(in, charset)));
			}

			@Override
			public void close() throws IOException {
				this.openened = true;
				BufferedReader br = this.in();
				if (br != null) {
					br.close();
				}
			}

			@Override
			public boolean hasNext() {
				this.optionalRead();
				return this.line != null;
			}

			protected BufferedReader in() throws IOException {
				return this.in;
			}

			@Override
			public String next() {
				this.optionalRead();
				if (this.line == null) {
					throw new NoSuchElementException();
				}
				String tmp = this.line;
				this.line = null;
				return tmp;
			}

			protected void optionalRead() {
				if (this.line == null) {
					try {
						BufferedReader br = this.in();
						if (br != null) {
							this.line = br.readLine();
							if (this.line == null) {
								this.close();
							}
						}
					} catch (IOException ex) {
						this.line = null;
					}
				}
			}

			@Override
			public void remove() {
				this.line = null;
			}
		}

		public static class PathIterator implements Iterator<Path> {
			protected transient Path path;

			public PathIterator(Path path) {
				this.path = path;
			}

			@Override
			public boolean hasNext() {
				Path parent = this.path.getParent();
				return parent != null;
			}

			@Override
			public Path next() {
				this.path = this.path.getParent();
				return this.path;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}

	public static class Visitors extends SimpleFileVisitor<Path> implements
			Serializable {
		private static final long serialVersionUID = 7414917192031528908L;

		public static class DeleteAllFilesVisitor extends Visitors {
			private static final long serialVersionUID = 5288444763948803482L;

			protected boolean ifExists = false;

			public DeleteAllFilesVisitor() {
				//
			}

			public DeleteAllFilesVisitor(boolean ifExists) {
				this.ifExists = ifExists;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				if (this.ifExists) {
					Files.deleteIfExists(dir);
				} else {
					Files.delete(dir);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (this.ifExists) {
					Files.deleteIfExists(file);
				} else {
					Files.delete(file);
				}
				return FileVisitResult.CONTINUE;
			}
		}

		public static class CopyAllFilesVisitor extends
				MoveOrCopyAllFilesVisitor {
			private static final long serialVersionUID = -2602269065839902682L;

			public CopyAllFilesVisitor(Path source, Path target) {
				super(false, source, target);
			}
		}

		public static class MoveAllFilesVisitor extends
				MoveOrCopyAllFilesVisitor {
			private static final long serialVersionUID = 8655910973146980405L;

			public MoveAllFilesVisitor(Path source, Path target) {
				super(true, source, target);
			}
		}

		protected static class MoveOrCopyAllFilesVisitor extends Visitors {
			private static final long serialVersionUID = -3550396212952077422L;

			protected FilePath source;

			protected FilePath target;

			protected boolean move = false;

			public MoveOrCopyAllFilesVisitor(boolean move, Path source,
					Path target) {
				this.move = move;
				this.source = FilePath.wrap(source);
				this.target = FilePath.wrap(target);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				if (move)
					try {
						Files.delete(dir);
					} catch (java.nio.file.DirectoryNotEmptyException ex) {
						for (Object o : FilePath.wrap(dir).listFiles()) {
							System.out.println(o);
						}
						for (Object o : FilePath.wrap(dir).listDirectories()) {
							System.out.println(o);
						}
						throw ex;
					}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				FilePath targetPath = FilePath.wrap(this.target
						.resolve(this.source.relativize(file)));
				targetPath.getParentPath().createDirectories();
				if (move)
					targetPath.moveFrom(file,
							StandardCopyOption.REPLACE_EXISTING);
				else
					targetPath.copyFrom(file,
							StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		}
	}

	public static FilePath createDefaultTempDirectory(String prefix,
			FileAttribute<?>... attrs) throws UncheckedIOException {
		try {
			return new FilePath(Files.createTempDirectory(prefix, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static FilePath createDefaultTempFile(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		return FilePath.createDefaultTempFile(System.currentTimeMillis() + "-"
				+ RND.nextLong(), null, attrs);
	}

	public static FilePath createDefaultTempFile(String extension,
			FileAttribute<?>... attrs) throws UncheckedIOException {
		return FilePath.createDefaultTempFile(null, extension, attrs);
	}

	public static FilePath createDefaultTempFile(String prefix,
			String extension, FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createTempFile(prefix == null ? null
					: prefix + "-",
					extension == null ? "" : FilePath.getFileSeperator()
							+ extension, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static String getConvertedSize(Long size) {
		if (size == null || size == 0l) {
			return 0 + UNITS[0];
		}
		int scale = (int) (Math.log10(size) / 3);
		NumberFormat nf = NumberFormat.getNumberInstance();
		DecimalFormat df = (DecimalFormat) nf;
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(2);
		return "" + df.format((double) size / (1 << (scale * 10))) + ""
				+ FilePath.UNITS[scale];
	}

	public static FilePath getDesktopDirectory() {
		return new FilePath(FilePath.DESKTOPDIR);
	}

	public static List<FilePath> getDrives() {
		List<FilePath> drives = new ArrayList<FilePath>();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// windows
		if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) {
			String systemDriveString = System.getProperty("user.home")
					.substring(0, 2) + File.separator;
			File systemDriveFile = new File(systemDriveString);
			File parentSystemDrive = fsv.getParentDirectory(systemDriveFile);
			for (File f : parentSystemDrive.listFiles()) {
				try {
					if (fsv.isFileSystem(f) && fsv.isDrive(f)
							&& fsv.isTraversable(f) && !fsv.isFloppyDrive(f)
							&& (f.listFiles().length > 0)) {
						if (new FilePath(f).getParent() == null) {
							drives.add(new FilePath(f));
						}
					}
				} catch (Throwable e) {
					// not accesible
				}
			}
		} else {
			// unix
			for (File f : fsv.getRoots()) {
				drives.add(new FilePath(f));
			}
		}
		return drives;
	}

	public static String getExtension(Path path) {
		String fileName = path.getFileName().toString();
		int p = fileName.lastIndexOf(FilePath.getFileSeperator());
		if (p == -1) {
			return null;
		}
		return fileName.substring(p + 1);
	}

	public static String getFileSeperator() {
		return String.valueOf(FilePath.DOT);
	}

	public static char getFileSeperatorChar() {
		return FilePath.DOT;
	}

	public static Path getPath(Path p) {
		while (p instanceof FilePath) {
			p = FilePath.class.cast(p).path;
		}
		return p;
	}

	public static String getPathSeperator() {
		return File.separator;
	}

	public static char getPathSeperatorChar() {
		return FilePath.getPathSeperator().charAt(0);
	}

	public static String getShortFileName(Path path) {
		String fileName = path.getFileName().toString();
		int p = fileName.lastIndexOf(FilePath.DOT);
		if (p == -1) {
			return fileName;
		}
		return fileName.substring(0, p);
	}

	public static FilePath getTempDirectory() {
		return new FilePath(FilePath.TEMPDIR);
	}

	public static FilePath getUserHomeDirectory() {
		return new FilePath(FilePath.USERDIR);
	}

	/**
	 * convers a filename to a legal filename for given operating system, too
	 * long parts are chopped, illegal characters are replaced by the character
	 * <i>_</i> , a missing extensions is adapted to extension <i>ext</i>
	 *
	 * @param filename
	 *            : String : current name
	 * @param os
	 *            : int : operating system
	 *
	 * @return : String : converted name
	 */
	public static FilePath legalize(String filename, final OSGroup os)
			throws UncheckedIOException {
		filename = new FilePath(filename).getName();
		while (filename.substring(0, 1).compareTo(FilePath.getFileSeperator()) == 0) {
			filename = filename.substring(1, filename.length());
		}
		char[] c = new char[0];
		if (os == OSGroup.Dos) {
			String[] parts = filename.split("\\" + FilePath.getFileSeperator());
			if (parts.length == 1) {
				if (filename
						.substring(filename.length() - 1, filename.length())
						.compareTo(FilePath.getFileSeperator()) != 0) {
					filename = filename + FilePath.getFileSeperator();
				}

				filename = filename + "ext";
			}
			if (parts.length > 2) {
				filename = parts[0] + FilePath.getFileSeperator()
						+ parts[parts.length - 1];
			}
			parts = filename.split("\\" + FilePath.getFileSeperator());
			if (parts[0].length() > 8) {
				parts[0] = parts[0].substring(0, 8);
			}
			if (parts[1].length() > 3) {
				parts[1] = parts[1].substring(0, 3);
			}
			filename = parts[0] + FilePath.getFileSeperator() + parts[1];
			c = Utils.legal(os);
		}
		if (os == OSGroup.Windows) {
			c = Utils.legal(os);
		}
		if (os == OSGroup.Nix) {
			c = Utils.legal(os);
		}
		if (c.length == 0) {
			throw new UncheckedIOException(new IOException(
					"characters for OS not found"));
		}
		char[] fn = filename.toCharArray();
		for (int i = 0; i < fn.length; i++) {
			boolean chk = false;
			for (int j = 0; (j < c.length) && !chk; j++) {
				if (fn[i] == c[j]) {
					chk = true;
				}
			}
			if (!chk) {
				fn[i] = '_';
			}
		}
		return new FilePath(new String(fn));
	}

	protected static FilePath newFileIndex(Path parent, String outFileName,
			String extension) {
		return FilePath.newFileIndex(parent, outFileName, "_", "0000",
				extension);
	}

	/**
	 * if file does not exists, return it<br>
	 * if file exists and does not end on _9999 (any number), adds _0000 and
	 * does index checking<br>
	 * if file exists and does end on _9999 (any number), and does index
	 * checking<br>
	 * <br>
	 * index checking:<br>
	 * if file exists, return it<br>
	 * if file does not exists, adds 1 to the index (_0000 goes to _0001) and
	 * does further index again
	 *
	 * @param parent
	 *            : String : the location (path only) of the target file
	 * @param outFileName
	 *            : String : the name of the target file (without extension and
	 *            . before extension)
	 * @param sep
	 *            : String : characters sperating filename from index (example:
	 *            _ )
	 * @param format
	 *            : String : number of positions character 0 (example: 0000 )
	 * @param extension
	 *            : String : the extension of the target file (without . before
	 *            extension), see class constants FORMAT... for possibilities
	 *
	 * @return : IOFile : new indexed File
	 */
	protected static FilePath newFileIndex(Path parent, String outFileName,
			String sep, String format, String extension) {
		String SEPARATOR = sep;
		String FORMAT = sep + format;
		FilePath file = StringUtils.isBlank(extension) ? new FilePath(parent,
				outFileName) : new FilePath(parent, outFileName
				+ FilePath.getFileSeperator() + extension);
		if (file.exists()) {
			if (outFileName.length() <= FORMAT.length()) {
				outFileName = outFileName + FORMAT;
				if (StringUtils.isBlank(extension)) {
					file = new FilePath(parent, outFileName);
				} else {
					file = new FilePath(parent, outFileName
							+ FilePath.getFileSeperator() + extension);
				}
			} else {
				String ch = outFileName.substring(
						outFileName.length() - FORMAT.length(),
						(outFileName.length() - FORMAT.length())
								+ SEPARATOR.length());
				String nr = outFileName.substring(
						(outFileName.length() - FORMAT.length())
								+ SEPARATOR.length(), outFileName.length());
				boolean isNumber = true;
				try {
					Integer.parseInt(nr);
				} catch (final NumberFormatException ex) {
					isNumber = false;
				}
				if (!(isNumber && (ch.compareTo(SEPARATOR) == 0))) {
					outFileName = outFileName + FORMAT;
					if (StringUtils.isBlank(extension)) {
						file = new FilePath(parent, outFileName);
					} else {
						file = new FilePath(parent, outFileName
								+ FilePath.getFileSeperator() + extension);
					}
				}
			}
		}
		int ind = 0;
		while (file.exists()) {
			StringBuilder indStringSB = new StringBuilder(16);
			int nr0 = FORMAT.length() - SEPARATOR.length()
					- String.valueOf(ind).length();
			for (int i = 0; i < nr0; i++) {
				indStringSB.append("0");
			}
			indStringSB.append(String.valueOf(ind));
			if (extension.equals("")) {
				file = new FilePath(parent, outFileName.substring(0,
						outFileName.length() - FORMAT.length())
						+ SEPARATOR
						+ indStringSB.toString());
			} else {
				file = new FilePath(parent, outFileName.substring(0,
						outFileName.length() - FORMAT.length())
						+ SEPARATOR
						+ indStringSB.toString()
						+ FilePath.getFileSeperator()
						+ extension);
			}
			ind++;
		}
		return file;
	}

	public static FilePath wrap(Path p) {
		return p instanceof FilePath ? FilePath.class.cast(p) : new FilePath(p);
	}

	public static final char DOT = '.';

	protected transient Path path;

	protected static ExtensionIconFinder grabber = new org.jhaws.common.io.SystemIcon();

	protected static Random RND = new Random(System.currentTimeMillis());

	/** temp dir */
	public static final String TEMPDIR = System.getProperty("java.io.tmpdir");

	/** desktop */
	public static final String DESKTOPDIR = FileSystemView.getFileSystemView()
			.getHomeDirectory().getAbsolutePath();

	/** user home dir */
	public static final String USERDIR = System.getProperty("user.home");

	public static final String[] UNITS = new String[] { "bytes", "kB", "MB",
			"GB", "TB" };

	public FilePath() {
		this.path = Paths.get(FilePath.getFileSeperator()).toAbsolutePath()
				.normalize();
	}

	public FilePath(Class<?> root, String relativePath)
			throws UncheckedIOException {
		this(root.getClassLoader().getResource(
				root.getPackage().getName()
						.replace(FilePath.DOT, FilePath.getPathSeperatorChar())
						+ (relativePath.startsWith("/") ? "" : "/")
						+ relativePath));
	}

	public FilePath(File file) {
		this.path = Paths.get(file.toURI());
	}

	public FilePath(File dir, String file) {
		this.path = Paths.get(dir.toURI()).resolve(file);
	}

	public FilePath(FilePath file) {
		if (file.getPath() == null) {
			throw new NullPointerException();
		}
		this.path = file.path;
	}

	public FilePath(FilePath dir, String file) {
		this.path = dir.resolve(file);
	}

	public FilePath(Path path) {
		if (path == null) {
			throw new NullPointerException();
		}
		this.path = path;
	}

	public FilePath(Path dir, String file) {
		this.path = dir.resolve(file);
	}

	public FilePath(String dir, String... file) {
		this.path = Paths.get(dir, file);
	}

	public FilePath(URI uri) {
		this.path = Paths.get(uri);
	}

	public FilePath(URL url) throws UncheckedIOException {
		if (url == null)
			throw new UncheckedIOException(new IOException("url is null"));
		try {
			URI uri = url.toURI();
			if ("file".equals(uri.getScheme())) {
				String p = uri.getPath().substring(1);
				this.path = Paths.get(p);
			} else {
				this.path = Paths.get(uri);
			}
		} catch (URISyntaxException ex) {
			throw new UncheckedIOException(new IOException(String.valueOf(ex)));
		}
	}

	public FilePath addExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getFullFileName()
				+ FilePath.DOT + extension) : new FilePath(this.getParent(),
				this.getFullFileName() + FilePath.DOT + extension);
	}

	public long adler32() throws UncheckedIOException {
		return this.checksum(new Adler32());
	}

	public Iterators.FileByteIterator bytes() throws UncheckedIOException {
		return new Iterators.FileByteIterator(this);
	}

	public FilePath changeExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getShortFileName()
				+ FilePath.DOT + extension) : new FilePath(this.getParent(),
				this.getShortFileName() + FilePath.DOT + extension);
	}

	public FilePath appendExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getFileNameString()
				+ FilePath.DOT + extension) : new FilePath(this.getParent(),
				this.getFileNameString() + FilePath.DOT + extension);
	}

	public FilePath checkDirectory() throws AccessDeniedException {
		if (this.isFile()) {
			throw new AccessDeniedException(this.toString());
		}
		return this;
	}

	public FilePath checkExists() throws FileNotFoundException {
		if (!this.exists()) {
			throw new FileNotFoundException(this.toString());
		}
		return this;
	}

	public FilePath checkFile() throws AccessDeniedException {
		if (this.isDirectory()) {
			throw new AccessDeniedException(this.toString());
		}
		return this;
	}

	public FilePath checkNotExists() throws FileAlreadyExistsException {
		if (this.exists()) {
			throw new FileAlreadyExistsException(this.toString());
		}
		return this;
	}

	public long checksum(Checksum checksum) throws UncheckedIOException {
		try (Iterators.FileByteIterator bytes = this.bytes()) {
			while (bytes.hasNext()) {
				checksum.update(bytes.next());
			}
			return checksum.getValue();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath child(Path other) {
		return new FilePath(this.getPath().resolve(FilePath.getPath(other)));
	}

	public FilePath child(String other) {
		return new FilePath(this.getPath().resolve(other));
	}

	/**
	 *
	 * @see java.nio.file.Path#compareTo(java.nio.file.Path)
	 */
	@Override
	public int compareTo(Path other) {
		return this.getPath().compareTo(FilePath.getPath(other));
	}

	public long copyFrom(InputStream in, CopyOption... options)
			throws UncheckedIOException {
		try {
			return Files.copy(in, this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath copyFrom(Path source, CopyOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.copy(FilePath.getPath(source), getPath(),
					options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public long copyTo(OutputStream out) throws UncheckedIOException {
		try {
			return Files.copy(this.getPath(), out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath copyTo(Path target) throws UncheckedIOException {
		return this.copyTo(FilePath.getPath(target),
				StandardCopyOption.REPLACE_EXISTING);
	}

	public FilePath copyTo(Path target, CopyOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.copy(this.getPath(),
					FilePath.getPath(target), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public long crc32() throws UncheckedIOException {
		return this.checksum(new CRC32());
	}

	public FilePath createDirectories(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createDirectories(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createDirectory(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createDirectory(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createDirectoryIfNotExists(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		if (this.exists() && this.isDirectory()) {
			return this;
		}
		if (this.exists()) {
			throw new UncheckedIOException(new IOException(
					"file is not a directory"));
		}
		return this.createDirectory(attrs);
	}

	public FilePath createFile(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createFile(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createFileIfNotExists(FileAttribute<?>... attrs)
			throws UncheckedIOException {
		if (this.exists() && this.isFile()) {
			return this;
		}
		return this.createFile(attrs);
	}

	public FilePath createLinkFrom(Path existing) throws UncheckedIOException {
		try {
			return new FilePath(Files.createLink(this.getPath(), existing));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createLinkTo(Path link) throws UncheckedIOException {
		try {
			return new FilePath(Files.createLink(link, this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkFrom(Path link, FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createSymbolicLink(link, this.getPath(),
					attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkTo(Path target, FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.createSymbolicLink(this.getPath(),
					target, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createTempDirectory(String prefix,
			FileAttribute<?>... attrs) throws UncheckedIOException {
		try {
			return new FilePath(Files.createTempDirectory(this.getPath(),
					prefix, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createTempFile(String prefix, FileAttribute<?>... attrs)
			throws UncheckedIOException {
		return this.createTempFile(prefix, null, attrs);
	}

	public FilePath createTempFile(String prefix, String extension,
			FileAttribute<?>... attrs) throws UncheckedIOException {
		try {
			return new FilePath(Files.createTempFile(this.getPath(), prefix
					+ "-", extension == null ? "" : FilePath.getFileSeperator()
					+ extension, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath delete() throws UncheckedIOException {
		try {
			Files.delete(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath deleteAll() throws UncheckedIOException {
		return this.walkFileTree(new Visitors.DeleteAllFilesVisitor(false));
	}

	public boolean deleteAllIfExists() throws UncheckedIOException {
		if (!this.exists()) {
			return false;
		}
		this.walkFileTree(new Visitors.DeleteAllFilesVisitor(true));
		return true;
	}

	public Collection<FilePath>[] deleteDuplicates()
			throws UncheckedIOException {
		return deleteDuplicates(true);
	}

	public static class FindDuplicateMeta {
		private final FilePath file;

		private final Long length;

		private Long check;

		public FindDuplicateMeta(FilePath file) {
			this.file = file;
			this.length = file.getFileSize();
		}

		public FilePath getFile() {
			return file;
		}

		public Long getLength() {
			return length;
		}

		public Long getCheck() {
			if (check == null)
				check = file.adler32();
			return check;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((file == null) ? 0 : file.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FindDuplicateMeta other = (FindDuplicateMeta) obj;
			if (file == null) {
				if (other.file != null)
					return false;
			} else if (!file.equals(other.file))
				return false;
			return true;
		}

		public String naturalKey() {
			return getLength() + "_" + getCheck();
		}

		public boolean naturalEquals(FindDuplicateMeta other) {
			if (!getLength().equals(other.getLength())) {
				return false;
			}
			return getCheck().equals(other.getCheck());
		}

		@Override
		public String toString() {
			return "FindDoublesMeta [file=" + file + ", length=" + length
					+ ", check=" + check + "]";
		}
	}

	public static class FindDuplicateData {
		private final List<FindDuplicateMeta> files = new ArrayList<>();

		private final Map<String, List<FilePath>> duplicates = new HashMap<>();

		public boolean add(FilePath file) {
			FindDuplicateMeta meta = new FindDuplicateMeta(file);
			FindDuplicateMeta duplicate = files.stream()
					.filter(meta::naturalEquals).findFirst().orElse(null);
			if (duplicate != null) {
				String key = duplicate.naturalKey();
				List<FilePath> list = duplicates.get(key);
				if (list == null) {
					list = new ArrayList<>();
					duplicates.put(key, list);
					list.add(duplicate.getFile());
				}
				list.add(meta.getFile());
			}
			files.add(meta);
			return duplicate != null;
		}

		@SuppressWarnings("unchecked")
		public Collection<FilePath>[] getDuplicates() {
			return duplicates.values().toArray(
					new Collection[duplicates.size()]);
		}
	}

	public Collection<FilePath>[] deleteDuplicates(boolean doDelete)
			throws UncheckedIOException {
		FindDuplicateData data = new FindDuplicateData();
		try {
			Files.walkFileTree(this.getPath(), new HashSet<FileVisitOption>(),
					1, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file,
								BasicFileAttributes attrs) throws IOException {
							if (attrs.isRegularFile()) {
								FilePath fp = new FilePath(file);
								if (data.add(fp) && doDelete) {
									fp.delete();
								}
							}
							return super.visitFile(file, attrs);
						}
					});
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return data.getDuplicates();
	}

	public boolean deleteIfExists() throws UncheckedIOException {
		try {
			return Files.deleteIfExists(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * downloads a file from the web to a local file when it does not exists or
	 * is older, binary copy
	 *
	 * @param urlSourceFile
	 *            : URL : file on the web
	 *
	 * @return : boolean : file downloaded or not
	 */
	public boolean download(URL urlSourceFile) throws UncheckedIOException {
		if (this.exists()) {
			long localFileTime = this.getLastModifiedTime().toMillis();
			URLConnection conn;
			try {
				conn = urlSourceFile.openConnection();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
			long webFileTime = conn.getLastModified();
			if (webFileTime <= localFileTime) {
				return false;
			}
			long len = conn.getContentLengthLong();
			if ((len != -1) && (len == this.getFileSize())) {
				return false;
			}
		}
		try (InputStream in = new BufferedInputStream(
				urlSourceFile.openStream());
				OutputStream out = this.newBufferedOutputStream()) {
			Utils.copy(in, out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	/**
	 * @see java.nio.file.Path#endsWith(java.nio.file.Path)
	 */
	@Override
	public boolean endsWith(Path other) {
		return this.getPath().endsWith(other);
	}

	/**
	 * @see java.nio.file.Path#endsWith(java.lang.String)
	 */
	@Override
	public boolean endsWith(String other) {
		return this.getPath().endsWith(other);
	}

	public boolean equal(Path file) throws UncheckedIOException {
		FilePath filePath = FilePath.wrap(file);
		return this.equal(filePath, filePath.getFileSize());
	}

	/**
	 * compares two files binary, reads only first limit bytes
	 *
	 * @param file
	 *            : Path : file to compare to
	 * @param limit
	 *            : int : maximum size when comparing files
	 *
	 * @return : boolean : file is equal or not
	 */
	public boolean equal(Path file, long limit) throws UncheckedIOException {
		FilePath otherPath = FilePath.wrap(file);
		if (this.notExists() || otherPath.notExists() || this.isDirectory()
				|| this.isDirectory()) {
			return false;
		}
		long size = this.getFileSize();
		long otherSize = otherPath.getFileSize();
		if (size != otherSize) {
			return false;
		}
		long compareSize = Math.min(limit, size);
		long comparedSize = 0l;
		try (Iterators.FileByteIterator buffer = this.bytes();
				Iterators.FileByteIterator otherBuffer = otherPath.bytes()) {
			while (buffer.hasNext() || otherBuffer.hasNext()) {
				if (!buffer.hasNext() || !otherBuffer.hasNext()) {
					return false;
				}
				Byte thisByte = buffer.next();
				Byte otherByte = otherBuffer.next();
				if (thisByte != otherByte) {
					return false;
				}
				if (comparedSize++ >= compareSize) {
					break;
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Path)) {
			return false;
		}
		return this.getPath().equals(FilePath.getPath(Path.class.cast(other)));
	}

	public boolean exists(LinkOption... options) {
		return Files.exists(this.getPath(), options);
	}

	/**
	 * flattens this directory, copies all files in all subdirectories to this
	 * directory, deletes doubles, rename if file already exists and isn't the
	 * same, delete all subdirectories afterwards
	 */
	public FilePath flatten() throws UncheckedIOException {
		final FilePath root = this;
		this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				if (!root.equals(dir)) {
					FilePath fpd = new FilePath(dir);
					try {
						fpd.delete();
					} catch (UncheckedIOException e) {
						if (e.getCause() instanceof java.nio.file.DirectoryNotEmptyException) {
							try {
								Thread.sleep(250);
							} catch (InterruptedException e1) {
								//
							}
							fpd.delete();
						}
					}
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (!root.equals(file.getParent())) {
					new FilePath(file).moveTo(new FilePath(root, file
							.getFileName().toString()).newFileIndex());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return this;
	}

	public String getAbsolutePath() {
		return this.toAbsolutePath().toString();
	}

	public Object getAttribute(String attribute, LinkOption... options)
			throws UncheckedIOException {
		try {
			return Files.getAttribute(this.getPath(), attribute, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public List<FilePath> getChildren() {
		List<FilePath> children = new ArrayList<>();
		try (DirectoryStream<Path> stream = this.newDirectoryStream()) {
			for (Path file : stream) {
				children.add(new FilePath(file));
			}
		} catch (IOException ex) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			throw new UncheckedIOException(ex);
		} catch (DirectoryIteratorException ex) {
			throw new UncheckedIOException(new IOException(String.valueOf(ex)));
		}
		return children;
	}

	public String getConvertedFileSize() throws UncheckedIOException {
		return FilePath.getConvertedSize(this.getFileSize());
	}

	public Charset getDefaultCharset() {
		return Charset.defaultCharset();
	}

	public String getExtension() {
		return FilePath.getExtension(this.getPath());
	}

	public <V extends FileAttributeView> V getFileAttributeView(Class<V> type,
			LinkOption... options) {
		return Files.getFileAttributeView(this.getPath(), type, options);
	}

	/**
	 * @see java.nio.file.Path#getFileName()
	 */
	@Override
	public Path getFileName() {
		return new FilePath(this.getPath().getFileName());
	}

	public long getFileSize() throws UncheckedIOException {
		try {
			return Files.size(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FileStore getFileStore() throws UncheckedIOException {
		try {
			return Files.getFileStore(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#getFileSystem()
	 */
	@Override
	public FileSystem getFileSystem() {
		return this.getPath().getFileSystem();
	}

	public long getFolderSize() throws UncheckedIOException {
		final AtomicLong size = new AtomicLong(0);
		this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				size.addAndGet(attrs.size());
				return FileVisitResult.CONTINUE;
			}
		});
		return size.get();
	}

	public String getFullFileName() {
		return this.getPath().getFileName().toString();
	}

	public String getFullPathName() {
		return this.getPath().toString();
	}

	public Icon getLargeIcon() {
		return FilePath.grabber.getLargeIcon(this.toFile());
	}

	public FileTime getLastModifiedTime(LinkOption... options)
			throws UncheckedIOException {
		try {
			return Files.getLastModifiedTime(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public int getLineCount() throws UncheckedIOException {
		try (LineNumberReader lnr = new LineNumberReader(
				this.newBufferedReader())) {
			lnr.skip(Long.MAX_VALUE);
			return lnr.getLineNumber() + 1;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public String getName() {
		return this.getFileName().toString();
	}

	/**
	 * @see java.nio.file.Path#getName(int)
	 */
	@Override
	public Path getName(int index) {
		return new FilePath(this.getPath().getName(index));
	}

	/**
	 * @see java.nio.file.Path#getNameCount()
	 */
	@Override
	public int getNameCount() {
		return this.getPath().getNameCount();
	}

	public UserPrincipal getOwner(LinkOption... options)
			throws UncheckedIOException {
		try {
			return Files.getOwner(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#getParent()
	 */
	@Override
	public Path getParent() {
		return this.getParentPath();
	}

	public FilePath getParentPath() {
		return this.getPath().getParent() == null ? null : new FilePath(this
				.getPath().getParent());
	}

	public Path getPath() {
		return FilePath.getPath(this);
	}

	public Set<PosixFilePermission> getPosixFilePermissions(
			LinkOption... options) throws UncheckedIOException {
		try {
			return Files.getPosixFilePermissions(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#getRoot()
	 */
	@Override
	public Path getRoot() {
		return new FilePath(this.getPath().getRoot());
	}

	public String getShortFileName() {
		return FilePath.getShortFileName(this);
	}

	public Icon getSmallIcon() {
		return FilePath.grabber.getSmallIcon(this.toFile());
	}

	/**
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getPath().hashCode();
	}

	/**
	 *
	 * @see java.nio.file.Path#isAbsolute()
	 */
	@Override
	public boolean isAbsolute() {
		return this.getPath().isAbsolute();
	}

	public boolean isDirectory(LinkOption... options) {
		return Files.isDirectory(this.getPath(), options);
	}

	public boolean isExecutable() {
		return Files.isExecutable(this.getPath());
	}

	public boolean isFile(LinkOption... options) {
		return Files.isRegularFile(this.getPath(), options);
	}

	public boolean isHidden() throws UncheckedIOException {
		try {
			return Files.isHidden(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean isReadable() {
		return Files.isReadable(this.getPath());
	}

	public boolean isSameFile(Path otherPath) throws UncheckedIOException {
		try {
			return Files.isSameFile(this.getPath(), otherPath);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean isSymbolicLink() {
		return Files.isSymbolicLink(this.getPath());
	}

	public boolean isWritable() {
		return Files.isWritable(this.getPath());
	}

	/**
	 * @see java.nio.file.Path#iterator()
	 */
	@Override
	public Iterator<Path> iterator() {
		return this.getPath().iterator();
	}

	public Iterators.FileLineIterator lines() throws UncheckedIOException {
		return new Iterators.FileLineIterator(this);
		// return Files.lines(this.getPath());
	}

	public List<FilePath> list() throws UncheckedIOException {
		return this.list(false);
	}

	public List<FilePath> list(boolean iterate) throws UncheckedIOException {
		return this.list(iterate, new Filters.AcceptAllFilter());
	}

	public List<FilePath> list(boolean iterate,
			DirectoryStream.Filter<? super Path> filter)
			throws UncheckedIOException {
		Deque<FilePath> stack = new ArrayDeque<>();
		List<FilePath> files = new LinkedList<>();
		stack.push(this);
		Filters filterAndDirs = new Filters(false, filter,
				new Filters.DirectoryFilter());
		while (!stack.isEmpty()) {
			try (DirectoryStream<Path> stream = stack.pop().newDirectoryStream(
					filterAndDirs)) {
				for (Path p : stream) {
					FilePath child = new FilePath(p);
					if (filter.accept(p)) {
						files.add(child);
					}
					if (iterate && child.isDirectory()) {
						stack.push(child);
					}
				}
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
		return files;
	}

	public List<FilePath> list(DirectoryStream.Filter<? super Path> filter)
			throws UncheckedIOException {
		return this.list(false, filter);
	}

	public List<FilePath> listDirectories() throws UncheckedIOException {
		return this.listDirectories(false);
	}

	public List<FilePath> listDirectories(boolean iterate)
			throws UncheckedIOException {
		return this.list(iterate, new Filters.DirectoryFilter());
	}

	public List<FilePath> listFiles() throws UncheckedIOException {
		return this.listFiles(false);
	}

	public List<FilePath> listFiles(boolean iterate)
			throws UncheckedIOException {
		return this.list(iterate, new Filters.FileFilter());
	}

	public FilePath moveFrom(Path source) throws UncheckedIOException {
		return this.moveFrom(source, StandardCopyOption.ATOMIC_MOVE,
				StandardCopyOption.REPLACE_EXISTING);
	}

	public FilePath moveFrom(Path source, CopyOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.move(FilePath.getPath(source),
					this.getPath(), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath moveAllTo(Path target) throws UncheckedIOException {
		FilePath tp = FilePath.wrap(target);
		if (tp.notExists()) {
			return this.moveTo(target, StandardCopyOption.REPLACE_EXISTING);
		}
		return this.walkFileTree(new MoveAllFilesVisitor(this, tp));
	}

	public FilePath copyAllTo(Path target) throws UncheckedIOException {
		FilePath tp = FilePath.wrap(target);
		if (tp.notExists()) {
			return this.copyTo(target, StandardCopyOption.REPLACE_EXISTING);
		}
		return this.walkFileTree(new CopyAllFilesVisitor(this, tp));
	}

	public FilePath moveTo(Path target, CopyOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.move(this.getPath(),
					FilePath.getPath(target), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BufferedInputStream newBufferedInputStream(OpenOption... options)
			throws UncheckedIOException {
		return new BufferedInputStream(this.newInputStream(options));
	}

	public BufferedOutputStream newBufferedOutputStream(OpenOption... options)
			throws UncheckedIOException {
		return new BufferedOutputStream(this.newOutputStream(options));
	}

	public BufferedReader newBufferedReader() throws UncheckedIOException {
		return this.newBufferedReader(this.getDefaultCharset());
	}

	public BufferedReader newBufferedReader(Charset charset)
			throws UncheckedIOException {
		try {
			return Files.newBufferedReader(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BufferedWriter newBufferedWriter(Charset charset,
			OpenOption... options) throws UncheckedIOException {
		try {
			return Files.newBufferedWriter(this.getPath(), charset, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BufferedWriter newBufferedWriter(OpenOption... options)
			throws UncheckedIOException {
		return this.newBufferedWriter(this.getDefaultCharset(), options);
	}

	public SeekableByteChannel newByteChannel(OpenOption... options)
			throws UncheckedIOException {
		try {
			return Files.newByteChannel(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public SeekableByteChannel newByteChannel(
			Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws UncheckedIOException {
		try {
			return Files.newByteChannel(this.getPath(), options, attrs);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public DirectoryStream<Path> newDirectoryStream()
			throws UncheckedIOException {
		try {
			return Files.newDirectoryStream(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public DirectoryStream<Path> newDirectoryStream(
			DirectoryStream.Filter<? super Path> filter)
			throws UncheckedIOException {
		try {
			return Files.newDirectoryStream(this.getPath(), filter);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public DirectoryStream<Path> newDirectoryStream(String glob)
			throws UncheckedIOException {
		try {
			return Files.newDirectoryStream(this.getPath(), glob);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * {@link #newFileIndex(String, String, String, String, String)} but with
	 * separator set to '_' and format to '0000' and the other parameters
	 * derived from given File
	 */
	public FilePath newFileIndex() {
		if (this.notExists()) {
			return this;
		}
		String shortFile = this.getShortFileName();
		String extension = this.getExtension();
		return FilePath.newFileIndex(new FilePath(this.getParent()), shortFile,
				extension);
	}

	public InputStream newInputStream(OpenOption... options)
			throws UncheckedIOException {
		try {
			return Files.newInputStream(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public OutputStream newOutputStream(OpenOption... options)
			throws UncheckedIOException {
		try {
			return Files.newOutputStream(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#normalize()
	 */
	@Override
	public Path normalize() {
		return new FilePath(this.getPath().normalize());
	}

	public boolean notExists(LinkOption... options) {
		return Files.notExists(this.getPath(), options);
	}

	public void open() {
		try {
			Desktop.getDesktop().open(this.toFile());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Iterators.PathIterator paths() {
		return new Iterators.PathIterator(this);
	}

	public Iterators.PathIterator pathsToRoot() {
		return new Iterators.PathIterator(FilePath.wrap(this.toAbsolutePath()));

	}

	public String probeContentType() throws UncheckedIOException {
		try {
			return Files.probeContentType(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public InputStream read() throws UncheckedIOException {
		return this.newInputStream();
	}

	public long read(InputStream in) throws UncheckedIOException {
		return this.copyFrom(in);
	}

	public String readAll() throws UncheckedIOException {
		return this.readAll(this.getDefaultCharset());
	}

	public String readAll(Charset charset) throws UncheckedIOException {
		try {
			return new String(Files.readAllBytes(this.getPath()), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public byte[] readAllBytes() throws UncheckedIOException {
		try {
			return Files.readAllBytes(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public ByteBuffer readAllBytesToBuffer() throws UncheckedIOException {
		try {
			return ByteBuffer.wrap(Files.readAllBytes(this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public List<String> readAllLines() throws UncheckedIOException {
		return this.readAllLines(this.getDefaultCharset());
	}

	public List<String> readAllLines(Charset charset)
			throws UncheckedIOException {
		try {
			return Files.readAllLines(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<String> streamContentString() throws UncheckedIOException {
		return this.streamContentString(this.getDefaultCharset());
	}

	public Stream<Path> streamChildren() {
		return StreamSupport.stream(newDirectoryStream().spliterator(), false);
	}

	public Stream<String> streamContentString(Charset charset)
			throws UncheckedIOException {
		try {
			return Files.lines(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public <A extends BasicFileAttributes> A readAttributes(Class<A> type,
			LinkOption... options) throws UncheckedIOException {
		try {
			return Files.readAttributes(this.getPath(), type, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Map<String, Object> readAttributes(String attributes,
			LinkOption... options) throws UncheckedIOException {
		try {
			return Files.readAttributes(this.getPath(), attributes, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		try {
			this.path = Paths.get(new URI(in.readUTF()));
		} catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}

	public BufferedImage readImage() throws UncheckedIOException {
		try {
			return ImageIO.read(this.newInputStream());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath readSymbolicLink() throws UncheckedIOException {
		try {
			return new FilePath(Files.readSymbolicLink(this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#register(java.nio.file.WatchService,
	 *      java.nio.file.WatchEvent.Kind[])
	 */
	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events)
			throws UncheckedIOException {
		try {
			return this.getPath().register(watcher, events);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#register(java.nio.file.WatchService,
	 *      java.nio.file.WatchEvent.Kind[],
	 *      java.nio.file.WatchEvent.Modifier[])
	 */
	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events,
			Modifier... modifiers) throws UncheckedIOException {
		try {
			return this.getPath().register(watcher, events, modifiers);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#relativize(java.nio.file.Path)
	 */
	@Override
	public Path relativize(Path other) {
		return new FilePath(this.getPath().relativize(FilePath.getPath(other)));
	}

	/**
	 * @see java.nio.file.Path#resolve(java.nio.file.Path)
	 */
	@Override
	public Path resolve(Path other) {
		return this.child(other);
	}

	/**
	 * @see java.nio.file.Path#resolve(java.lang.String)
	 */
	@Override
	public Path resolve(String other) {
		return this.resolvePath(other);
	}

	public FilePath resolvePath(String other) {
		return this.child(other);
	}

	/**
	 * @see java.nio.file.Path#resolveSibling(java.nio.file.Path)
	 */
	@Override
	public Path resolveSibling(Path other) {
		return new FilePath(this.getPath().resolveSibling(
				FilePath.getPath(other)));
	}

	/**
	 * @see java.nio.file.Path#resolveSibling(java.lang.String)
	 */
	@Override
	public Path resolveSibling(String other) {
		return resolveSiblingPath(other);
	}

	private Path resolveSiblingPath(String other) {
		return new FilePath(this.getPath().resolveSibling(other));
	}

	public FilePath setAttribute(String attribute, Object value,
			LinkOption... options) throws UncheckedIOException {
		try {
			return new FilePath(Files.setAttribute(this.getPath(), attribute,
					value, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean setExecutable(boolean executable)
			throws UncheckedIOException {
		try {
			Files.setAttribute(this, null, executable);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	public boolean setExecutable(boolean executable, boolean ownerOnly)
			throws UncheckedIOException {
		try {
			Files.setAttribute(this, null, executable);
			Files.setAttribute(this, null, ownerOnly);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	public FilePath setLastModifiedTime(FileTime time)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.setLastModifiedTime(this.getPath(), time));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath setOwner(UserPrincipal owner) throws UncheckedIOException {
		try {
			return new FilePath(Files.setOwner(this.getPath(), owner));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath setPosixFilePermissions(Set<PosixFilePermission> perms)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.setPosixFilePermissions(this.getPath(),
					perms));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean setReadable(boolean readable) {
		return this.toFile().setReadable(readable);
	}

	public boolean setReadable(boolean readable, boolean ownerOnly) {
		return this.toFile().setReadable(readable, ownerOnly);
	}

	public boolean setReadOnly() {
		return this.toFile().setReadOnly();
	}

	public boolean setWritable(boolean writable) {
		return this.toFile().setWritable(writable);
	}

	public boolean setWritable(boolean writable, boolean ownerOnly) {
		return this.toFile().setWritable(writable, ownerOnly);
	}

	public List<FilePath> siblings() {
		List<FilePath> siblings = new ArrayList<FilePath>();
		if (this.getParent() != null) {
			for (FilePath child : this.getChildren()) {
				if (!child.equals(this.getPath())) {
					siblings.add(child);
				}
			}
		}
		return siblings;
	}

	/**
	 * @see java.nio.file.Path#startsWith(java.nio.file.Path)
	 */
	@Override
	public boolean startsWith(Path other) {
		return this.getPath().startsWith(other);
	}

	/**
	 * @see java.nio.file.Path#startsWith(java.lang.String)
	 */
	@Override
	public boolean startsWith(String other) {
		return this.getPath().startsWith(other);
	}

	/**
	 * @see java.nio.file.Path#subpath(int, int)
	 */
	@Override
	public Path subpath(int beginIndex, int endIndex) {
		return new FilePath(this.getPath().subpath(beginIndex, endIndex));
	}

	/**
	 * @see java.nio.file.Path#toAbsolutePath()
	 */
	@Override
	public Path toAbsolutePath() {
		return new FilePath(this.getPath().toAbsolutePath());
	}

	/**
	 * @see java.nio.file.Path#toFile()
	 */
	@Override
	public File toFile() {
		return this.getPath().toFile();
	}

	/**
	 * @see java.nio.file.Path#toRealPath(java.nio.file.LinkOption[])
	 */
	@Override
	public Path toRealPath(LinkOption... options) throws UncheckedIOException {
		try {
			return new FilePath(this.getPath().toRealPath(options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getPath().toString();
	}

	/**
	 * @see java.nio.file.Path#toUri()
	 */
	@Override
	public URI toUri() {
		return this.getPath().toUri();
	}

	public URL toUrl() {
		try {
			return this.toUri().toURL();
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public FilePath walkDirectories(final FileVisit visitor) {
		return this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {
				FileVisitResult preVisitDirectory = super.preVisitDirectory(
						dir, attrs);
				visitor.visit(new FilePath(dir), attrs);
				return preVisitDirectory;
			}
		});
	}

	public FilePath walkFiles(final FileVisit visitor) {
		return this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				FileVisitResult visitFile = super.visitFile(file, attrs);
				visitor.visit(new FilePath(file), attrs);
				return visitFile;
			}
		});
	}

	public FilePath walkFileTree(FileVisitor<? super Path> visitor)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.walkFileTree(this.getPath(), visitor));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath walkFileTree(Set<FileVisitOption> options, int maxDepth,
			FileVisitor<? super Path> visitor) throws UncheckedIOException {
		try {
			return new FilePath(Files.walkFileTree(this.getPath(), options,
					maxDepth, visitor));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath writeNew(byte... bytes) throws UncheckedIOException {
		return write(bytes, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(ByteBuffer bytes) throws UncheckedIOException {
		return write(bytes, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(Iterable<? extends CharSequence> lines,
			Charset charset) throws UncheckedIOException {
		return write(lines, charset, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(Iterable<? extends CharSequence> lines)
			throws UncheckedIOException {
		return writeNew(lines, this.getDefaultCharset());
	}

	public FilePath writeNew(String text, Charset charset)
			throws UncheckedIOException {
		return write(text, charset, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(String text) throws UncheckedIOException {
		return writeNew(text, this.getDefaultCharset());
	}

	public FilePath writeAppend(byte... bytes) throws UncheckedIOException {
		return write(bytes, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(ByteBuffer bytes) throws UncheckedIOException {
		return write(bytes, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(Iterable<? extends CharSequence> lines,
			Charset charset) throws UncheckedIOException {
		return write(lines, charset, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(Iterable<? extends CharSequence> lines)
			throws UncheckedIOException {
		return writeAppend(lines, this.getDefaultCharset());
	}

	public FilePath writeAppend(String text, Charset charset)
			throws UncheckedIOException {
		return write(text, charset, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(String text) throws UncheckedIOException {
		return writeAppend(text, this.getDefaultCharset());
	}

	public FilePath write(byte[] bytes, OpenOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.write(this.getPath(), bytes, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(ByteBuffer bytes, OpenOption... options)
			throws UncheckedIOException {
		try {
			byte[] array;
			if (bytes.limit() == bytes.capacity()) {
				array = bytes.array();
			} else {
				array = new byte[bytes.limit()];
				bytes.get(array, 0, bytes.limit());
			}
			return new FilePath(Files.write(this.getPath(), array, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(Iterable<? extends CharSequence> lines,
			Charset charset, OpenOption... options) throws UncheckedIOException {
		try {
			return new FilePath(Files.write(this.getPath(), lines, charset,
					options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(Iterable<? extends CharSequence> lines,
			OpenOption... options) throws UncheckedIOException {
		return write(lines, this.getDefaultCharset(), options);
	}

	public FilePath write(String text, Charset charset, OpenOption... options)
			throws UncheckedIOException {
		try {
			return new FilePath(Files.write(this.getPath(), text
					.getBytes(charset == null ? this.getDefaultCharset()
							: charset), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(String text, OpenOption... options)
			throws UncheckedIOException {
		return this.write(text, this.getDefaultCharset(), options);
	}

	/**
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.toUri().toString());
	}

	public String getFileNameString() {
		return getFileName().toString();
	}

	public String getSortableFileName() {
		StringBuilder sb = new StringBuilder();
		for (char c : Normalizer
				.normalize(getFileNameString(), Normalizer.Form.NFKD)
				.toUpperCase().toCharArray()) {
			if (('A' <= c) && (c <= 'Z')) {
				sb.append(c);
			} else if (('0' <= c) && (c <= '9')) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public String getCanonicalPath() {
		try {
			return toFile().getCanonicalPath();
		} catch (IOException ex) {
			return normalize().toString();
		}
	}
}
