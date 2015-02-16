package org.jhaws.common.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;

import org.jhaws.common.io.Utils.OSGroup;

/**
 * @since 1.7
 */
public class FilePath implements Path, Externalizable {
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

    public static FilePath createDefaultTempFile(String prefix, String extension) throws IOException {
        return new FilePath(Files.createTempFile(prefix, "." + extension));
    }

    public static FilePath createDefaultTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempFile(prefix, suffix, attrs));
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

    protected transient Path path;

    protected static ExtensionIconFinder grabber = null;

    static {
        try {
            FilePath.grabber = (ExtensionIconFinder) Class.forName("org.jhaws.common.io.SystemIcon").newInstance();
        } catch (Throwable e) {
            //
        }
    }

    public FilePath(File file) {
        this.path = Paths.get(file.toURI());
    }

    public FilePath(File dir, String file) {
        this.path = Paths.get(dir.toURI()).resolve(file);
    }

    public FilePath(FilePath file) {
        this.path = file.path;
    }

    public FilePath(FilePath dir, String file) {
        this.path = dir.resolve(file);
    }

    public FilePath(Path path) {
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
            this.path = Paths.get(url.toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
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
            String extension = this.getFileExtension();
            return FilePath.checkFileIndex(this.getParent(), shortFile, extension);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public int compareTo(Path other) {
        return this.path.compareTo(other);
    }

    public long copy(InputStream in, CopyOption... options) throws IOException {
        return Files.copy(in, this.path, options);
    }

    public long copy(OutputStream out) throws IOException {
        return Files.copy(this.path, out);
    }

    public FilePath copyFrom(Path source, CopyOption... options) throws IOException {
        return new FilePath(Files.copy(source, this, options));
    }

    public FilePath copyTo(Path target, CopyOption... options) throws IOException {
        return new FilePath(Files.copy(this, target, options));
    }

    public FilePath createDirectories(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createDirectories(this.path, attrs));
    }

    public FilePath createDirectory(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createDirectory(this.path, attrs));
    }

    public FilePath createFile(FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createFile(this.path, attrs));
    }

    public FilePath createTempDirectory(String prefix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempDirectory(this.path, prefix, attrs));
    }

    public FilePath createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws IOException {
        return new FilePath(Files.createTempFile(this.path, prefix, suffix, attrs));
    }

    public void delete() throws IOException {
        Files.delete(this.path);
    }

    public void deleteIfExists() throws IOException {
        Files.deleteIfExists(this.path);
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

    @Override
    public boolean endsWith(Path other) {
        return this.path.endsWith(other);
    }

    @Override
    public boolean endsWith(String other) {
        return this.path.endsWith(other);
    }

    @Override
    public boolean equals(Object other) {
        return this.path.equals(other);
    }

    public boolean exists(LinkOption... options) {
        return Files.exists(this.path, options);
    }

    public Object getAttribute(String attribute, LinkOption... options) throws IOException {
        return Files.getAttribute(this.path, attribute, options);
    }

    /**
     * converts a size in bytes to kB, MB, GB, TB
     *
     * @param conversion : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
     * @param decimals : int : number of decimals to show
     *
     * @return : double : converted size
     * @throws FileNotFoundException
     */
    public double getConvertedSize(ByteTo conversion, int decimals) throws IOException {
        return IOGeneralFile.convertSize(this.size(), conversion, decimals);
    }

    public <V extends FileAttributeView> V getFileAttributeView(Class<V> type, LinkOption... options) {
        return Files.getFileAttributeView(this.path, type, options);
    }

    public String getFileExtension() {
        String fileName = this.path.getFileName().toString();
        int p = fileName.lastIndexOf('.');
        if (p == -1) {
            return null;
        }
        return fileName.substring(p + 1);
    }

    @Override
    public Path getFileName() {
        return new FilePath(this.path.getFileName());
    }

    public FileStore getFileStore() throws IOException {
        return Files.getFileStore(this.path);
    }

    @Override
    public FileSystem getFileSystem() {
        return this.path.getFileSystem();
    }

    public Icon getLargeIcon() {
        return FilePath.grabber.getLargeIcon(this.toFile());
    }

    public FileTime getLastModifiedTime(LinkOption... options) throws IOException {
        return Files.getLastModifiedTime(this.path, options);
    }

    public String getName() {
        return this.toString();
    }

    @Override
    public Path getName(int index) {
        return new FilePath(this.path.getName(index));
    }

    @Override
    public int getNameCount() {
        return this.path.getNameCount();
    }

    public UserPrincipal getOwner(LinkOption... options) throws IOException {
        return Files.getOwner(this.path, options);
    }

    @Override
    public Path getParent() {
        return new FilePath(this.path.getParent());
    }

    public Path getPath() {
        return this.path;
    }

    public Set<PosixFilePermission> getPosixFilePermissions(LinkOption... options) throws IOException {
        return Files.getPosixFilePermissions(this.path, options);
    }

    @Override
    public Path getRoot() {
        return new FilePath(this.path.getRoot());
    }

    public String getShortFileName() {
        String fileName = this.path.getFileName().toString();
        int p = fileName.lastIndexOf('.');
        if (p == -1) {
            return fileName;
        }
        return fileName.substring(0, p);
    }

    // public Path createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs)throws IOException

    // public Path createLink(Path link, Path existing) throws IOException

    /**
     * converts this file's size to string, decimals 2
     *
     * @return : String : converted size as string
     */
    public String getSizeString() throws IOException {
        return IOGeneralFile.convertSize2String(this.size());
    }

    /**
     * converts a size in bytes to kB, MB, GB, TB with suffix
     *
     * @param conversion : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
     * @param decimals : int : number of decimals to show
     *
     * @return : String : converted size with size suffix
     * @throws FileNotFoundException
     */
    public String getSizeString(ByteTo conversion, final int decimals) throws IOException {
        return IOGeneralFile.convertSize2String(this.size(), conversion, decimals);
    }

    /**
     * autoconverts a size in bytes to kB, MB, GB, TB with a suffix
     *
     * @param decimals : int : number of decimals to show
     *
     * @return : String : converted size with size suffix
     * @throws FileNotFoundException
     */
    public String getSizeString(int decimals) throws IOException {
        return IOGeneralFile.convertSize2String(this.size(), decimals);
    }

    public Icon getSmallIcon() {
        return FilePath.grabber.getSmallIcon(this.toFile());
    }

    @Override
    public int hashCode() {
        return this.path.hashCode();
    }

    @Override
    public boolean isAbsolute() {
        return this.path.isAbsolute();
    }

    public boolean isDirectory(LinkOption... options) {
        return Files.isDirectory(this.path, options);
    }

    public boolean isExecutable() {
        return Files.isExecutable(this.path);
    }

    public boolean isHidden() throws IOException {
        return Files.isHidden(this.path);
    }

    public boolean isReadable() {
        return Files.isReadable(this.path);
    }

    public boolean isRegularFile(LinkOption... options) {
        return Files.isRegularFile(this.path, options);
    }

    public boolean isSameFile(Path otherPath) throws IOException {
        return Files.isSameFile(this.path, otherPath);
    }

    public boolean isSymbolicLink() {
        return Files.isSymbolicLink(this.path);
    }

    public boolean isWritable() {
        return Files.isWritable(this.path);
    }

    @Override
    public Iterator<Path> iterator() {
        return this.path.iterator();
    }

    public FilePath moveFrom(Path source, CopyOption... options) throws IOException {
        return new FilePath(Files.move(source, this, options));
    }

    public FilePath moveTo(Path target, CopyOption... options) throws IOException {
        return new FilePath(Files.move(this, target, options));
    }

    public BufferedInputStream newBufferedInputStream(OpenOption... options) throws IOException {
        return new BufferedInputStream(this.newInputStream(options));
    }

    public BufferedOutputStream newBufferedOutputStream(OpenOption... options) throws IOException {
        return new BufferedOutputStream(this.newOutputStream(options));
    }

    public BufferedReader newBufferedReader(Charset cs) throws IOException {
        return Files.newBufferedReader(this.path, cs);
    }

    public BufferedWriter newBufferedWriter(Charset cs, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(this.path, cs, options);
    }

    public SeekableByteChannel newByteChannel(OpenOption... options) throws IOException {
        return Files.newByteChannel(this.path, options);
    }

    public SeekableByteChannel newByteChannel(Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return Files.newByteChannel(this.path, options, attrs);
    }

    public DirectoryStream<Path> newDirectoryStream() throws IOException {
        return Files.newDirectoryStream(this.path);
    }

    public DirectoryStream<Path> newDirectoryStream(DirectoryStream.Filter<? super Path> filter) throws IOException {
        return Files.newDirectoryStream(this.path, filter);
    }

    public DirectoryStream<Path> newDirectoryStream(String glob) throws IOException {
        return Files.newDirectoryStream(this.path, glob);
    }

    public InputStream newInputStream(OpenOption... options) throws IOException {
        return Files.newInputStream(this.path, options);
    }

    public OutputStream newOutputStream(OpenOption... options) throws IOException {
        return Files.newOutputStream(this.path, options);
    }

    @Override
    public Path normalize() {
        return new FilePath(this.path.normalize());
    }

    public boolean notExists(LinkOption... options) {
        return Files.notExists(this.path, options);
    }

    public String probeContentType() throws IOException {
        return Files.probeContentType(this.path);
    }

    public InputStream read() throws IOException {
        return this.newInputStream();
    }

    public byte[] readAllBytes() throws IOException {
        return Files.readAllBytes(this.path);
    }

    public ByteBuffer readAllBytesToBuffer() throws IOException {
        return ByteBuffer.wrap(Files.readAllBytes(this.path));
    }

    public List<String> readAllLines(Charset cs) throws IOException {
        return Files.readAllLines(this.path, cs);
    }

    public <A extends BasicFileAttributes> A readAttributes(Class<A> type, LinkOption... options) throws IOException {
        return Files.readAttributes(this.path, type, options);
    }

    public Map<String, Object> readAttributes(String attributes, LinkOption... options) throws IOException {
        return Files.readAttributes(this.path, attributes, options);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            this.path = Paths.get(new URI(in.readUTF()));
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }

    public FilePath readSymbolicLink() throws IOException {
        return new FilePath(Files.readSymbolicLink(this.path));
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
        return this.path.register(watcher, events);
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        return this.path.register(watcher, events, modifiers);
    }

    @Override
    public Path relativize(Path other) {
        return new FilePath(this.path.relativize(other));
    }

    @Override
    public Path resolve(Path other) {
        return new FilePath(this.path.resolve(other));
    }

    @Override
    public Path resolve(String other) {
        return new FilePath(this.path.resolve(other));
    }

    @Override
    public Path resolveSibling(Path other) {
        return new FilePath(this.path.resolveSibling(other));
    }

    @Override
    public Path resolveSibling(String other) {
        return new FilePath(this.path.resolveSibling(other));
    }

    public FilePath setAttribute(String attribute, Object value, LinkOption... options) throws IOException {
        return new FilePath(Files.setAttribute(this.path, attribute, value, options));
    }

    public FilePath setLastModifiedTime(FileTime time) throws IOException {
        return new FilePath(Files.setLastModifiedTime(this.path, time));
    }

    public FilePath setOwner(UserPrincipal owner) throws IOException {
        return new FilePath(Files.setOwner(this.path, owner));
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public FilePath setPosixFilePermissions(Set<PosixFilePermission> perms) throws IOException {
        return new FilePath(Files.setPosixFilePermissions(this.path, perms));
    }

    public long size() throws IOException {
        return Files.size(this.path);
    }

    @Override
    public boolean startsWith(Path other) {
        return this.path.startsWith(other);
    }

    @Override
    public boolean startsWith(String other) {
        return this.path.startsWith(other);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return new FilePath(this.path.subpath(beginIndex, endIndex));
    }

    @Override
    public Path toAbsolutePath() {
        return new FilePath(this.path.toAbsolutePath());
    }

    @Override
    public File toFile() {
        return this.path.toFile();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return new FilePath(this.path.toRealPath(options));
    }

    @Override
    public String toString() {
        return this.path.toString();
    }

    @Override
    public URI toUri() {
        return this.path.toUri();
    }

    public FilePath walkFileTree(FileVisitor<? super Path> visitor) throws IOException {
        return new FilePath(Files.walkFileTree(this.path, visitor));
    }

    public FilePath walkFileTree(Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) throws IOException {
        return new FilePath(Files.walkFileTree(this.path, options, maxDepth, visitor));
    }

    public FilePath write(byte[] bytes, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.path, bytes, options));
    }

    public FilePath write(ByteBuffer bytes, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.path, bytes.array(), options));
    }

    public long write(InputStream in) throws IOException {
        return this.copy(in);
    }

    public FilePath write(Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.path, lines, cs, options));
    }

    public FilePath write(String text, Charset charset, OpenOption... options) throws IOException {
        return new FilePath(Files.write(this.path, text.getBytes(charset == null ? Charset.defaultCharset() : charset), options));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.path.toUri().toString());
    }
}
