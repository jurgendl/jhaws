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
import java.lang.reflect.Method;
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
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
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
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.io.FilePath.Filters.DirectoryFilter;
import org.jhaws.common.io.FilePath.Visitors.CopyAllFilesVisitor;
import org.jhaws.common.io.FilePath.Visitors.MoveAllFilesVisitor;
import org.jhaws.common.io.Utils.OSGroup;

/**
 * @since 1.8
 * @see http://andreinc.net/
 */
public class FilePath implements Path, Externalizable {
	public static final String CURRENT_FILE_PATH = ".";

	public static final String PROPERTIES = "properties";

	public static final String XML = "xml";

	protected static ExtensionIconFinder grabber = new org.jhaws.common.io.SystemIcon();

	protected static Random RND = new Random(System.currentTimeMillis());

	/** temp dir */
	public static final String TEMPDIR = System.getProperty("java.io.tmpdir");

	/** desktop */
	public static final String DESKTOPDIR = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

	/** user home dir */
	public static final String USERDIR = System.getProperty("user.home");

	public static final String SIZE_UNIT = "B";

	public static char EXTENSION_SEPERATOR = '.';

	public static final char PATH_SEPERATOR_SYSTEM = File.separatorChar;

	public static String getSystemPathSeperator() {
		return String.valueOf(getSystemPathSeperatorChar());
	}

	public static String getFileExtensionSeperator() {
		return String.valueOf(getFileExtensionSeperatorChar());
	}

	public static char getSystemPathSeperatorChar() {
		return PATH_SEPERATOR_SYSTEM;
	}

	public static char getFileExtensionSeperatorChar() {
		return EXTENSION_SEPERATOR;
	}

	public static void setFileExtensionSeperatorChar(char c) {
		EXTENSION_SEPERATOR = c;
	}

	public static String getCurrentUser() {
		// http://stackoverflow.com/questions/797549/get-login-username-in-java
		String osName = System.getProperty("os.name").toLowerCase();
		String className = null;
		if (osName.contains("windows")) {
			className = "com.sun.security.auth.module.NTSystem";
		} else if (osName.contains("linux")) {
			className = "com.sun.security.auth.module.UnixSystem";
		} else if (osName.contains("solaris") || osName.contains("sunos")) {
			className = "com.sun.security.auth.module.SolarisSystem";
		}
		try {
			if (className != null) {
				Class<?> c = Class.forName(className);
				Method method = c.getDeclaredMethod("getUsername");
				Object o = c.newInstance();
				return (String) method.invoke(o);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static class FilePathWatcher {
		protected boolean enabled = true;

		protected final FilePath directory;

		protected WatchKey key;

		protected final Thread thread;

		protected final Consumer<Path> whenAlteredAction;

		public FilePathWatcher(FilePath directory, Consumer<Path> whenAlteredAction) {
			this.whenAlteredAction = whenAlteredAction;
			try {
				this.directory = directory.checkDirectory();
			} catch (AccessDeniedException ex) {
				throw new UncheckedIOException(ex);
			}
			WatchService watcher;
			try {
				watcher = FileSystems.getDefault().newWatchService();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
			key = directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
			thread = new Thread(() -> {
				System.out.println("FilePathWatcher[" + Thread.currentThread() + ":started]");
				while (enabled) {
					try {
						key = watcher.take();
					} catch (InterruptedException ex) {
						//
					}
					for (WatchEvent<?> event : key.pollEvents()) {
						Object o = event.context();
						if (o instanceof Path) {
							FilePathWatcher.this.whenAlteredAction.accept(Path.class.cast(o));
						}
					}
					key.reset();
				}
				System.out.println("FilePathWatcher[" + Thread.currentThread() + ":stopped]");
			});
			thread.setDaemon(true);
			thread.start();
		}

		public void stop() {
			enabled = false;
			thread.interrupt();
		}
	}

	public abstract static class Comparators implements Comparator<FilePath>, Serializable {
		private static final long serialVersionUID = 8900331112954086720L;

		public static class LastModifiedTimeComparator extends Comparators {
			private static final long serialVersionUID = 6476737426344812503L;

			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getLastModifiedTime(), o2.getLastModifiedTime()).toComparison();
			}
		}

		public static class SizeComparator extends Comparators {
			private static final long serialVersionUID = 1515835061656115678L;

			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getFileSize(), o2.getFileSize()).toComparison();
			}
		}
	}

	public static class Filters implements DirectoryStream.Filter<Path>, Serializable {
		private static final long serialVersionUID = 8537116661747478884L;

		public static class ExtensionFilter extends Filters implements Iterable<String> {
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
				String extension = of(entry).getExtension();
				return extension == null ? false : ext.contains(extension.toLowerCase());
			}

			public List<String> getExt() {
				return ext;
			}

			public void setExt(List<String> ext) {
				this.ext = ext;
			}

			public Filters add(String _ext) {
				this.ext.add(_ext);
				return this;
			}

			public Filters remove(String _ext) {
				this.ext.remove(_ext);
				return this;
			}

			public Filters add(Filters.ExtensionFilter otherFilter) {
				this.ext.addAll(otherFilter.ext);
				return this;
			}

			@Override
			public Iterator<String> iterator() {
				return ext.iterator();
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
				super("flv", "webm", "mp4", "m4v", "mpg", "mpeg", "mpe", "mpv", "wmv", "avi", "mov", "qt", "asf", "rm", "divx", "mkv");
			}
		}

		public static class Html5VideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = -7433598725949137242L;

