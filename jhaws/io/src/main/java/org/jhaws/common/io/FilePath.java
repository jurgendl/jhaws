package org.jhaws.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.jhaws.common.io.Utils.OSGroup;

/**
 * @since 1.7
 */
public class FilePath implements Path, Externalizable {
    public static class FileLineIterator implements Iterator<String>, Closeable {
        protected transient final FilePath path;

        protected transient final Charset charset;

        protected transient String line;

        protected transient BufferedReader bufferedReader;

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
            BufferedReader br = this.getBufferedReader();
            if (br != null) {
                br.close();
            }

        }

        protected BufferedReader getBufferedReader() throws IOException {
            if (!this.openened) {
                this.openened = true;
                this.bufferedReader = this.path.newBufferedReader();
            }
            return this.bufferedReader;
        }

        @Override
        public boolean hasNext() {
            this.optionalReadLine();
            return this.line != null;
        }

        @Override
        public String next() {
            this.optionalReadLine();
            if (this.line == null) {
                throw new NoSuchElementException();
            }
            String tmp = this.line;
            this.line = null;
            return tmp;
        }

        protected void optionalReadLine() {
            if (this.line == null) {
                try {
                    BufferedReader br = this.getBufferedReader();
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

    protected static FilePath checkFileIndex(Path parent, String outFileName, String extension) {
        return FilePath.checkFileIndex(parent, outFileName, "_", "0000", extension);
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
     * @param parent : String : the location (path only) of the target file
     * @param outFileName : String : the name of the target file (without extension and . before extension)
     * @param sep : String : characters sperating filename from index (example: _ )
     * @param format : String : number of positions character 0 (example: 0000 )
     * @param extension : String : the extension of the target file (without . before extension), see class constants FORMAT... for possibilities
     *
     * @return : IOFile : new indexed File
     */
    protected static FilePath checkFileIndex(Path parent, String outFileName, String sep, String format, String extension) {
        String SEPARATOR = sep;
        String FORMAT = sep + format;
        FilePath file = "".equals(extension) ? new FilePath(parent, outFileName) : new FilePath(parent, outFileName + "." + extension);
        if (file.exists()) {
            if (outFileName.length() <= FORMAT.length()) {
                outFileName = outFileName + FORMAT;
                if (extension.equals("")) {
                    file = new FilePath(parent, outFileName);
                } else {
                    file = new FilePath(parent, outFileName + "." + extension);
                }
            } else {
                String ch = outFileName.substring(outFileName.length() - FORMAT.length(),
                        (outFileName.length() - FORMAT.length()) + SEPARATOR.length());
                String nr = outFileName.substring((outFileName.length() - FORMAT.length()) + SEPARATOR.length(), outFileName.length());
                boolean isNumber = true;
                try {
                    Integer.parseInt(nr);
                } catch (final NumberFormatException ex) {
                    isNumber = false;
                }
                if (!(isNumber && (ch.compareTo(SEPARATOR) == 0))) {
                    outFileName = outFileName + FORMAT;
                    if (extension.equals("")) {
                        file = new FilePath(parent, outFileName);
                    } else {
                        file = new FilePath(parent, outFileName + "." + extension);
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
                file = new FilePath(parent, outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR + indStringSB.toString()
                        + "." + extension);
            }
            ind++;
        }
        return file;
    }

    public static FilePath createDefaultTempDirectory(String prefix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempDirectory(prefix, attrs));
    }

    public static FilePath createDefaultTempFile(String prefix, FileAttribute<?>... attrs) throws IOException {
        return FilePath.createDefaultTempFile(prefix, null, attrs);
    }

    public static FilePath createDefaultTempFile(String prefix, String extension, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempFile(prefix + "-", extension == null ? "" : "." + extension, attrs));
    }

    public static String getConvertedSize(Long size) {
        if (size == null) {
            return "";
        }
        int scale = (int) (Math.log10(size) / 3);
        return new DecimalFormat().format(size / (1 << (scale * 10))) + "" + FilePath.UNITS[scale];
    }

    public static String getExtension(Path path) {
        String fileName = path.getFileName().toString();
        int p = fileName.lastIndexOf(FilePath.DOT);
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
        int p = fileName.lastIndexOf('.');
        if (p == -1) {
            return fileName;
        }
        return fileName.substring(0, p);
    }

    /**
     * convers a filename to a legal filename for given operating system, too long parts are chopped, illegal characters are replaced by the character
     * <i>_</i> , a missing extensions is adapted to extension <i>ext</i>
     *
     * @param filename : String : current name
     * @param os : int : operating system, use FileConv.WIN32, FileConv.DOS, FileConv.UNIX
     *
     * @return : String : converted name
     * @throws IOException
     */
    public static FilePath legalize(String filename, final OSGroup os) throws IOException {
        filename = new FilePath(filename).getName();
        while (filename.substring(0, 1).compareTo(".") == 0) {
            filename = filename.substring(1, filename.length());
        }
        char[] c = new char[0];
        if (os == OSGroup.Dos) {
            String[] parts = filename.split("\\.");
            if (parts.length == 1) {
                if (filename.substring(filename.length() - 1, filename.length()).compareTo(".") != 0) {
                    filename = filename + ".";
                }

                filename = filename + "ext";
            }
            if (parts.length > 2) {
                filename = parts[0] + "." + parts[parts.length - 1];
            }
            parts = filename.split("\\.");
            if (parts[0].length() > 8) {
                parts[0] = parts[0].substring(0, 8);
            }
            if (parts[1].length() > 3) {
                parts[1] = parts[1].substring(0, 3);
            }
            filename = parts[0] + "." + parts[1];
            c = Utils.legal(os);
        }
        if (os == OSGroup.Windows) {
            c = Utils.legal(os);
        }
        if (os == OSGroup.Nix) {
            c = Utils.legal(os);
        }
        if (c.length == 0) {
            throw new IOException("characters for OS not found");
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

    public static FilePath wrap(Path p) {
        return p instanceof FilePath ? FilePath.class.cast(p) : new FilePath(p);
    }

    public static final char DOT = '.';

    protected transient Path path;

    protected static ExtensionIconFinder grabber = null;

    static {
        try {
            FilePath.grabber = (ExtensionIconFinder) Class.forName("org.jhaws.common.io.SystemIcon").newInstance();
        } catch (Throwable e) {
            //
        }
    }

    /** temp dir */
    public static final FilePath TEMPDIR = new FilePath(System.getProperty("java.io.tmpdir"));

    /** desktop */
    public static final FilePath DESKTOPDIR = new FilePath(FileSystemView.getFileSystemView().getHomeDirectory());

    /** user home dir */
    public static final FilePath USERDIR = new FilePath(System.getProperty("user.home"));

    public static final String[] UNITS = new String[] { "bytes", "kB", "MB", "GB", "TB"/* , "PB" */};

    public FilePath() {
        this.path = Paths.get(".").toAbsolutePath().normalize();
    }

    public FilePath(Class<?> root, String relativePath) {
        this(root.getClassLoader().getResource(
                root.getPackage().getName().replace('.', '/') + (relativePath.startsWith("/") ? "" : "/") + relativePath));
    }

    public FilePath(File file) {
        this.path = Paths.get(file.toURI());
    }

    public FilePath(File dir, String file) {
        this.path = Paths.get(dir.toURI()).resolve(file);
    }

    public FilePath(FilePath file) {
        if (this.getPath() == null) {
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

    public FilePath(URL url) {
        try {
            URI uri = url.toURI();
            if ("file".equals(uri.getScheme())) {
                String p = uri.getPath().substring(1);
                this.path = Paths.get(p);
            } else {
                this.path = Paths.get(uri);
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Long adler32() throws IOException {
        return this.checksum(new Adler32());
    }

    public FilePath checkDirectory() throws IOException {
        if (!this.isDirectory()) {
            throw new IOException("not a directory");
        }
        return this;
    }

    public FilePath checkExists() throws IOException {
        if (!this.exists()) {
            throw new IOException("does not exists");
        }
        return this;
    }

    public FilePath checkFile() throws IOException {
        if (this.isDirectory()) {
            throw new IOException("not a file");
        }
        return this;
    }

    /**
     * {@link #checkFileIndex(String, String, String, String, String)} but with separator set to '_' and format to '0000' and the other parameters
     * derived from given File
     */
    public FilePath checkFileIndex() {
        try {
            if (!this.exists()) {
                return this;
            }
            String shortFile = this.getShortFileName();
            String extension = this.getExtension();
            return FilePath.checkFileIndex(new FilePath(this.getParent()), shortFile, extension);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long checksum(Checksum checksum) throws IOException {
        InputStream is = null;
        Long rv = null;
        try {
            is = this.newInputStream();
            int cnt;
            while ((cnt = is.read()) != -1) {
                checksum.update(cnt);
            }
            rv = checksum.getValue();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    //
                }
            }
        }
        return rv;
    }

    /**
     * compares two files binary, reads only first maxCompareSize bytes
     *
     * @param file : IOFile : file to compare to
     * @param maxCompareSize0 : int : maximum size when comparing files
     *
     * @return : boolean : file is equal or not
     *
     * @throws IOException : thrown exception
     */
    public boolean compareContents(Path file, int maxCompareSize) throws IOException {
        BufferedInputStream iser1 = null;
        BufferedInputStream iser2 = null;
        try {
            long size = this.size();
            if (size != Files.size(file)) {
                return false;
            }
            int compareSize = Math.min(maxCompareSize, (int) size);
            iser1 = this.newBufferedInputStream();
            byte[] b1 = new byte[compareSize];
            iser1.read(b1, 0, compareSize);
            iser2 = this.newBufferedInputStream();
            byte[] b2 = new byte[compareSize];
            iser2.read(b2, 0, compareSize);
            int bpos = 0;
            while (bpos < compareSize) {
                if (b1[bpos] != b2[bpos]) {
                    return false;
                }
                bpos++;
            }
            return true;
        } finally {
            if (iser1 != null) {
                try {
                    iser1.close();
                } catch (Exception ex) {
                    //
                }
            }
            if (iser2 != null) {
                try {
                    iser2.close();
                } catch (Exception ex) {
                    //
                }
            }
        }
    }

    /**
     *
     * @see java.nio.file.Path#compareTo(java.nio.file.Path)
     */
    @Override
    public int compareTo(Path other) {
        return this.getPath().compareTo(other);
    }

    public long copyFrom(InputStream in, CopyOption... options) throws IOException {
        return Files.copy(in, this.getPath(), options);
    }

    public FilePath copyFrom(Path source, CopyOption... options) throws IOException {
        return new FilePath(Files.copy(FilePath.getPath(source), this, options));
    }

    public long copyTo(OutputStream out) throws IOException {
        return Files.copy(this.getPath(), out);
    }

    public FilePath copyTo(Path target) throws IOException {
        return this.copyTo(FilePath.getPath(target), StandardCopyOption.REPLACE_EXISTING);
    }

    public FilePath copyTo(Path target, CopyOption... options) throws IOException {
        return new FilePath(Files.copy(this.getPath(), FilePath.getPath(target), options));
    }

    public Long crc32() throws IOException {
        return this.checksum(new CRC32());
    }

    public FilePath createDirectories(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createDirectories(this.getPath(), attrs));
    }

    public FilePath createDirectory(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createDirectory(this.getPath(), attrs));
    }

    public FilePath createFile(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createFile(this.getPath(), attrs));
    }

    public FilePath createLinkFrom(Path existing) throws IOException {
        return new FilePath(Files.createLink(this.getPath(), existing));
    }

    public FilePath createLinkTo(Path link) throws IOException {
        return new FilePath(Files.createLink(link, this.getPath()));
    }

    public Path createSymbolicLinkFrom(Path link, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createSymbolicLink(link, this.getPath(), attrs));
    }

    public Path createSymbolicLinkTo(Path target, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createSymbolicLink(this.getPath(), target, attrs));
    }

    public FilePath createTempDirectory(String prefix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempDirectory(this.getPath(), prefix, attrs));
    }

    public FilePath createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempFile(this.getPath(), prefix, suffix, attrs));
    }

    public FilePath delete() throws IOException {
        Files.delete(this.getPath());
        return this;
    }

    public FilePath deleteIfExists() throws IOException {
        Files.deleteIfExists(this.getPath());
        return this;
    }

    /**
     * downloads a file from the web to a local file when it does not exists or is older, binary copy
     *
     * @param urlSourceFile : URL : file on the web
     *
     * @return : boolean : file downloaded or not
     *
     * @throws IOException : IOException
     */
    public boolean download(URL urlSourceFile) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (this.exists()) {
                long localFileTime = this.getLastModifiedTime().toMillis();
                URLConnection conn = urlSourceFile.openConnection();
                long webFileTime = conn.getLastModified();
                if (webFileTime <= localFileTime) {
                    return false;
                }
            }
            is = new BufferedInputStream(urlSourceFile.openStream());
            os = this.newBufferedOutputStream();
            int b = is.read();
            while (b != -1) {
                os.write(b);
                b = is.read();
            }
            return true;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    //
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    //
                }
            }
        }
    }

    /**
     *
     * @see java.nio.file.Path#endsWith(java.nio.file.Path)
     */
    @Override
    public boolean endsWith(Path other) {
        return this.getPath().endsWith(other);
    }

    /**
     *
     * @see java.nio.file.Path#endsWith(java.lang.String)
     */
    @Override
    public boolean endsWith(String other) {
        return this.getPath().endsWith(other);
    }

    /**
     *
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

    public Object getAttribute(String attribute, LinkOption... options) throws IOException {
        return Files.getAttribute(this.getPath(), attribute, options);
    }

    public List<FilePath> getChildren() {
        List<FilePath> children = new ArrayList<>();
        try (DirectoryStream<Path> stream = this.newDirectoryStream()) {
            for (Path file : stream) {
                children.add(new FilePath(file));
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            throw new RuntimeException(x);
        }
        return children;
    }

    public String getConvertedSize() throws IOException {
        return FilePath.getConvertedSize(this.size());
    }

    public String getExtension() {
        return FilePath.getExtension(this.getPath());
    }

    public <V extends FileAttributeView> V getFileAttributeView(Class<V> type, LinkOption... options) {
        return Files.getFileAttributeView(this.getPath(), type, options);
    }

    /**
     *
     * @see java.nio.file.Path#getFileName()
     */
    @Override
    public Path getFileName() {
        return new FilePath(this.getPath().getFileName());
    }

    public FileStore getFileStore() throws IOException {
        return Files.getFileStore(this.getPath());
    }

    /**
     *
     * @see java.nio.file.Path#getFileSystem()
     */
    @Override
    public FileSystem getFileSystem() {
        return this.getPath().getFileSystem();
    }

    public Icon getLargeIcon() {
        return FilePath.grabber.getLargeIcon(this.toFile());
    }

    public FileTime getLastModifiedTime(LinkOption... options) throws IOException {
        return Files.getLastModifiedTime(this.getPath(), options);
    }

    public String getName() {
        return this.toString();
    }

    /**
     *
     * @see java.nio.file.Path#getName(int)
     */
    @Override
    public Path getName(int index) {
        return new FilePath(this.getPath().getName(index));
    }

    /**
     *
     * @see java.nio.file.Path#getNameCount()
     */
    @Override
    public int getNameCount() {
        return this.getPath().getNameCount();
    }

    public UserPrincipal getOwner(LinkOption... options) throws IOException {
        return Files.getOwner(this.getPath(), options);
    }

    /**
     *
     * @see java.nio.file.Path#getParent()
     */
    @Override
    public Path getParent() {
        return this.getPath().getParent() == null ? null : new FilePath(this.getPath().getParent());
    }

    public Path getPath() {
        return FilePath.getPath(this);
    }

    public Set<PosixFilePermission> getPosixFilePermissions(LinkOption... options) throws IOException {
        return Files.getPosixFilePermissions(this.getPath(), options);
    }

    /**
     *
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

    public boolean isHidden() throws IOException {
        return Files.isHidden(this.getPath());
    }

    public boolean isReadable() {
        return Files.isReadable(this.getPath());
    }

    public boolean isRegularFile(LinkOption... options) {
        return Files.isRegularFile(this.getPath(), options);
    }

    public boolean isSameFile(Path otherPath) throws IOException {
        return Files.isSameFile(this.getPath(), otherPath);
    }

    public boolean isSymbolicLink() {
        return Files.isSymbolicLink(this.getPath());
    }

    public boolean isWritable() {
        return Files.isWritable(this.getPath());
    }

    /**
     *
     * @see java.nio.file.Path#iterator()
     */
    @Override
    public Iterator<Path> iterator() {
        return this.getPath().iterator();
    }

    public FileLineIterator lines() throws IOException {
        return new FileLineIterator(this);
        // return Files.lines(this.getPath());
    }

    public FilePath moveFrom(Path source) throws IOException {
        return this.moveFrom(source, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public FilePath moveFrom(Path source, CopyOption... options) throws IOException {
        return new FilePath(Files.move(FilePath.getPath(source), this, options));
    }

    public FilePath moveTo(Path target) throws IOException {
        return this.moveTo(target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }

    public FilePath moveTo(Path target, CopyOption... options) throws IOException {
        return new FilePath(Files.move(this, FilePath.getPath(target), options));
    }

    public BufferedInputStream newBufferedInputStream(OpenOption... options) throws IOException {
        return new BufferedInputStream(this.newInputStream(options));
    }

    public BufferedOutputStream newBufferedOutputStream(OpenOption... options) throws IOException {
        return new BufferedOutputStream(this.newOutputStream(options));
    }

    public BufferedReader newBufferedReader() throws IOException {
        return this.newBufferedReader(Charset.defaultCharset());
    }

    public BufferedReader newBufferedReader(Charset cs) throws IOException {
        return Files.newBufferedReader(this.getPath(), cs);
    }

    public BufferedWriter newBufferedWriter(Charset cs, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(this.getPath(), cs, options);
    }

    public BufferedWriter newBufferedWriter(OpenOption... options) throws IOException {
        return this.newBufferedWriter(Charset.defaultCharset(), options);
    }

    public SeekableByteChannel newByteChannel(OpenOption... options) throws IOException {
        return Files.newByteChannel(this.getPath(), options);
    }

    public SeekableByteChannel newByteChannel(Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return Files.newByteChannel(this.getPath(), options, attrs);
    }

    public DirectoryStream<Path> newDirectoryStream() throws IOException {
        return Files.newDirectoryStream(this.getPath());
    }

    public DirectoryStream<Path> newDirectoryStream(DirectoryStream.Filter<? super Path> filter) throws IOException {
        return Files.newDirectoryStream(this.getPath(), filter);
    }

    public DirectoryStream<Path> newDirectoryStream(String glob) throws IOException {
        return Files.newDirectoryStream(this.getPath(), glob);
    }

    public InputStream newInputStream(OpenOption... options) throws IOException {
        return Files.newInputStream(this.getPath(), options);
    }

    public OutputStream newOutputStream(OpenOption... options) throws IOException {
        return Files.newOutputStream(this.getPath(), options);
    }

    /**
     *
     * @see java.nio.file.Path#normalize()
     */
    @Override
    public Path normalize() {
        return new FilePath(this.getPath().normalize());
    }

    public boolean notExists(LinkOption... options) {
        return Files.notExists(this.getPath(), options);
    }

    public PathIterator paths() {
        return new PathIterator(this);
    }

    public String probeContentType() throws IOException {
        return Files.probeContentType(this.getPath());
    }

    public InputStream read() throws IOException {
        return this.newInputStream();
    }

    public long read(InputStream in) throws IOException {
        return this.copyFrom(in);
    }

    public String readAll() throws IOException {
        return this.readAll(Charset.defaultCharset());
    }

    public String readAll(Charset cs) throws IOException {
        return new String(Files.readAllBytes(this.getPath()), cs);
    }

    public byte[] readAllBytes() throws IOException {
        return Files.readAllBytes(this.getPath());
    }

    public ByteBuffer readAllBytesToBuffer() throws IOException {
        return ByteBuffer.wrap(Files.readAllBytes(this.getPath()));
    }

    public List<String> readAllLines() throws IOException {
        return this.readAllLines(Charset.defaultCharset());
    }

    public List<String> readAllLines(Charset cs) throws IOException {
        return Files.readAllLines(this.getPath(), cs);
    }

    public <A extends BasicFileAttributes> A readAttributes(Class<A> type, LinkOption... options) throws IOException {
        return Files.readAttributes(this.getPath(), type, options);
    }

    public Map<String, Object> readAttributes(String attributes, LinkOption... options) throws IOException {
        return Files.readAttributes(this.getPath(), attributes, options);
    }

    /**
     *
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

    public FilePath readSymbolicLink() throws IOException {
        return new FilePath(Files.readSymbolicLink(this.getPath()));
    }

    /**
     *
     * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind[])
     */
    @Override
    public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
        return this.getPath().register(watcher, events);
    }

    /**
     *
     * @see java.nio.file.Path#register(java.nio.file.WatchService, java.nio.file.WatchEvent.Kind[], java.nio.file.WatchEvent.Modifier[])
     */
    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        return this.getPath().register(watcher, events, modifiers);
    }

    /**
     *
     * @see java.nio.file.Path#relativize(java.nio.file.Path)
     */
    @Override
    public Path relativize(Path other) {
        return new FilePath(this.getPath().relativize(FilePath.getPath(other)));
    }

    /**
     *
     * @see java.nio.file.Path#resolve(java.nio.file.Path)
     */
    @Override
    public Path resolve(Path other) {
        return new FilePath(this.getPath().resolve(FilePath.getPath(other)));
    }

    /**
     *
     * @see java.nio.file.Path#resolve(java.lang.String)
     */
    @Override
    public Path resolve(String other) {
        return new FilePath(this.getPath().resolve(other));
    }

    /**
     *
     * @see java.nio.file.Path#resolveSibling(java.nio.file.Path)
     */
    @Override
    public Path resolveSibling(Path other) {
        return new FilePath(this.getPath().resolveSibling(FilePath.getPath(other)));
    }

    /**
     *
     * @see java.nio.file.Path#resolveSibling(java.lang.String)
     */
    @Override
    public Path resolveSibling(String other) {
        return new FilePath(this.getPath().resolveSibling(other));
    }

    public FilePath setAttribute(String attribute, Object value, LinkOption... options) throws IOException {
        return new FilePath(Files.setAttribute(this.getPath(), attribute, value, options));
    }

    public boolean setExecutable(boolean executable) throws IOException {
        Files.setAttribute(this, null, executable);
        return true;
    }

    public boolean setExecutable(boolean executable, boolean ownerOnly) throws IOException {
        Files.setAttribute(this, null, executable);
        Files.setAttribute(this, null, ownerOnly);
        return true;
    }

    public FilePath setLastModifiedTime(FileTime time) throws IOException {
        return new FilePath(Files.setLastModifiedTime(this.getPath(), time));
    }

    public FilePath setOwner(UserPrincipal owner) throws IOException {
        return new FilePath(Files.setOwner(this.getPath(), owner));
    }

    public FilePath setPosixFilePermissions(Set<PosixFilePermission> perms) throws IOException {
        return new FilePath(Files.setPosixFilePermissions(this.getPath(), perms));
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

    public long size() throws IOException {
        return Files.size(this.getPath());
    }

    /**
     *
     * @see java.nio.file.Path#startsWith(java.nio.file.Path)
     */
    @Override
    public boolean startsWith(Path other) {
        return this.getPath().startsWith(other);
    }

    /**
     *
     * @see java.nio.file.Path#startsWith(java.lang.String)
     */
    @Override
    public boolean startsWith(String other) {
        return this.getPath().startsWith(other);
    }

    /**
     *
     * @see java.nio.file.Path#subpath(int, int)
     */
    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return new FilePath(this.getPath().subpath(beginIndex, endIndex));
    }

    /**
     *
     * @see java.nio.file.Path#toAbsolutePath()
     */
    @Override
    public Path toAbsolutePath() {
        return new FilePath(this.getPath().toAbsolutePath());
    }

    /**
     *
     * @see java.nio.file.Path#toFile()
     */
    @Override
    public File toFile() {
        return this.getPath().toFile();
    }

    /**
     *
     * @see java.nio.file.Path#toRealPath(java.nio.file.LinkOption[])
     */
    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return new FilePath(this.getPath().toRealPath(options));
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getPath().toString();
    }

    public long totalSize() throws IOException {
        final AtomicLong size = new AtomicLong(0);
        this.walkFileTree(new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return size.get();
    }

    /**
     *
     * @see java.nio.file.Path#toUri()
     */
    @Override
    public URI toUri() {
        return this.getPath().toUri();
    }

    public FilePath walkDirectories(final FileVisit visitor) throws IOException {
        return this.walkFileTree(new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                FileVisitResult preVisitDirectory = super.preVisitDirectory(dir, attrs);
                visitor.visit(new FilePath(dir), attrs);
                return preVisitDirectory;
            }
        });
    }

    public FilePath walkFiles(final FileVisit visitor) throws IOException {
        return this.walkFileTree(new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                FileVisitResult visitFile = super.visitFile(file, attrs);
                visitor.visit(new FilePath(file), attrs);
                return visitFile;
            }
        });
    }

    public FilePath walkFileTree(FileVisitor<? super Path> visitor) throws IOException {
        return new FilePath(Files.walkFileTree(this.getPath(), visitor));
    }

    public FilePath walkFileTree(Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) throws IOException {
        return new FilePath(Files.walkFileTree(this.getPath(), options, maxDepth, visitor));
    }

    public FilePath write(byte[] bytes, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.getPath(), bytes, options));
    }

    public FilePath write(ByteBuffer bytes, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.getPath(), bytes.array(), options));
    }

    public FilePath write(Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.getPath(), lines, cs, options));
    }

    public Path write(Iterable<? extends CharSequence> lines, OpenOption... options) throws IOException {
        try (BufferedWriter bufferedWriter = this.newBufferedWriter(options)) {
            for (CharSequence line : lines) {
                bufferedWriter.write(line.toString());
            }
        }
        return this;// Files.write(this.getPath(), lines, options);
    }

    public FilePath write(String text, Charset charset, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.getPath(), text.getBytes(charset == null ? Charset.defaultCharset() : charset), options));
    }

    /**
     *
     * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.getPath().toUri().toString());
    }
}
