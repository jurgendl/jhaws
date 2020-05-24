package org.jhaws.common.io;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.io.FilePath.Filters.DirectoryFilter;
import org.jhaws.common.io.FilePath.Visitors.CopyAllFilesVisitor;
import org.jhaws.common.io.FilePath.Visitors.MoveAllFilesVisitor;
import org.jhaws.common.io.Utils.OSGroup;
import org.jhaws.common.io.win.WinRegistryAlt;
import org.jhaws.common.lang.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 1.8
 * @see http://andreinc.net/
 */
@SuppressWarnings("serial")
public class FilePath implements Path, Externalizable {
	protected static Logger LOGGER = LoggerFactory.getLogger(FilePath.class);

	public static final String CURRENT_FILE_PATH = ".";

	public static final String PROPERTIES = "properties";

	public static final String XML = "xml";

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

	public static LocalDateTime convert(FileTime fileTime) {
		return fileTime == null ? null : LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
	}

	public static FileTime convert(LocalDateTime dateTime) {
		return dateTime == null ? null
				: FileTime.from(dateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())));
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
				Object o = c.getDeclaredConstructor().newInstance();
				return (String) method.invoke(o);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static URL url(String path, Class<?> root, ClassLoader classLoader) {
		return url(path, null, root, new Value<>(classLoader));
	}

	protected static URL url(String path, URL url, Class<?> root, Value<ClassLoader> classLoader) {
		if (path != null && path.startsWith("/")) {
			path = path.substring(1);
		}
		if (root == null) {
			root = FilePath.class;
		}
		if (classLoader.get() == null) {
			classLoader.set(root.getClassLoader());
		}
		if (path != null) {
			if (url == null) {
				url = root.getResource(path);
			} else {
				LOGGER.trace("{}", url);
			}
			if (url == null) {
				url = root.getResource("/" + path);
			} else {
				LOGGER.trace("{}", url);
			}
			if (url == null) {
				url = classLoader.get().getResource("/" + path);
			} else {
				LOGGER.trace("{}", url);
			}
			if (url == null) {
				url = classLoader.get().getResource(path);
			} else {
				LOGGER.trace("{}", url);
			}
			if (url == null) {
				LOGGER.error("url not resolved={}", url);
			} else {
				LOGGER.trace("url={}", url);
			}
		}
		return url;
	}

	public static URI uri(URL url) {
		return uri(url, null);
	}

	protected static URI uri(URL url, URI uri) {
		if (uri == null) {
			if (url == null) {
				throw new UncheckedIOException(new IOException("resource not found: null"));
			}
			try {
				uri = url.toURI();
			} catch (URISyntaxException ex) {
				throw new UncheckedIOException(new IOException("resource not found: " + url, ex));
			}
		}
		return uri;
	}

	/**
	 * path (then class or classloader is required) or url or uri required
	 *
	 * @see http://stackoverflow.com/questions/15713119/java-nio-file-path-for-a-classpath-resource
	 */
	public static Path path(String _path, URL _url, URI _uri, Class<?> _root, ClassLoader _classLoader) {
		Value<ClassLoader> __classLoader = new Value<>(_classLoader);
		_url = url(_path, _url, _root, __classLoader);
		_uri = uri(_url, _uri);
		String scheme = _uri.getScheme();
		if (scheme.equals("file")) {
			return Paths.get(_uri);
		}
		if (!scheme.equals("jar")) {
			throw new UnsupportedOperationException("Cannot convert to Path: " + _uri);
		}
		String s = _uri.toASCIIString();
		int separator = s.indexOf("!/");
		String entryName = s.substring(separator + 2);
		URI fileURI = URI.create(s.substring(0, separator));
		FileSystem fs;
		try {
			try {
				fs = FileSystems.newFileSystem(fileURI, Collections.<String, Object>emptyMap(), __classLoader.get());
			} catch (java.nio.file.FileSystemAlreadyExistsException ex) {
				fs = FileSystems.getFileSystem(fileURI);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return fs.getPath(entryName);
	}

	public static Icon getSystemIcon(final FilePath f) {
		if (f == null) {
			return null;
		}
		String ext = f.getExtension();
		if (!f.isDirectory() && iconMap.containsKey(ext)) {
			return iconMap.get(ext);
		}
		try {
			Icon icon = fsv.getSystemIcon(f.toFile());
			if (!f.isDirectory()) {
				iconMap.put(ext, icon);
			}
			return icon;
		} catch (final NullPointerException ex) {
			if (f.isDirectory()) {
				return UIManager.getIcon("FileView.directoryIcon"); //$NON-NLS-1$
			}
			return UIManager.getIcon("FileView.fileIcon"); //$NON-NLS-1$
		}
	}

	protected static FileSystemView fsv = FileSystemView.getFileSystemView();

	protected static HashMap<String, Icon> iconMap = new HashMap<>();

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
			key = directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
			thread = new Thread(() -> {
				LOGGER.trace("{}", "FilePathWatcher[" + Thread.currentThread() + ":started]");
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
				LOGGER.trace("{}", "FilePathWatcher[" + Thread.currentThread() + ":stopped]");
			});
			thread.setDaemon(true);
			thread.start();
		}

		public void stop() {
			enabled = false;
			thread.interrupt();
		}
	}

	public static class Comparators implements Comparator<FilePath>, Serializable {
		public static class LastModifiedTimeComparator extends Comparators {
			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getLastModifiedTime(), o2.getLastModifiedTime()).toComparison();
			}
		}

		public static class SizeComparator extends Comparators {
			@Override
			public int compare(FilePath o1, FilePath o2) {
				return new CompareToBuilder().append(o1.getFileSize(), o2.getFileSize()).toComparison();
			}
		}

		protected Comparators[] comparators;

		protected boolean invers;

		@SafeVarargs
		public Comparators(boolean invers, Comparators... filters) {
			this.comparators = filters.length == 0 ? new Comparators[] { this } : filters;
			this.invers = invers;
		}

		@SafeVarargs
		public Comparators(Comparators... filters) {
			this(false, filters);
		}

		@Override
		public int compare(FilePath o1, FilePath o2) {
			int index = 0;
			for (Comparators comparator : this.comparators) {
				index = comparator.compare(o1, o1);
				if (index != 0) {
					break;
				}
			}
			return this.invers ? -index : index;
		}

		public void invers(boolean invers) {
			this.invers = invers;
		}

		public void invers() {
			this.invers = true;
		}

		public Comparators and(Comparators comparator) {
			return new Comparators(this, comparator);
		}
	}

	public static class Filters implements DirectoryStream.Filter<Path>, Serializable, Predicate<Path> {
		public static class PredicateDelegate extends Filters {
			protected final Predicate<Path> predicate;

			public PredicateDelegate(Predicate<Path> predicate) {
				super();
				this.predicate = predicate;
			}

			@Override
			public boolean accept(Path entry) {
				return predicate.test(entry);
			}
		}

		public static class ExtensionFilter extends Filters implements Iterable<String> {
			protected List<String> ext;

			public ExtensionFilter() {
				setExt(null);
			}

			public ExtensionFilter(List<String> ext) {
				setExt(ext);
			}

			public ExtensionFilter(String... ext) {
				setExt(Arrays.asList(ext));
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
				this.ext = ext.stream().map(String::toLowerCase).collect(Collectors.toList());
			}

			public Filters add(String _ext) {
				this.ext.add(_ext.toLowerCase());
				return this;
			}

			public Filters remove(String _ext) {
				this.ext.remove(_ext.toLowerCase());
				return this;
			}

			public Filters add(Filters.ExtensionFilter otherFilter) {
				otherFilter.ext.stream().map(String::toLowerCase).forEach(ext::add);
				return this;
			}

			@Override
			public Iterator<String> iterator() {
				return ext.iterator();
			}

			@Override
			public String toString() {
				return getClass().getSimpleName() + ":" + ext;
			}
		}

		public static class WebImageFilter extends Filters.ExtensionFilter {
			public WebImageFilter() {
				super("jpg", "jpeg", "png", "gif", "webp");
			}
		}

		public static class ImageFilter extends Filters.ExtensionFilter {
			public ImageFilter() {
				super("jpg", "jpeg", "bmp", "tiff", "tif", "pix", "png", "gif", "jp2", "tga", "pcx", "pnm", "ppm",
						"pbm", "pgm", "ras", "iff", "raw", "jpe", "wmf", "svg", "jpm", "emf", "rla", "jif", "dpx",
						"dcx", "pic", "ico", "webp");
			}
		}

		public static class VideoFilter extends Filters.ExtensionFilter {
			public VideoFilter() {
				super("flv", "webm", "mp4v", "mp4", "m4v", "mpg", "mpeg", "mpe", "mpv", "wmv", "avi", "mov", "qt",
						"asf", "rm", "divx", "mkv", "ts");
			}
		}

		// https://en.wikipedia.org/wiki/Audio_file_format
		public static class AudioFilter extends Filters.ExtensionFilter {
			public AudioFilter() {
				super("ogg", "oga", "mp3", "m4a", "mpa", "aa", "3gp", "aac", "aax", "act", "aiff", "amr", "ape", "au",
						"wav", "wv", "wma");
			}
		}

		public static class Html5VideoFilter extends Filters.ExtensionFilter {
			public Html5VideoFilter() {
				super("flv", "webm", "mp4");
			}
		}

		public static class QuickTimeVideoFilter extends Filters.ExtensionFilter {
			public QuickTimeVideoFilter() {
				super("mov", "3gp", "3g2", "m2v");
			}
		}

		public static class FlashVideoFilter extends Filters.ExtensionFilter {
			public FlashVideoFilter() {
				super("flv");
			}
		}

		public static class ShockwaveVideoFilter extends Filters.ExtensionFilter {
			public ShockwaveVideoFilter() {
				super("sfw");
			}
		}

		public static class IIOImageFilter extends Filters.ExtensionFilter {
			public IIOImageFilter() {
				super(javax.imageio.ImageIO.getReaderFormatNames());
			}
		}

		public static final class AcceptAllFilter extends Filters {
			@Override
			public boolean accept(Path entry) {
				return true;
			}
		}

		public static class CompatibleFilter extends Filters {
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
			@Override
			public boolean accept(Path entry) {
				return Files.isDirectory(getPath(entry));
			}
		}

		public static class FileFilter extends Filters {
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
		public boolean test(Path t) {
			return accept(t);
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
		public static class DeleteAllFilesVisitor extends Visitors {
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
					Files.delete(dir);
				} else {
					Files.delete(dir);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (this.ifExists) {
					Files.delete(file);
				} else {
					Files.delete(file);
				}
				return FileVisitResult.CONTINUE;
			}
		}

		public static class CopyAllFilesVisitor extends MoveOrCopyAllFilesVisitor {
			public CopyAllFilesVisitor(Path source, Path target) {
				super(false, source, target);
			}
		}

		public static class MoveAllFilesVisitor extends MoveOrCopyAllFilesVisitor {
			public MoveAllFilesVisitor(Path source, Path target) {
				super(true, source, target);
			}
		}

		protected static class MoveOrCopyAllFilesVisitor extends Visitors {
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
							LOGGER.trace("{}", o);
						}
						for (Object o : of(dir).listDirectories()) {
							LOGGER.trace("{}", o);
						}
						throw ex;
					}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				FilePath targetPath = of(this.target.resolve(this.source.relativize(file)));
				targetPath.getParentPath().createDirectory();
				if (move)
					targetPath.moveFrom(file, StandardCopyOption.REPLACE_EXISTING);
				else
					targetPath.copyFrom(file, StandardCopyOption.REPLACE_EXISTING);
				return FileVisitResult.CONTINUE;
			}
		}
	}

	public static FilePath createTempDirectory() {
		return createTempDirectory("" + System.currentTimeMillis());
	}

	public static FilePath createTempDirectory(String prefix, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempDirectory(prefix, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static FilePath createTempFile(FileAttribute<?>... attrs) {
		return createTempFile(System.currentTimeMillis() + "-" + RND.nextLong(), null, attrs);
	}

	public static FilePath createTempFile(String extension, FileAttribute<?>... attrs) {
		return createTempFile(null, extension, attrs);
	}

	public static FilePath createTempFile(String prefix, String extension, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createTempFile(prefix == null ? null : prefix + "-",
					extension == null ? "" : getFileExtensionSeperator() + extension, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public static String getHumanReadableFileSize(long bytes) {
		return getHumanReadableFileSize(bytes, 3);
	}

	public static String getHumanReadableFileSize(long bytes, int maxDigits) {
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
					if (fsv.isFileSystem(f) && fsv.isDrive(f) && fsv.isTraversable(f) && !fsv.isFloppyDrive(f)
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
		Path fileName0 = path.getFileName();
		if (fileName0 == null) {
			return null;
		}
		String fileName = fileName0.toString();
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

	public static String legalize(String filename) {
		return legalize(filename, org.jhaws.common.io.Utils.osgroup);
	}

	/**
	 * convers a filename to a legal filename for given operating system, too
	 * long parts are chopped, illegal characters are replaced by the character
	 * <i>_</i> , a missing extensions is adapted to extension <i>ext</i>
	 *
	 * @param filename : String : current name
	 * @param os : int : operating system
	 *
	 * @return : String : converted name
	 */
	public static String legalize(String filename, final OSGroup os) {
		StringBuilder sb = new StringBuilder();
		char[] legal = Utils.legal(os);
		for (char cc : filename.toCharArray()) {
			for (char c : legal) {
				if (c == cc) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	protected static FilePath newFileIndex(Path parent, String outFileName, String extension) {
		return newFileIndex(parent, outFileName, "_", "0000", extension);
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
	 * @param parent : String : the location (path only) of the target file
	 * @param outFileName : String : the name of the target file (without
	 * extension and . before extension)
	 * @param sep : String : characters sperating filename from index (example:
	 * _ )
	 * @param format : String : number of positions character 0 (example: 0000 )
	 * @param extension : String : the extension of the target file (without .
	 * before extension), see class constants FORMAT... for possibilities
	 *
	 * @return : IOFile : new indexed File
	 */
	protected static FilePath newFileIndex(Path parent, String outFileName, String sep, String format,
			String extension) {
		String SEPARATOR = sep;
		String FORMAT = sep + format;
		FilePath file = StringUtils.isBlank(extension) ? new FilePath(parent, outFileName)
				: new FilePath(parent, outFileName + getFileExtensionSeperator() + extension);
		if (file.exists()) {
			if (outFileName.length() <= FORMAT.length()) {
				outFileName = outFileName + FORMAT;
				if (StringUtils.isBlank(extension)) {
					file = new FilePath(parent, outFileName);
				} else {
					file = new FilePath(parent, outFileName + getFileExtensionSeperator() + extension);
				}
			} else {
				String ch = outFileName.substring(outFileName.length() - FORMAT.length(),
						(outFileName.length() - FORMAT.length()) + SEPARATOR.length());
				String nr = outFileName.substring((outFileName.length() - FORMAT.length()) + SEPARATOR.length(),
						outFileName.length());
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
			if (extension == null || extension.equals("")) {
				file = new FilePath(parent, outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR
						+ indStringSB.toString());
			} else {
				file = new FilePath(parent, outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR
						+ indStringSB.toString() + getFileExtensionSeperator() + extension);
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

	public static FilePath of(ClassLoader classLoader, String relativePath) {
		return new FilePath(classLoader, relativePath);
	}

	public static FilePath of(Class<?> root, String relativePath) {
		return new FilePath(root, relativePath);
	}

	protected static <T> T notNull(T o) {
		if (o == null)
			throw new UncheckedIOException(new IOException(new NullPointerException()));
		return o;
	}

	protected transient Path path;

	protected transient Charset charSet;

	public FilePath() {
		this.path = Paths.get(CURRENT_FILE_PATH).toAbsolutePath().normalize();
	}

	public FilePath(Class<?> root, String relativePath) {
		this(path(relativePath, null, null, root, null));
	}

	public FilePath(ClassLoader classLoader, String relativePath) {
		this(path(relativePath, null, null, null, classLoader));
	}

	public FilePath(URI uri) {
		this(path(null, null, uri, null, null));
	}

	public FilePath(URL url) {
		this(path(null, url, null, null, null));
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
		this.path = file == null || file.length == 0 ? Paths.get(notNull(dir)) : Paths.get(notNull(dir), notNull(file));
	}

	public FilePath addExtension(String extension) {
		if (StringUtils.isBlank(extension))
			return this;
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
		return this.getParent() == null
				? new FilePath(this.getShortFileName() + getFileExtensionSeperator() + extension)
				: new FilePath(this.getParent(), this.getShortFileName() + getFileExtensionSeperator() + extension);
	}

	public FilePath appendExtension(String extension) {
		return this.getParent() == null
				? new FilePath(this.getFileNameString() + getFileExtensionSeperator() + extension)
				: new FilePath(this.getParent(), this.getFileNameString() + getFileExtensionSeperator() + extension);
	}

	public FilePath dropExtension() {
		return new FilePath(getParent(), getShortFileName());
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
			return new FilePath(Files.createLink(this.getPath(), getPath(existing)));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath createLinkTo(Path link) {
		try {
			return new FilePath(Files.createLink(getPath(link), this.getPath()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkFrom(Path link, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createSymbolicLink(getPath(link), this.getPath(), attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Path createSymbolicLinkTo(Path target, FileAttribute<?>... attrs) {
		try {
			return new FilePath(Files.createSymbolicLink(this.getPath(),
					target instanceof FilePath ? FilePath.class.cast(target).toPath() : path, attrs));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Collection<FilePath>[] deleteDuplicates() {
		return deleteDuplicates(true);
	}

	public static class FindDuplicateMeta {
		protected final FilePath file;

		protected final Long length;

		protected Long check;

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
		protected final List<FindDuplicateMeta> files = new ArrayList<>();

		protected final Map<String, List<FilePath>> duplicates = new HashMap<>();

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
	 * @param file : Path : file to compare to
	 * @param limit : int : maximum size when comparing files
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
		return this.getPath().equals(getPath(Path.class.cast(other)));
	}

	public boolean exists(LinkOption... options) {
		return Files.exists(this.getPath(), options);
	}

	public FilePath flatten() {
		return flatten(new ArrayList<>());
	}

	/**
	 * flattens this directory, copies all files in all subdirectories to this
	 * directory, deletes doubles, rename if file already exists and isn't the
	 * same, delete all subdirectories afterwards
	 */
	public FilePath flatten(Collection<FilePath> changed) {
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
					FilePath target = new FilePath(root, file.getFileName().toString()).newFileIndex();
					changed.add(target);
					new FilePath(file).moveTo(target);
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

	public List<FilePath> getFiles() {
		return getChildren();
	}

	public long getFileCount() {
		try {
			return Files.list(getPath()).count();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public String getHumanReadableFileSize() {
		return getHumanReadableFileSize(getFileSize(), 3);
	}

	public String getHumanReadableFileSize(int decimals) {
		return getHumanReadableFileSize(getFileSize(), decimals);
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
	public FilePath getFileName() {
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
		return getSystemIcon(this);
	}

	public BasicFileAttributes getAttributes() {
		try {
			return Files.readAttributes(getPath(), BasicFileAttributes.class);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public LocalDateTime getLastAccessDateTime() {
		return convert(getLastAccessTime());
	}

	public LocalDateTime getCreationDateTime() {
		return convert(getCreationTime());
	}

	public LocalDateTime getLastModifiedDateTime(LinkOption... options) {
		return convert(getLastModifiedTime(options));
	}

	public FileTime getLastAccessTime() {
		try {
			return getAttributes().lastAccessTime();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public FileTime getCreationTime() {
		try {
			return getAttributes().creationTime();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return null;
		}
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

	public Path toPath() {
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

//	@SuppressWarnings("restriction")
//	public Icon getSmallIcon() {
//		try {
//			sun.awt.shell.ShellFolder sf;
//			try {
//				sf = sun.awt.shell.ShellFolder.getShellFolder(toFile());
//			} catch (IOException ex) {
//				throw new RuntimeException(ex);
//			}
//			return new ImageIcon(sf.getIcon(true));
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}

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
	}

	public Stream<String> stream() {
		return stream(getCharSet());
	}

	public Stream<String> stream(Charset charSet) {
		try {
			return Files.lines(this.getPath(), charSet);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public List<FilePath> list() {
		return this.list(false);
	}

	public List<FilePath> list(boolean iterate) {
		return this.list(iterate, new Filters.AcceptAllFilter());
	}

	public List<FilePath> list(boolean iterate, DirectoryStream.Filter<? super Path> filter) {
		if (notExists())
			return Collections.emptyList();
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

	private static FilePath move(Path from, Path to, CopyOption... options) {
		try {
			return new FilePath(Files.move(getPath(from), getPath(to), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath moveTo(Path target, CopyOption... options) {
		FilePath targetPath = new FilePath(target);
		if (this.isFile() && targetPath.isDirectory()) {
			return move(this, targetPath.child(getFileNameString()));
		}
		return move(this, targetPath, options);
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
		return this.newBufferedReader(getCharSet());
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
		return this.newBufferedWriter(getCharSet(), options);
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
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath()).spliterator(), false)
					.map(c -> new FilePath(this, c.getFileName().toString()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<FilePath> streamChildren(DirectoryStream.Filter<? super Path> filter) {
		try {
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath(), filter).spliterator(), false)
					.map(c -> new FilePath(this, c.getFileName().toString()));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<FilePath> streamChildren(String glob) {
		try {
			return StreamSupport.stream(Files.newDirectoryStream(this.getPath(), glob).spliterator(), false)
					.map(c -> new FilePath(this, c.getFileName().toString()));
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

	public FilePath _openOnJava() {
		try {
			Desktop.getDesktop().open(this.toFile());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	/**
	 * on Windows only
	 */
	public FilePath _openOnOS(String... parameters) {
		try {
			Utils.open(toFile(), parameters);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath open(String... parameters) {
		try {
			_openOnOS(parameters);
		} catch (Exception ex) {
			_openOnJava();
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
		return this.readAll(getCharSet());
	}

	public String readAll(Charset charset) {
		try {
			return new String(Files.readAllBytes(this.getPath()), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public byte[] readFully() {
		return readAllBytes();
	}

	public byte[] readAllBytes() {
		try {
			return Files.readAllBytes(this.getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public String readAllText() {
		return readAllText(getCharSet());
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
		return this.readAllLines(getCharSet());
	}

	public List<String> readAllLines(Charset charset) {
		try {
			return Files.readAllLines(this.getPath(), charset);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public Stream<String> streamContentString() {
		return this.streamContentString(getCharSet());
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
	 * @see java.nio.file.Path#register(java.nio.file.WatchService,
	 * java.nio.file.WatchEvent.Kind[])
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
	 * @see java.nio.file.Path#register(java.nio.file.WatchService,
	 * java.nio.file.WatchEvent.Kind[], java.nio.file.WatchEvent.Modifier[])
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

	protected Path resolveSiblingPath(String other) {
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

	public FilePath setLastAccessDateTime(LocalDateTime lastAccessTime) {
		return setLastAccessTime(convert(lastAccessTime));
	}

	public FilePath setCreationDateTime(LocalDateTime creationTime) {
		return setCreationTime(convert(creationTime));
	}

	public FilePath setLastModifiedDateTime(LocalDateTime time) {
		return setLastModifiedTime(convert(time));
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
		return writeNew(lines, getCharSet());
	}

	public FilePath writeNew(String text, Charset charset) {
		return write(text, charset, StandardOpenOption.CREATE_NEW);
	}

	public FilePath writeNew(String text) {
		return writeNew(text, getCharSet());
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
		return writeAppend(lines, getCharSet());
	}

	public FilePath writeAppend(String text, Charset charset) {
		return write(text, charset, StandardOpenOption.APPEND);
	}

	public FilePath writeAppend(CharSequence text) {
		return writeAppend(text, getCharSet());
	}

	public FilePath writeAppend(CharSequence text, Charset charset) {
		return writeAppend(text.toString(), charset);
	}

	public FilePath writeAppend(String text) {
		return writeAppend(text, getCharSet());
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

	public FilePath writeTo(FilePath target, CopyOption... options) {
		return copyTo(target, options);
	}

	public FilePath writeFrom(FilePath source, CopyOption... options) {
		return copyFrom(source, options);
	}

	public FilePath write(boolean lastNewLine, Iterable<? extends CharSequence> lines, Charset charset,
			OpenOption... options) {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(newOutputStream(options), charset.newEncoder()))) {
			Iterator<? extends CharSequence> it = lines.iterator();
			while (it.hasNext()) {
				writer.append(it.next());
				if (it.hasNext() || lastNewLine) {
					writer.newLine();
				}
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath write(boolean lastNewLine, Iterable<? extends CharSequence> lines, OpenOption... options) {
		return write(lastNewLine, lines, getCharSet(), options);
	}

	public FilePath write(Iterable<? extends CharSequence> lines, Charset charset, OpenOption... options) {
		return write(true, lines, getCharSet(), options);
	}

	public FilePath write(Iterable<? extends CharSequence> lines, OpenOption... options) {
		return write(true, lines, getCharSet(), options);
	}

	public FilePath write(Stream<String> lines, Charset charset, OpenOption... options) {
		Iterable<String> tmp = () -> lines.iterator();
		return write(tmp, options);
	}

	public FilePath write(Stream<String> lines, OpenOption... options) {
		return write(lines, getCharSet(), options);
	}

	public FilePath write(String text, Charset charset, OpenOption... options) {
		try {
			return new FilePath(
					Files.write(this.getPath(), text.getBytes(charset == null ? getCharSet() : charset), options));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath write(CharSequence text, OpenOption... options) {
		return this.write(text, getCharSet(), options);
	}

	public FilePath write(String text, OpenOption... options) {
		return this.write(text, getCharSet(), options);
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

	public FilePath getCanonicalFilePath() {
		return new FilePath(getCanonicalPath());
	}

	public FilePath deleteEmptyDirectories() {
		if (exists() && isDirectory()) {
			streamChildrenForDeletion(this);
		}
		return this;
	}

	protected void streamChildrenForDeletion(Path f) {
		FilePath filePath = of(f);
		if (filePath.isDirectory()) {
			if (filePath.streamChildren().count() == 0) {
				filePath.delete();
			} else {
				filePath.streamChildren(new DirectoryFilter()).forEach(this::streamChildrenForDeletion);
			}
		}
	}

	public FilePath writeProperties(Properties properties) {
		return writeProperties(properties, null);
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

	public boolean isNotEmpty() {
		return (isFile() && getFileSize() > 0) || (isDirectory() && getChildren().size() > 0);
	}

	public boolean isEmpty() {
		return (isFile() && getFileSize() == 0) || (isDirectory() && getChildren().size() == 0);
	}

	public FilePath process(Predicate<String> isGroup, UnaryOperator<String> groupBuilder,
			Predicate<String> acceptGroup, BiConsumer<String, String> whenAccept) {
		return _process(isGroup, groupBuilder == null ? t -> t : groupBuilder,
				acceptGroup == null ? x -> true : acceptGroup, whenAccept);
	}

	protected FilePath _process(Predicate<String> isGroup, UnaryOperator<String> groupBuilder,
			Predicate<String> acceptGroup, BiConsumer<String, String> whenAccept) {
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

	public FilePath download(URI uri) {
		return write(uri);
	}

	public FilePath download(URL url) {
		return write(url);
	}

	public FilePath write(URI uri) {
		try {
			return write(uri.toURL());
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * downloads a file from the web to a local file when it does not exists or
	 * is older, binary copy
	 *
	 * @param urlSourceFile : URL : file on the web
	 */
	public FilePath write(URL url) {
		if (this.exists()) {
			long localFileTime = this.getLastModifiedTime().toMillis();
			URLConnection conn;
			try {
				conn = url.openConnection();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
			long webFileTime = conn.getLastModified();
			if (webFileTime <= localFileTime) {
				return null;
			}
			long len = conn.getContentLengthLong();
			if ((len != -1) && (len == this.getFileSize())) {
				return null;
			}
		}
		try (InputStream in = new BufferedInputStream(url.openStream());
				OutputStream out = this.newBufferedOutputStream()) {
			Utils.copy(in, out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath write(InputStream binaryStream) {
		try (BufferedInputStream in = new BufferedInputStream(binaryStream);
				BufferedOutputStream out = newBufferedOutputStream()) {
			IOUtils.copyLarge(in, out);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath read(OutputStream binaryStream) {
		try (BufferedOutputStream out = new BufferedOutputStream(binaryStream);
				BufferedInputStream in = newBufferedInputStream()) {
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
		return renameFileName(
				filename + (StringUtils.isBlank(getExtension()) ? "" : getFileExtensionSeperator() + getExtension()));
	}

	public FilePath moveFileName(String filename) {
		return renameFileName(filename);
	}

	public FilePath renameFileName(String filename) {
		return moveTo(getParentPath().child(filename));
	}

	/**
	 * extract files from this zip, overwrites
	 */
	public void unzip(FilePath target) throws IOException {
		try (ZipInputStream zin = new ZipInputStream(newBufferedInputStream())) {
			ZipEntry entry;
			while ((entry = zin.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					FilePath file = target.child(entry.getName());
					file.getParentPath().createDirectory();
					file.copyFrom(zin);
				} else {
					FilePath dir = target.child(entry.getName());
					dir.createDirectory();
				}
			}
		}
	}

	/**
	 * write files into this zip, overwrites
	 */
	public void zip(FilePath... files) {
		try {
			byte[] buffer = new byte[Utils.DEFAULT_BUFFER_LEN];
			int read;
			try (ZipOutputStream zout = new ZipOutputStream(newBufferedOutputStream())) {
				for (FilePath file : files) {
					if (file.isDirectory()) {
						continue;
					}
					zout.putNextEntry(new ZipEntry(file.getName()));
					try (InputStream fin = file.newBufferedInputStream()) {
						while ((read = fin.read(buffer)) > 0) {
							zout.write(buffer, 0, read);
						}
						fin.close();
					}
					zout.closeEntry();
				}
				zout.close();
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * write single file/data=byte[] into this zip, overwrites
	 */
	public void zip(String entryname, byte[] data) {
		zip(entryname, new ByteArrayInputStream(data));
	}

	/**
	 * write single file/data=InputStream into this zip, overwrites
	 */
	public void zip(String entryname, InputStream in) {
		try {
			byte[] buffer = new byte[Utils.DEFAULT_BUFFER_LEN];
			int read;
			try (ZipOutputStream zout = new ZipOutputStream(newOutputStream())) {
				zout.putNextEntry(new ZipEntry(entryname));
				while ((read = in.read(buffer)) > 0) {
					zout.write(buffer, 0, read);
				}
				in.close();
				zout.closeEntry();
				zout.close();
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * Windows only
	 */
	protected static final Map<String, String> FILE_TYPES = new HashMap<>();

	/**
	 * Windows only
	 */
	public String getFileTypeName() {
		String ext = getExtension();
		if (ext == null)
			return null;
		ext = ext.toLowerCase();
		if (FILE_TYPES.containsKey(ext))
			return FILE_TYPES.get(ext);
		try {
			String value = WinRegistryAlt
					.getRegValue("HKEY_CLASSES_ROOT\\" + Utils.WIN_FILE_EXTS.get(ext), null, "REG_SZ", false).get(0);
			if (value.startsWith("[") && value.endsWith("]")) {
				value = value.substring(1, value.length() - 1);
			}
			if ("(value not set)".equals(value)) {
				value = null;
			}
			FILE_TYPES.put(ext, value);
			return value.toString();
		} catch (Exception ex) {
			try {
				if (exists()) {
					String systemTypeDescription = FileSystemView.getFileSystemView()
							.getSystemTypeDescription(toFile());
					if (StringUtils.isNotBlank(systemTypeDescription))
						return systemTypeDescription;
				}
				String type = FileSystemView.getFileSystemView().getSystemTypeDescription(createTempFile(ext).toFile())
						.toString();
				FILE_TYPES.put(ext, type);
				return type;
			} catch (Exception ex2) {
				return ext;
			}
		}
	}

	public FileChannel openFileChannel(OpenOption... options) {
		try {
			return FileChannel.open(getPath(), options);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public FilePath nioCopy(FilePath target) {
		return nioCopy(target, true);
	}

	public FilePath nioCopy(FilePath target, boolean blocks) {
		try (FileChannel inChannel = openFileChannel(); FileChannel outChannel = target.openFileChannel()) {
			if (blocks) {
				// magic number for Windows, 64Mb - 32Kb)
				int maxCount = (64 * 1024 * 1024) - (32 * 1024);
				long size = inChannel.size();
				long position = 0;
				while (position < size) {
					position += inChannel.transferTo(position, maxCount, outChannel);
				}
			} else {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			}
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return target;
	}

	/**
	 * windows only
	 *
	 * @see http://stackoverflow.com/questions/1646425/cmd-command-to-delete-files-and-put-them-into-recycle-bin
	 * @see https://github.com/npocmaka/batch.scripts/blob/master/hybrids/jscript/deleteJS.bat
	 * @see http://stackoverflow.com/questions/615948/how-do-i-run-a-batch-file-from-my-java-application
	 */
	public boolean recycle() {
		if (notExists()) {
			return false;
		}
		FilePath deleteJSBat = getTempDirectory().child("deleteJS.bat");
		if (deleteJSBat.notExists()) {
			FilePath source = FilePath.of(FilePath.class, "deleteJS/deleteJS.bat");
			source.writeTo(deleteJSBat);
		}
		try {
			Process p = Runtime.getRuntime()
					.exec(new String[] { "cmd", "/c", "call", deleteJSBat.getAbsolutePath(), getAbsolutePath() });
			p.waitFor();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		} catch (InterruptedException ex) {
			//
		}
		return notExists();
	}

	public Charset getCharSet() {
		if (charSet == null) {
			return Charset.defaultCharset();
		}
		return this.charSet;
	}

	public Charset guessCharSet() {
		throw new UnsupportedOperationException("not implemented");
	}

	public void setCharSet(Charset charSet) {
		this.charSet = charSet;
	}

	public FilePath prefix(String string) {
		return getParentPath().child(string + getShortFileName()).addExtension(getExtension());
	}

	public FilePath suffix(String string) {
		return getParentPath().child(getShortFileName() + string).addExtension(getExtension());
	}

	/**
	 * Windows only
	 */
	public FilePath explore() {
		try {
			Runtime.getRuntime().exec("explorer.exe /select,\"" + getAbsolutePath() + "\"");
		} catch (IOException ex) {
			//
		}
		return this;
	}

	private static Path createDirectory(Path path, boolean throwException, FileAttribute<?>... attrs) {
		if (!throwException && Files.exists(path) && Files.isDirectory(path)) {
			return path;
		}
		if (Files.exists(path) && !Files.isDirectory(path)) {
			throw new UncheckedIOException(new FileAlreadyExistsException("path exists and is not a directory"));
		}
		try {
			return Files.createDirectories(path, attrs);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private static boolean delete(Path path, boolean throwException) {
		if (!throwException) {
			if (Files.notExists(path)) {
				return false;
			}
			if (Files.isDirectory(path) && !Files.isSymbolicLink(path)) {
				try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
					stream.forEach(subPath -> delete(subPath, throwException));
				} catch (IOException ex) {
					if (throwException) {
						throw new UncheckedIOException(ex);
					}
					System.err.println("failed to delete: " + path + " :: " + ex);
				}
			}
		}
		try {
			Files.delete(path);
			return true;
		} catch (IOException ex) {
			if (throwException) {
				throw new UncheckedIOException(ex);
			}
			System.err.println("failed to delete: " + path + " :: " + ex);
			return false;
		}
	}

	public FilePath delete() {
		delete(getPath(), false);
		return this;
	}

	public FilePath delete(boolean throwExceptions) {
		delete(getPath(), throwExceptions);
		return this;
	}

	public FilePath createDirectory(FileAttribute<?>... attrs) {
		createDirectory(getPath(), false, attrs);
		return this;
	}

	public FilePath createDirectory(boolean throwExceptions, FileAttribute<?>... attrs) {
		createDirectory(getPath(), throwExceptions, attrs);
		return this;
	}

	public static InputStream buffer(boolean buffer, InputStream in) {
		if (!buffer)
			return in;
		if (in instanceof BufferedInputStream)
			return in;
		return new BufferedInputStream(in);
	}

	public static OutputStream buffer(boolean buffer, OutputStream out) {
		if (!buffer)
			return out;
		if (out instanceof BufferedOutputStream)
			return out;
		return new BufferedOutputStream(out);
	}

	public FilePath processInput(boolean buffer, Consumer<InputStream> inputStreamConsumer, OpenOption... options) {
		try (InputStream in = buffer(buffer, newInputStream(options))) {
			inputStreamConsumer.accept(in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public FilePath processOutput(boolean buffer, Consumer<OutputStream> outputStreamConsumer, OpenOption... options) {
		try (OutputStream in = buffer(buffer, newOutputStream(options))) {
			outputStreamConsumer.accept(in);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return this;
	}

	public String getMimeType() {
		try {
			return Files.probeContentType(getPath());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	// https://stackoverflow.com/questions/28698125/java-check-if-path-is-parent-of-a-file
	public static boolean isChildPath(Path parent, Path child) {
		Path pn = parent.normalize();
		Path cn = child.normalize();
		return cn.getNameCount() > pn.getNameCount() && cn.startsWith(pn);
	}

	public boolean isParentOf(FilePath child) {
		return isChildPath(toPath(), child.toPath());
	}

	public boolean isChildOf(FilePath parent) {
		return parent.isParentOf(this);
	}
}