			public Html5VideoFilter() {
				super("flv", "webm", "mp4");
			}
		}

		public static class QuickTimeVideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = -2262172528663217508L;

			public QuickTimeVideoFilter() {
				super("mov", "3gp", "3g2", "m2v");
			}
		}

		public static class FlashVideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = 4338488348271169459L;

			public FlashVideoFilter() {
				super("flv");
			}
		}

		public static class ShockwaveVideoFilter extends Filters.ExtensionFilter {
			private static final long serialVersionUID = 5719384120325522164L;

			public ShockwaveVideoFilter() {
				super("sfw");
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
				return Files.isDirectory(getPath(entry));
			}
		}

		public static class FileFilter extends Filters {
			private static final long serialVersionUID = 6174016893380146941L;

			@Override
			public boolean accept(Path entry) {
				return Files.isRegularFile(getPath(entry));
			}
		}

		protected DirectoryStream.Filter<? super Path>[] filters;

		protected boolean and;

		protected boolean not;

		@SuppressWarnings("unchecked")
		@SafeVarargs
		public Filters(boolean and, boolean not, DirectoryStream.Filter<? super Path>... filters) {
			this.filters = filters.length == 0 ? new DirectoryStream.Filter[] { this } : filters;
			this.and = and;
			this.not = not;
		}

		@SafeVarargs
		public Filters(boolean and, DirectoryStream.Filter<? super Path>... filters) {
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
					if (!this.and /* or */ && filter.accept(entry)) {
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

		public static class FileByteIterator implements Iterator<Byte>, Closeable {
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
				try (BufferedInputStream br = this.in()) {
					if (br != null) {
						br.close();
					}
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

		public static class FileLineIterator implements Iterator<String>, Closeable {
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
				try (BufferedReader br = this.in()) {
					if (br != null) {
						br.close();
					}
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
				try (BufferedReader br = this.in()) {
					if (br != null) {
						br.close();
					}
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

	public static class Visitors extends SimpleFileVisitor<Path> implements Serializable {
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
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (this.ifExists) {
					Files.deleteIfExists(dir);
				} else {
					Files.delete(dir);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (this.ifExists) {
					Files.deleteIfExists(file);
				} else {
					Files.delete(file);
				}
				return FileVisitResult.CONTINUE;
			}
		}

		public static class CopyAllFilesVisitor extends MoveOrCopyAllFilesVisitor {
			private static final long serialVersionUID = -2602269065839902682L;

			public CopyAllFilesVisitor(Path source, Path target) {
				super(false, source, target);
			}
		}

		public static class MoveAllFilesVisitor extends MoveOrCopyAllFilesVisitor {
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

			public MoveOrCopyAllFilesVisitor(boolean move, Path source, Path target) {
				this.move = move;
				this.source = of(source);
				this.target = of(target);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (move)
					try {
						Files.delete(dir);
					} catch (java.nio.file.DirectoryNotEmptyException ex) {
						for (Object o : of(dir).listFiles()) {
							System.out.println(o);
						}
						for (Object o : of(dir).listDirectories()) {
							System.out.println(o);
						}
						throw ex;
					}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				FilePath targetPath = of(this.target.resolve(this.source.relativize(file)));
				targetPath.getParentPath().createDirectories();
				if (move)
					targetPath.moveFrom(file, StandardCopyOption.REPLACE_EXISTING);
				else
					targetPath.copyFrom(file, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		}
	}

	public static FilePath createDefaultTempDirectory(String prefix, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempDirectory(prefix, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static FilePath createDefaultTempFile(FileAttribute<?>... attrs) {
		return createDefaultTempFile(System.currentTimeMillis() + "-" + RND.nextLong(), null, attrs);
	}

	public static FilePath createDefaultTempFile(String extension, FileAttribute<?>... attrs) {
		return createDefaultTempFile(null, extension, attrs);
	}

	public static FilePath createDefaultTempFile(String prefix, String extension, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempFile(prefix == null ? null : prefix + "-", extension == null ? "" : getFileExtensionSeperator() + extension, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static String getHumanReadableByteCount(long bytes, int maxDigits) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " " + SIZE_UNIT;
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		DecimalFormat df = DecimalFormat.class.cast(NumberFormat.getNumberInstance());
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(maxDigits);
		return df.format(bytes / Math.pow(unit, exp)) + " " + "KMGTPE".charAt(exp - 1) + "i" + SIZE_UNIT;
	}

	public static FilePath getDesktopDirectory() {
		return new FilePath(DESKTOPDIR);
	}

	public static String globExtension(String ext) {
		return "*." + ext;
	}

	public static List<FilePath> getDrives() {
		List<FilePath> drives = new ArrayList<>();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// windows
		if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) {
			String systemDriveString = System.getProperty("user.home").substring(0, 2) + getSystemPathSeperator();
			File systemDriveFile = new File(systemDriveString);
			File parentSystemDrive = fsv.getParentDirectory(systemDriveFile);
			for (File f : parentSystemDrive.listFiles()) {
				try {
					if (fsv.isFileSystem(f) && fsv.isDrive(f) && fsv.isTraversable(f) && !fsv.isFloppyDrive(f) && (f.listFiles().length > 0)) {
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
		int p = fileName.lastIndexOf(getFileExtensionSeperator());
		if (p == -1) {
			return null;
		}
		return fileName.substring(p + 1);
	}

	public static Path getPath(Path p) {
		while (p instanceof FilePath) {
			p = FilePath.class.cast(p).path;
		}
		return p;
	}

	public static String getShortFileName(Path path) {
		String fileName = path.getFileName().toString();
		return getShortFileName(fileName);
	}

	public static String getShortFileName(String fileName) {
		if (fileName.contains(getSystemPathSeperator())) {
			fileName = fileName.substring(fileName.lastIndexOf(getSystemPathSeperator()) + 1);
		}
		int p = fileName.lastIndexOf(getFileExtensionSeperator());
		if (p == -1) {
			return fileName;
		}
		return fileName.substring(0, p);
	}

	public static FilePath getTempDirectory() {
		return new FilePath(TEMPDIR);
	}

	public static FilePath getUserHomeDirectory() {
		return new FilePath(USERDIR);
	}

	/**
	 * convers a filename to a legal filename for given operating system, too long parts are chopped, illegal characters are replaced by the character <i>_</i> , a missing
	 * extensions is adapted to extension <i>ext</i>
	 *
	 * @param filename
	 *            : String : current name
	 * @param os
	 *            : int : operating system
	 *
	 * @return : String : converted name
	 */
	public static FilePath legalize(String filename, final OSGroup os) {
		filename = new FilePath(filename).getName();
		while (filename.substring(0, 1).compareTo(getFileExtensionSeperator()) == 0) {
			filename = filename.substring(1, filename.length());
		}
		char[] c = new char[0];
		if (os == OSGroup.Dos) {
			String[] parts = filename.split("\\" + getFileExtensionSeperator());
			if (parts.length == 1) {
				if (filename.substring(filename.length() - 1, filename.length()).compareTo(getFileExtensionSeperator()) != 0) {
					filename = filename + getFileExtensionSeperator();
				}

				filename = filename + "ext";
			}
			if (parts.length > 2) {
				filename = parts[0] + getFileExtensionSeperator() + parts[parts.length - 1];
			}
			parts = filename.split("\\" + getFileExtensionSeperator());
			if (parts[0].length() > 8) {
				parts[0] = parts[0].substring(0, 8);
			}
			if (parts[1].length() > 3) {
				parts[1] = parts[1].substring(0, 3);
			}
			filename = parts[0] + getFileExtensionSeperator() + parts[1];
			c = Utils.legal(os);
		}
		if (os == OSGroup.Windows) {
			c = Utils.legal(os);
		}
		if (os == OSGroup.Nix) {
			c = Utils.legal(os);
		}
		if (c.length == 0) {
			throw new UncheckedIOException(new IOException("characters for OS not found"));
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

	protected static FilePath newFileIndex(Path parent, String outFileName, String extension) {
		return newFileIndex(parent, outFileName, "_", "0000", extension);
	}

	/**
	 * if file does not exists, return it<br>
	 * if file exists and does not end on _9999 (any number), adds _0000 and does index checking<br>
	 * if file exists and does end on _9999 (any number), and does index checking<br>
	 * <br>
	 * index checking:<br>
	 * if file exists, return it<br>
	 * if file does not exists, adds 1 to the index (_0000 goes to _0001) and does further index again
	 *
	 * @param parent
	 *            : String : the location (path only) of the target file
	 * @param outFileName
	 *            : String : the name of the target file (without extension and . before extension)
	 * @param sep
	 *            : String : characters sperating filename from index (example: _ )
	 * @param format
	 *            : String : number of positions character 0 (example: 0000 )
	 * @param extension
	 *            : String : the extension of the target file (without . before extension), see class constants FORMAT... for possibilities
	 *
	 * @return : IOFile : new indexed File
	 */
	protected static FilePath newFileIndex(Path parent, String outFileName, String sep, String format, String extension) {
		String SEPARATOR = sep;
		String FORMAT = sep + format;
		FilePath file = StringUtils.isBlank(extension) ? new FilePath(parent, outFileName) : new FilePath(parent, outFileName + getFileExtensionSeperator() + extension);
		if (file.exists()) {
			if (outFileName.length() <= FORMAT.length()) {
				outFileName = outFileName + FORMAT;
				if (StringUtils.isBlank(extension)) {
					file = new FilePath(parent, outFileName);
				} else {
					file = new FilePath(parent, outFileName + getFileExtensionSeperator() + extension);
				}
			} else {
				String ch = outFileName.substring(outFileName.length() - FORMAT.length(), (outFileName.length() - FORMAT.length()) + SEPARATOR.length());
				String nr = outFileName.substring((outFileName.length() - FORMAT.length()) + SEPARATOR.length(), outFileName.length());
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
						file = new FilePath(parent, outFileName + getFileExtensionSeperator() + extension);
					}
				}
			}
		}
		int ind = 0;
		while (file.exists()) {
			StringBuilder indStringSB = new StringBuilder(16);
			int nr0 = FORMAT.length() - SEPARATOR.length() - String.valueOf(ind).length();
			for (int i = 0; i < nr0; i++) {
				indStringSB.append("0");
			}
			indStringSB.append(String.valueOf(ind));
			if (extension.equals("")) {
				file = new FilePath(parent, outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR + indStringSB.toString());
			} else {
				file = new FilePath(parent,
						outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR + indStringSB.toString() + getFileExtensionSeperator() + extension);
			}
			ind++;
		}
		return file;
	}

	public static FilePath of(Path path) {
		return path instanceof FilePath ? FilePath.class.cast(path) : new FilePath(path);
	}

	public static FilePath of(File file) {
		return new FilePath(file);
	}

	public static FilePath of(URI uri) {
		return new FilePath(uri);
	}

	public static FilePath of(URL url) {
		return new FilePath(url);
	}

	public static FilePath of(Class<?> c, String resource) {
		return new FilePath(c, resource);
	}

	/**
	 * @see http://stackoverflow.com/questions/15713119/java-nio-file-path-for-a -classpath-resource
	 */
	public static Path path(URI uri) {
		String scheme = uri.getScheme();
		if (scheme.equals("file")) {
			return Paths.get(uri);
		}
		if (!scheme.equals("jar")) {
			throw new UnsupportedOperationException("Cannot convert to Path: " + uri);
		}
		String s = uri.toString();
		int separator = s.indexOf("!/");
		String entryName = s.substring(separator + 2);
		URI fileURI = URI.create(s.substring(0, separator));
		FileSystem fs;
		try {
			fs = FileSystems.newFileSystem(fileURI, Collections.<String, Object>emptyMap());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return fs.getPath(entryName);
	}

	protected static <T> T notNull(T o) {
		if (o == null)
			throw new UncheckedIOException(new IOException(new NullPointerException()));
		return o;
	}

	protected transient Path path;

	public FilePath() {
		this.path = Paths.get(CURRENT_FILE_PATH).toAbsolutePath().normalize();
	}

	public FilePath(Class<?> root, String relativePath) throws UncheckedIOException {
		this(root.getClassLoader().getResource(
				(root.getPackage() == null ? "" : root.getPackage().getName().replace(getFileExtensionSeperatorChar(), '/') + (relativePath.startsWith("/") ? "" : '/'))
						+ relativePath));
	}

	public FilePath(File file) {
		this.path = Paths.get(notNull(file).toURI());
	}

	public FilePath(File dir, String file) {
		this.path = Paths.get(notNull(dir).toURI()).resolve(notNull(file));
	}

	public FilePath(Path path) {
		this.path = getPath(notNull(path));
	}

	public FilePath(Path dir, String file) {
		this.path = notNull(dir).resolve(notNull(file));
	}

	public FilePath(String dir, String... file) {
		this.path = Paths.get(notNull(dir), notNull(file));
	}

	public FilePath(URI uri) {
		this.path = path(notNull(uri));
	}

	public FilePath(URL url) {
		try {
			this.path = path(notNull(url).toURI());
		} catch (URISyntaxException ex) {
			throw new UncheckedIOException(new IOException(ex));
		}
	}

	public FilePath addExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getFullFileName() + getFileExtensionSeperator() + extension)
				: new FilePath(this.getParent(), this.getFullFileName() + getFileExtensionSeperator() + extension);
	}

	public long adler32() {
		return this.checksum(new Adler32());
	}

	public long getAdler32() {
		return adler32();
	}

	public Iterators.FileByteIterator bytes() {
		return new Iterators.FileByteIterator(this);
	}

	public FilePath changeExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getShortFileName() + getFileExtensionSeperator() + extension)
				: new FilePath(this.getParent(), this.getShortFileName() + getFileExtensionSeperator() + extension);
	}

	public FilePath appendExtension(String extension) {
		return this.getParent() == null ? new FilePath(this.getFileNameString() + getFileExtensionSeperator() + extension)
				: new FilePath(this.getParent(), this.getFileNameString() + getFileExtensionSeperator() + extension);
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

	public long checksum(Checksum checksum) {
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
		return new FilePath(this.getPath().resolve(getPath(other)));
	}

	public FilePath child(String other) {
		return new FilePath(this.getPath().resolve(other));
	}

	public FilePath dir(Path other) {
		try {
			return child(other).checkDirectory();
		} catch (AccessDeniedException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath dir(String other) {
		try {
			return child(other).checkDirectory();
		} catch (AccessDeniedException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath file(Path other) {
		try {
			return child(other).checkFile();
		} catch (AccessDeniedException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath file(String other) {
		try {
			return child(other).checkFile();
		} catch (AccessDeniedException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 *
	 * @see java.nio.file.Path#compareTo(java.nio.file.Path)
	 */
	@Override
	public int compareTo(Path other) {
		return this.getPath().compareTo(getPath(other));
	}

	public long copyFrom(InputStream in, CopyOption... options) {
		try {
			return Files.copy(in, this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath copyFrom(Path source, CopyOption... options) {
		try {
			return new FilePath(Files.copy(getPath(source), getPath(), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public long copyTo(OutputStream out) {
		try {
			return Files.copy(this.getPath(), out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath copyTo(Path target) {
		return this.copyTo(getPath(target), StandardCopyOption.REPLACE_EXISTING);
	}

	public FilePath copyTo(Path target, CopyOption... options) {
		try {
			return new FilePath(Files.copy(this.getPath(), getPath(target), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public long getCrc32() {
		return crc32();
	}

	public long crc32() {
		return this.checksum(new CRC32());
	}

	public FilePath createDirectories(FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createDirectories(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createDirectory(FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createDirectory(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createDirectoryIfNotExists(FileAttribute<?>... attrs) {
		if (this.exists() && this.isDirectory()) {
			return this;
		}
		if (this.exists()) {
			throw new UncheckedIOException(new IOException("file is not a directory"));
		}
		return this.createDirectory(attrs);
	}

	public FilePath createFile(FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createFile(this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createFileIfNotExists(FileAttribute<?>... attrs) {
		if (this.exists() && this.isFile()) {
			return this;
		}
		return this.createFile(attrs);
	}

	public FilePath createLinkFrom(Path existing) {
		try {
			return new FilePath(Files.createLink(this.getPath(), existing));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createLinkTo(Path link) {
		try {
			return new FilePath(Files.createLink(link, this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkFrom(Path link, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createSymbolicLink(link, this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkTo(Path target, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createSymbolicLink(this.getPath(), target, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createTempDirectory(String prefix, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempDirectory(this.getPath(), prefix, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createTempFile(String prefix, FileAttribute<?>... attrs) {
		return this.createTempFile(prefix, null, attrs);
	}

	public FilePath createTempFile(String prefix, String extension, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempFile(this.getPath(), prefix + "-", extension == null ? "" : getFileExtensionSeperator() + extension, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath delete() {
		try {
			Files.delete(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath deleteAll() {
		return this.walkFileTree(new Visitors.DeleteAllFilesVisitor(false));
	}

	public boolean deleteAllIfExists() {
		if (!this.exists()) {
			return false;
		}
		this.walkFileTree(new Visitors.DeleteAllFilesVisitor(true));
		return true;
	}

	public Collection<FilePath>[] deleteDuplicates() {
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
			return "FindDoublesMeta [file=" + file + ", length=" + length + ", check=" + check + "]";
		}
	}

	public static class FindDuplicateData {
		private final List<FindDuplicateMeta> files = new ArrayList<>();

		private final Map<String, List<FilePath>> duplicates = new HashMap<>();

		public boolean add(FilePath file) {
			FindDuplicateMeta meta = new FindDuplicateMeta(file);
			FindDuplicateMeta duplicate = files.stream().filter(meta::naturalEquals).findFirst().orElse(null);
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
			return duplicates.values().toArray(new Collection[duplicates.size()]);
		}
	}

	public Collection<FilePath>[] deleteDuplicates(boolean doDelete) {
		FindDuplicateData data = new FindDuplicateData();
		try {
			Files.walkFileTree(this.getPath(), new HashSet<FileVisitOption>(), 1, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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

	public boolean deleteIfExists() {
		try {
			return Files.deleteIfExists(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * downloads a file from the web to a local file when it does not exists or is older, binary copy
	 *
	 * @param urlSourceFile
	 *            : URL : file on the web
	 *
	 * @return : boolean : file downloaded or not
	 */
	public boolean download(URL urlSourceFile) {
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
		try (InputStream in = new BufferedInputStream(urlSourceFile.openStream()); OutputStream out = this.newBufferedOutputStream()) {
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

	public boolean equal(Path file) {
		FilePath filePath = of(file);
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
	public boolean equal(Path file, long limit) {
		FilePath otherPath = of(file);
		if (this.notExists() || otherPath.notExists() || this.isDirectory() || this.isDirectory()) {
			return false;
		}
		long size = this.getFileSize();
		long otherSize = otherPath.getFileSize();
		if (size != otherSize) {
			return false;
		}
		long compareSize = Math.min(limit, size);
		long comparedSize = 0l;
		try (Iterators.FileByteIterator buffer = this.bytes(); Iterators.FileByteIterator otherBuffer = otherPath.bytes()) {
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
		return this.getPath().equals(getPath(Path.class.cast(other)));
	}

	public boolean exists(LinkOption... options) {
		return Files.exists(this.getPath(), options);
	}

	/**
	 * flattens this directory, copies all files in all subdirectories to this directory, deletes doubles, rename if file already exists and isn't the same, delete all
	 * subdirectories afterwards
	 */
	public FilePath flatten() {
		final FilePath root = this;
		this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
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
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (!root.equals(file.getParent())) {
					new FilePath(file).moveTo(new FilePath(root, file.getFileName().toString()).newFileIndex());
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return this;
	}

	public String getAbsolutePath() {
		return this.toAbsolutePath().toString();
	}

	public Object getAttribute(String attribute, LinkOption... options) {
		try {
			return Files.getAttribute(this.getPath(), attribute, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public List<FilePath> getChildren() {
		return streamChildren().map(c -> new FilePath(c)).collect(Collectors.toList());
	}

	public String getHumanReadableByteCount() {
		return getHumanReadableByteCount(getFileSize(), 3);
	}

	public Charset getDefaultCharset() {
		return Charset.defaultCharset();
	}

	public String getExtension() {
		return getExtension(this.getPath());
	}

	public <V extends FileAttributeView> V getFileAttributeView(Class<V> type, LinkOption... options) {
		return Files.getFileAttributeView(this.getPath(), type, options);
	}

	public BasicFileAttributeView getBasicFileAttributeView(LinkOption... options) {
		return getFileAttributeView(BasicFileAttributeView.class, options);
	}

	public FileOwnerAttributeView getFileOwnerAttributeView(LinkOption... options) {
		return getFileAttributeView(FileOwnerAttributeView.class, options);
	}

	/**
	 * @see java.nio.file.Path#getFileName()
	 */
	@Override
	public Path getFileName() {
		return new FilePath(this.getPath().getFileName());
	}

	public long getFileSize() {
		try {
			return Files.size(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FileStore getFileStore() {
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

	public long getFolderSize() {
		final AtomicLong size = new AtomicLong(0);
		this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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
		return grabber.getLargeIcon(this);
	}

	public BasicFileAttributes getAttributes() {
		try {
			return Files.readAttributes(getPath(), BasicFileAttributes.class);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FileTime getLastAccessTime() {
		return getAttributes().lastAccessTime();
	}

	public FileTime getCreationTime() {
		return getAttributes().creationTime();
	}

	public FileTime getLastModifiedTime(LinkOption... options) {
		try {
			return Files.getLastModifiedTime(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public int getLineCount() {
		try (LineNumberReader lnr = new LineNumberReader(this.newBufferedReader())) {
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

	public UserPrincipal getOwner(LinkOption... options) {
		try {
			return Files.getOwner(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public String getOwnerName(LinkOption... options) {
		return getOwner(options).getName();
	}

	/**
	 * @see java.nio.file.Path#getParent()
	 */
	@Override
	public Path getParent() {
		return this.getParentPath();
	}

	public FilePath getParentPath() {
		return this.getPath().getParent() == null ? null : new FilePath(this.getPath().getParent());
	}

	public Path getPath() {
		return getPath(this);
	}

	public Set<PosixFilePermission> getPosixFilePermissions(LinkOption... options) {
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
		return getShortFileName(this);
	}

	public Icon getSmallIcon() {
		return grabber.getSmallIcon(this);
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

	public boolean isHidden() {
		try {
			return Files.isHidden(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean isReadable() {
		return Files.isReadable(this.getPath());
	}

	public boolean isSameFile(Path otherPath) {
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

	public Iterators.FileLineIterator lines() {
		return new Iterators.FileLineIterator(this);
		// return Files.lines(this.getPath());
	}

	public List<FilePath> list() {
		return this.list(false);
	}

	public List<FilePath> list(boolean iterate) {
		return this.list(iterate, new Filters.AcceptAllFilter());
	}

	public List<FilePath> list(boolean iterate, DirectoryStream.Filter<? super Path> filter) {
		Deque<FilePath> stack = new ArrayDeque<>();
		List<FilePath> files = new LinkedList<>();
		stack.push(this);
		Filters filterAndDirs = new Filters(false, filter, new Filters.DirectoryFilter());
		while (!stack.isEmpty()) {
			stack.pop().streamChildren(filterAndDirs).forEach(p -> {
				FilePath child = new FilePath(p);
				try {
					if (filter.accept(p)) {
						files.add(child);
					}
				} catch (IOException ex) {
					//
				}
				if (iterate && child.isDirectory()) {
					stack.push(child);
				}
			});
		}
		return files;
	}

	public List<FilePath> list(DirectoryStream.Filter<? super Path> filter) {
		return this.list(false, filter);
	}

	public List<FilePath> listDirectories() {
		return this.listDirectories(false);
	}

	public List<FilePath> listDirectories(boolean iterate) {
		return this.list(iterate, new Filters.DirectoryFilter());
	}

	public List<FilePath> listFiles() {
		return this.listFiles(false);
	}

	public List<FilePath> listFiles(boolean iterate) {
		return this.list(iterate, new Filters.FileFilter());
	}

	public FilePath renameFrom(Path source) {
		return moveFrom(source);
	}

	public FilePath moveFrom(Path source) {
		return this.moveFrom(source, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
	}

	public FilePath renameFrom(Path source, CopyOption... options) {
		return moveFrom(source, options);
	}

	public FilePath moveFrom(Path source, CopyOption... options) {
		try {
			return new FilePath(Files.move(getPath(source), this.getPath(), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath renameAllTo(Path target) {
		return moveAllTo(target);
	}

	public FilePath moveAllTo(Path target) {
		FilePath tp = of(target);
		if (tp.notExists()) {
			return this.moveTo(target, StandardCopyOption.REPLACE_EXISTING);
		}
		return this.walkFileTree(new MoveAllFilesVisitor(this, tp));
	}

	public FilePath renameTo(Path target, CopyOption... options) {
		return moveTo(target, options);
	}

	public FilePath moveTo(Path target, CopyOption... options) {
		try {
			return new FilePath(Files.move(this.getPath(), getPath(target), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath copyAllTo(Path target) {
		FilePath tp = of(target);
		if (tp.notExists()) {
			return this.copyTo(target, StandardCopyOption.REPLACE_EXISTING);
		}
		return this.walkFileTree(new CopyAllFilesVisitor(this, tp));
	}

	public BufferedInputStream newBufferedInputStream(OpenOption... options) {
		return new BufferedInputStream(this.newInputStream(options));
	}

	public BufferedOutputStream newBufferedOutputStream(OpenOption... options) {
		return new BufferedOutputStream(this.newOutputStream(options));
	}

	public BufferedReader newBufferedReader() {
		return this.newBufferedReader(this.getDefaultCharset());
	}

	public BufferedReader newBufferedReader(Charset charset) {
		try {
			return Files.newBufferedReader(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BufferedWriter newBufferedWriter(Charset charset, OpenOption... options) {
		try {
			return Files.newBufferedWriter(this.getPath(), charset, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BufferedWriter newBufferedWriter(OpenOption... options) {
		return this.newBufferedWriter(this.getDefaultCharset(), options);
	}

	public SeekableByteChannel newReadableByteChannel() {
		return newByteChannel(StandardOpenOption.READ);
	}

	public SeekableByteChannel newWritableByteChannelRead() {
		return newByteChannel(StandardOpenOption.WRITE);
	}

	public SeekableByteChannel newByteChannel(OpenOption... options) {
		try {
			return Files.newByteChannel(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public SeekableByteChannel newByteChannel(Set<? extends OpenOption> options, FileAttribute<?>... attrs) {
		try {
			return Files.newByteChannel(this.getPath(), options, attrs);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<FilePath> streamChildren() {
		try {
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath()).spliterator(), false).map(FilePath::of);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<FilePath> streamChildren(DirectoryStream.Filter<? super Path> filter) {
		try {
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath(), filter).spliterator(), false).map(FilePath::of);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<FilePath> streamChildren(String glob) {
		try {
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath(), glob).spliterator(), false).map(FilePath::of);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * {@link #newFileIndex(String, String, String, String, String)} but with separator set to '_' and format to '0000' and the other parameters derived from given File
	 */
	public FilePath newFileIndex() {
		if (this.notExists()) {
			return this;
		}
		String shortFile = this.getShortFileName();
		String extension = this.getExtension();
		return newFileIndex(new FilePath(this.getParent()), shortFile, extension);
	}

	public InputStream newInputStream(OpenOption... options) {
		try {
			return Files.newInputStream(this.getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public OutputStream newOutputStream(OpenOption... options) {
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

	public FilePath open() {
		try {
			Desktop.getDesktop().open(this.toFile());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public Iterators.PathIterator paths() {
		return new Iterators.PathIterator(this);
	}

	public Iterators.PathIterator pathsToRoot() {
		return new Iterators.PathIterator((this.toAbsolutePath()));

	}

	public String probeContentType() {
		try {
			return Files.probeContentType(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public InputStream read() {
		return this.newInputStream();
	}

	public long read(InputStream in) {
		return this.copyFrom(in);
	}

	public String readAll() {
		return this.readAll(this.getDefaultCharset());
	}

	public String readAll(Charset charset) {
		try {
			return new String(Files.readAllBytes(this.getPath()), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public byte[] readAllBytes() {
		try {
			return Files.readAllBytes(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public String readAllText() {
		return readAllText(getDefaultCharset());
	}

	public String readAllText(Charset charset) {
		try {
			return new String(Files.readAllBytes(this.getPath()), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public ByteBuffer readAllBytesToBuffer() {
		try {
			return ByteBuffer.wrap(Files.readAllBytes(this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public List<String> readAllLines() {
		return this.readAllLines(this.getDefaultCharset());
	}

	public List<String> readAllLines(Charset charset) {
		try {
			return Files.readAllLines(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<String> streamContentString() {
		return this.streamContentString(this.getDefaultCharset());
	}

	public Stream<String> streamContentString(Charset charset) {
		try {
			return Files.lines(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public <A extends BasicFileAttributes> A readAttributes(Class<A> type, LinkOption... options) {
		try {
			return Files.readAttributes(this.getPath(), type, options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Map<String, Object> readAttributes(String attributes, LinkOption... options) {
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
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		try {
			this.path = Paths.get(new URI(in.readUTF()));
		} catch (URISyntaxException ex) {
			throw new IOException(ex);
		}
	}

	public BufferedImage readImage() {
		try {
			return ImageIO.read(this.newInputStream());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath readSymbolicLink() {
		try {
			return new FilePath(Files.readSymbolicLink(this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind[])
	 */
	@Override
	public WatchKey register(WatchService watcher, Kind<?>... events) {
		try {
			return this.getPath().register(watcher, events);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind[], java.nio.file.WatchEvent.Modifier[])
	 */
	@Override
	public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) {
		try {
			return this.getPath().register(watcher, events, modifiers);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePathWatcher watch(Consumer<Path> whenAlteredAction) {
		return new FilePathWatcher(this, whenAlteredAction);
	}

	/**
	 * @see java.nio.file.Path#relativize(java.nio.file.Path)
	 */
	@Override
	public Path relativize(Path other) {
		return new FilePath(this.getPath().relativize(getPath(other)));
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
		return new FilePath(this.getPath().resolveSibling(getPath(other)));
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

	public FilePath setAttribute(String attribute, Object value, LinkOption... options) {
		try {
			return new FilePath(Files.setAttribute(this.getPath(), attribute, value, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean setExecutable(boolean executable) {
		try {
			Files.setAttribute(this, null, executable);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	public boolean setExecutable(boolean executable, boolean ownerOnly) {
		try {
			Files.setAttribute(this, null, executable);
			Files.setAttribute(this, null, ownerOnly);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return true;
	}

	public FilePath setLastAccessTime(FileTime lastAccessTime) {
		// http://stackoverflow.com/questions/9198184/setting-file-creation-timestamp-in-java
		try {
			// lastModifiedTime, lastAccessTime, createTime
			getBasicFileAttributeView().setTimes(null, lastAccessTime, null);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath setCreationTime(FileTime creationTime) {
		// http://stackoverflow.com/questions/9198184/setting-file-creation-timestamp-in-java
		try {
			// lastModifiedTime, lastAccessTime, createTime
			getBasicFileAttributeView().setTimes(creationTime, null, null);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath setLastModifiedTime(FileTime time) {
		try {
			return new FilePath(Files.setLastModifiedTime(this.getPath(), time));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath setOwner(UserPrincipal owner) {
		try {
			// getFileOwnerAttributeView().setOwner(owner);
			return new FilePath(Files.setOwner(this.getPath(), owner));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath setOwner(String owner) {
		try {
			return setOwner(FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(owner));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath setOwnerCurrentUser() {
		return setOwner(getCurrentUser());
	}

	public FilePath setPosixFilePermissions(Set<PosixFilePermission> perms) {
		try {
			return new FilePath(Files.setPosixFilePermissions(this.getPath(), perms));
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
		List<FilePath> siblings = new ArrayList<>();
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
	public Path toRealPath(LinkOption... options) {
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
		return getFilePathString();
	}

	/**
	 * @see java.nio.file.Path#toUri()
	 */
	@Override
	public URI toUri() {
		return this.getPath().toUri();
	}

	public URI toURI() {
		return toUri();
	}

	public URL toUrl() {
		try {
			return this.toUri().toURL();
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public URL toURL() {
		return toUrl();
	}

	public FilePath walkDirectories(final FileVisit visitor) {
		return this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return visitor.visit(new FilePath(dir)) ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
			}
		});
	}

	public FilePath walkAll(final FileVisit visitor) {
		return this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return visitor.visit(new FilePath(file)) ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return visitor.visit(new FilePath(dir)) ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
			}
		});
	}

	public FilePath walkFiles(final FileVisit visitor) {
		return this.walkFileTree(new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return visitor.visit(new FilePath(file)) ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
			}
		});
	}

	public FilePath walkFileTree(FileVisitor<? super Path> visitor) {
		try {
			return new FilePath(Files.walkFileTree(this.getPath(), visitor));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath walkFileTree(Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) {
		try {
			return new FilePath(Files.walkFileTree(this.getPath(), options, maxDepth, visitor));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath writeNew(byte... bytes) {
		return write(bytes, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(ByteBuffer bytes) {
		return write(bytes, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(Iterable<? extends CharSequence> lines, Charset charset) {
		return write(lines, charset, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(Iterable<? extends CharSequence> lines) {
		return writeNew(lines, this.getDefaultCharset());
	}

	public FilePath writeNew(String text, Charset charset) {
		return write(text, charset, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(String text) {
		return writeNew(text, this.getDefaultCharset());
	}

	public FilePath writeAppend(byte... bytes) {
		return write(bytes, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(ByteBuffer bytes) {
		return write(bytes, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(Iterable<? extends CharSequence> lines, Charset charset) {
		return write(lines, charset, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(Iterable<? extends CharSequence> lines) {
		return writeAppend(lines, this.getDefaultCharset());
	}

	public FilePath writeAppend(String text, Charset charset) {
		return write(text, charset, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(CharSequence text) {
		return writeAppend(text, this.getDefaultCharset());
	}

	public FilePath writeAppend(CharSequence text, Charset charset) {
		return writeAppend(text.toString(), charset);
	}

	public FilePath writeAppend(String text) {
		return writeAppend(text, this.getDefaultCharset());
	}

	public FilePath write(byte[] bytes, OpenOption... options) {
		try {
			return new FilePath(Files.write(this.getPath(), bytes, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(ByteBuffer bytes, OpenOption... options) {
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

	public FilePath write(Iterable<? extends CharSequence> lines, Charset charset, OpenOption... options) {
		try {
			return new FilePath(Files.write(this.getPath(), lines, charset, options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(Iterable<? extends CharSequence> lines, OpenOption... options) {
		return write(lines, this.getDefaultCharset(), options);
	}

	public FilePath write(String text, Charset charset, OpenOption... options) {
		try {
			return new FilePath(Files.write(this.getPath(), text.getBytes(charset == null ? this.getDefaultCharset() : charset), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(CharSequence text, OpenOption... options) {
		return this.write(text, this.getDefaultCharset(), options);
	}

	public FilePath write(String text, OpenOption... options) {
		return this.write(text, this.getDefaultCharset(), options);
	}

	public FilePath write(CharSequence text, Charset charSet, OpenOption... options) {
		return this.write(text.toString(), charSet, options);
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

	public String getFilePathString() {
		return getPath().toString();
	}

	public String getSortableFileName() {
		StringBuilder sb = new StringBuilder();
		for (char c : Normalizer.normalize(getFileNameString(), Normalizer.Form.NFKD).toUpperCase().toCharArray()) {
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

	public FilePath deleteEmptyDirectories() {
		if (exists() && isDirectory()) {
			streamChildrenForDeletion(this);
		}
		return this;
	}

	private void streamChildrenForDeletion(Path f) {
		FilePath filePath = of(f);
		if (filePath.isDirectory()) {
			if (filePath.streamChildren().count() == 0) {
				filePath.delete();
			} else {
				filePath.streamChildren(new DirectoryFilter()).forEach(this::streamChildrenForDeletion);
			}
		}
	}

	public FilePath writeProperties(Properties properties, String comment) {
		String ext = getExtension();
		try (OutputStream out = newOutputStream()) {
			if (XML.equalsIgnoreCase(ext)) {
				properties.storeToXML(out, comment);
			} else {
				properties.store(out, comment);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public Properties readProperties() {
		return readProperties(new Properties());
	}

	public Properties readProperties(Properties properties) {
		String ext = getExtension();
		try (InputStream in = newInputStream()) {
			if (XML.equalsIgnoreCase(ext)) {
				properties.loadFromXML(in);
			} else {
				properties.load(in);
			}
			return properties;
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public boolean isEmpty() {
		return (isFile() && getFileSize() == 0) || (isDirectory() && getChildren().size() == 0);
	}

	public FilePath process(Predicate<String> isGroup, UnaryOperator<String> groupBuilder, Predicate<String> acceptGroup, BiConsumer<String, String> whenAccept) {
		return _process(isGroup, groupBuilder == null ? t -> t : groupBuilder, acceptGroup == null ? x -> true : acceptGroup, whenAccept);
	}

	private FilePath _process(Predicate<String> isGroup, UnaryOperator<String> groupBuilder, Predicate<String> acceptGroup, BiConsumer<String, String> whenAccept) {
		String[] group = new String[1];
		readAllLines().stream().filter(StringUtils::isNotBlank).forEach(l -> {
			if (isGroup.test(l)) {
				group[0] = groupBuilder.apply(l);
			} else if (acceptGroup.test(group[0])) {
				whenAccept.accept(group[0], l);
			}
		});
		return this;
	}

	public FilePath write(InputStream binaryStream) {
		try (BufferedInputStream in = new BufferedInputStream(binaryStream); BufferedOutputStream out = newBufferedOutputStream()) {
			IOUtils.copyLarge(in, out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath read(OutputStream binaryStream) {
		try (BufferedOutputStream out = new BufferedOutputStream(binaryStream); BufferedInputStream in = newBufferedInputStream()) {
			IOUtils.copyLarge(in, out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath moveShortFilename(String filename) {
		return renameShortFilename(filename);
	}

	public FilePath renameShortFilename(String filename) {
		return renameFileName(filename + (StringUtils.isBlank(getExtension()) ? "" : getFileExtensionSeperator() + getExtension()));
	}

	public FilePath moveFileName(String filename) {
		return renameFileName(filename);
	}

	public FilePath renameFileName(String filename) {
		return moveTo(getParentPath().child(filename));
	}
}
