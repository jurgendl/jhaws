package org.jhaws.common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jhaws.common.io.Utils.OSGroup;

/**
 * static and string based functionality that java.io.File lacks for files and directories.<br>
 *
 * @author Jurgen
 * @version 7 March 2006
 *
 * @param <T> DOC_ME
 *
 * @see util.io.IOGeneralFile
 * @see util.io.IODirectory
 */
@Deprecated
public class IOGeneralFile<T extends IOGeneralFile<T>> extends File {
    public static enum Replace {
        ALWAYS, NEWER_AND_NON_EXISTING, ONLY_NEWER, ONLY_NOT_EXISTING;
    }

    public static enum Search {
        CLASSPATH_ONLY, EVERYWHERE, LIBRARYPATH_ONLY;
    }

    /**
     * {@link #checkFileIndex(String, String, String, String, String)} but with separator set to '_' and format to '0000'
     *
     * @param path : String : the location (path only) of the target file
     * @param outFileName : String : the name of the target file (without extension and . before extension)
     * @param extension : String : the extension of the target file (without . before extension), see class constants FORMAT... for possibilities
     *
     * @return : IOFile : new indexed File
     */
    public static IOFile checkFileIndex(final String path, final String outFileName, final String extension) {
        return IOGeneralFile.checkFileIndex(path, outFileName, "_", "0000", extension); //$NON-NLS-1$ //$NON-NLS-2$
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
     * @param path : String : the location (path only) of the target file
     * @param outFileName : String : the name of the target file (without extension and . before extension)
     * @param sep : String : characters sperating filename from index (example: _ )
     * @param format : String : number of positions character 0 (example: 0000 )
     * @param extension : String : the extension of the target file (without . before extension), see class constants FORMAT... for possibilities
     *
     * @return : IOFile : new indexed File
     */
    public static IOFile checkFileIndex(final String path, String outFileName, final String sep, final String format, final String extension) {
        String SEPARATOR = sep;
        String FORMAT = sep + format;
        IOFile file = "".equals(extension) ? new IOFile(path + File.separator + outFileName) //$NON-NLS-1$
                : new IOFile(path + File.separator + outFileName + "." + extension); //$NON-NLS-1$

        if (file.exists()) {
            if (outFileName.length() <= FORMAT.length()) {
                outFileName = outFileName + FORMAT;

                if (extension.equals("")) { //$NON-NLS-1$
                    file = new IOFile(path + File.separator + outFileName);
                } else {
                    file = new IOFile(path + File.separator + outFileName + "." + extension); //$NON-NLS-1$
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

                    if (extension.equals("")) { //$NON-NLS-1$
                        file = new IOFile(path + File.separator + outFileName);
                    } else {
                        file = new IOFile(path + File.separator + outFileName + "." + extension); //$NON-NLS-1$
                    }
                }
            }
        }

        int ind = 0;

        while (file.exists()) {
            StringBuilder indStringSB = new StringBuilder(16);

            int nr0 = FORMAT.length() - SEPARATOR.length() - String.valueOf(ind).length();

            for (int i = 0; i < nr0; i++) {
                indStringSB.append("0"); //$NON-NLS-1$
            }

            indStringSB.append(String.valueOf(ind));

            if (extension.equals("")) { //$NON-NLS-1$
                file = new IOFile(path + File.separator + outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR
                        + indStringSB.toString());
            } else {
                file = new IOFile(path + File.separator + outFileName.substring(0, outFileName.length() - FORMAT.length()) + SEPARATOR
                        + indStringSB.toString() + "." + extension); //$NON-NLS-1$
            }

            ind++;
        }

        return file;
    }

    /**
     * checks filename versus operating system restrictions
     *
     * @param filename : String : filename
     * @param os : int : operating system (use OS.DOS, OS.WIN32, OS.UNIX)
     *
     * @return : boolean : filename is valid or not
     * @throws IOException
     */
    public static boolean checkFilenameVS(String filename, final OSGroup os) throws IOException {
        filename = new File(filename).getName();

        if (filename.substring(0, 1).compareTo(".") == 0) { //$NON-NLS-1$

            return false;
        }

        char[] c = new char[0];
        char[] fn = filename.toCharArray();

        if (os == OSGroup.Dos) {
            String[] parts = filename.split("\\."); //$NON-NLS-1$

            if (parts.length != 2) {
                return false;
            }

            if ((parts[0].length() > 8) || (parts[0].length() == 0) || (parts[1].length() > 3) || (parts[1].length() == 0)) {
                return false;
            }

            c = Utils.legal(OSGroup.Dos);
        }

        if (os == OSGroup.Windows) {
            if (filename.length() > 255) {
                return false;
            }

            c = Utils.legal(OSGroup.Windows);
        }

        if (os == OSGroup.Nix) {
            if (filename.length() > 100) {
                return false;
            }

            c = Utils.legal(OSGroup.Nix);
        }

        if (c.length == 0) {
            throw new IOException("characters for OS not found"); //$NON-NLS-1$
        }

        for (char element : fn) {
            boolean chk = false;

            for (int j = 0; (j < c.length) && !chk; j++) {
                if (element == c[j]) {
                    chk = true;
                }
            }

            if (!chk) {
                return false;
            }
        }

        return true;
    }

    /**
     * checks if not null in constructor
     *
     * @param file : File : file
     *
     * @return : File : file
     *
     * @throws IllegalArgumentException : exception thrown
     */
    protected static File checkNotNull(final File file) throws IllegalArgumentException {
        if (file == null) {
            throw new IllegalArgumentException("File parameter cannot be null"); //$NON-NLS-1$
        }

        return file;
    }

    /**
     * checks if not null in constructor
     *
     * @param object : Object : any object
     *
     * @return : Object : same object as given
     *
     * @throws IllegalArgumentException : exception thrown
     */
    protected static Object checkNotNull(final Object object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException("parameter object cannot be null"); //$NON-NLS-1$
        }

        return object;
    }

    /**
     * converts a size in bytes to kB, MB, GB, TB
     *
     * @param size : long : size (file size)
     * @param conversion : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
     * @param decimals : int : number of decimals to show
     *
     * @return : double : converted size
     */
    public static double convertSize(final long size, final ByteTo conversion, final int decimals) {
        return IOGeneralFile.round(conversion.doubleValue() * size, decimals);
    }

    /**
     * converts this file's size to string, decimals 2
     *
     * @param length : long : size (file size)
     *
     * @return : String : converted size as string
     */
    public static String convertSize2String(final long length) {
        return IOGeneralFile.convertSize2String(length, IOGeneralFile.getNearest(length), 2);
    }

    /**
     * converts a size in bytes to kB, MB, GB, TB with suffix with two decimals
     *
     * @param length : long : size (file size)
     * @param conversion : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
     *
     * @return : String : converted size with size suffix
     */
    public static String convertSize2String(final long length, final ByteTo conversion) {
        return IOGeneralFile.convertSize2String(length, conversion, 2);
    }

    /**
     * converts a size in bytes to kB, MB, GB, TB with suffix
     *
     * @param length : long : size (file size)
     * @param conversion : int : use BYTE_TO_KiloByte, BYTE_TO_MegaByte, BYTE_TO_GigaByte, BYTE_TO_TerraByte
     * @param decimals : int : number of decimals to show
     *
     * @return : String : converted size with size suffix
     */
    public static String convertSize2String(final long length, final ByteTo conversion, final int decimals) {
        if (conversion == ByteTo.Byte) {
            return (int) (conversion.doubleValue() * length) + " " + conversion.display(); //$NON-NLS-1$
        }

        return IOGeneralFile.toString(IOGeneralFile.round(conversion.doubleValue() * length, decimals)) + " " //$NON-NLS-1$
                + conversion.display();
    }

    /**
     * autoconverts a size in bytes to kB, MB, GB, TB with a suffix
     *
     * @param length : long : size (file size)
     * @param decimals : int : number of decimals to show
     *
     * @return : String : converted size with size suffix
     */
    public static String convertSize2String(final long length, final int decimals) {
        return IOGeneralFile.convertSize2String(length, IOGeneralFile.getNearest(length), decimals);
    }

    /**
     * checks if a file exists somewhere in the classpath, library path or working directory
     *
     * @param file : File : file
     *
     * @return : boolean : exists or not
     */
    public static boolean fileExists(final String file) {
        return IOGeneralFile.fileExists(file, Search.EVERYWHERE);
    }

    /**
     * checks if a file exists somewhere in the classpath, library path or working directory
     *
     * @param file : File : file
     * @param parameter : int : use FileConv.EVERYWHERE, FileConv.CLASSPATH_ONLY or FileConv.LIBRARYPATH_ONLY
     *
     * @return : boolean : exists or not
     */
    public static boolean fileExists(final String file, final Search parameter) {
        IOFile tmp = IOGeneralFile.searchFile(file, parameter);

        if (tmp == null) {
            return false;
        }

        return true;
    }

    /**
     * na
     *
     * @param file na
     *
     * @return na
     */
    private static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * get current directory
     *
     * @return : IODirectory : current directory object
     */
    public static IODirectory getCurrentDirectory() {
        return new IODirectory(""); //$NON-NLS-1$
    }

    /**
     * if location is a file, returns the directory where it is in; if it is a directory, returns the directory; uses system default file separator;
     * String ends with system default file separator; location does not need to exist already
     *
     * @param location : String : filesystem location
     *
     * @return : String : directory
     */
    public static String getDirectory(final String location) {
        if ((location == null) || (location.length() == 0)) {
            return ""; //$NON-NLS-1$
        }

        String lastchar = location.substring(location.length() - 1, location.length());

        if (lastchar.equals("/") || lastchar.equals("\\")) { //$NON-NLS-1$ //$NON-NLS-2$

            return (new File(location) + File.separator).replaceAll("\\\\\\\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        File tmp = new File(location);

        if (tmp.isDirectory()) {
            IODirectory d = new IODirectory(tmp);

            return d.getDirectory().getAbsolutePath();
        }

        IOFile f = new IOFile(tmp);

        return f.getDirectory().getAbsolutePath();
    }

    /**
     * gets the extension of a file ("" when no extension)
     *
     * @param fileName : String : filename or complete file path
     *
     * @return : String : extension
     */
    public static String getExtension(final String fileName) {
        if ((fileName == null) || (fileName.length() == 0)) {
            return ""; //$NON-NLS-1$
        }

        int pos = fileName.lastIndexOf("."); //$NON-NLS-1$

        if (pos == -1) {
            return ""; //$NON-NLS-1$
        }

        int pos2 = fileName.lastIndexOf("/"); //$NON-NLS-1$
        int pos3 = fileName.lastIndexOf("\\"); //$NON-NLS-1$

        if ((pos2 > pos) || (pos3 > pos)) {
            return ""; //$NON-NLS-1$
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()); //$NON-NLS-1$
    }

    /**
     * gets the file without path (if location is a directory, returns ""); location does not need to exist already
     *
     * @param location : String : filesystem location
     *
     * @return : String : file
     */
    public static String getFile(final File location) {
        if (location == null) {
            return ""; //$NON-NLS-1$
        }

        if (location.exists()) {
            if (location.isDirectory()) {
                return ""; //$NON-NLS-1$
            }

            return location.getName();
        }

        String[] l = location.getAbsolutePath().replaceAll("\\\\", "/").split("/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if (l[l.length - 1].indexOf(".") != -1) { //$NON-NLS-1$

            return location.getName();
        }

        return ""; //$NON-NLS-1$
    }

    /**
     * gets the file without path (if location is a directory, returns ""); location does not need to exist already
     *
     * @param location : String : filesystem location
     *
     * @return : String : file
     */
    public static String getFile(final String location) {
        if ((location == null) || (location.length() == 0)) {
            return ""; //$NON-NLS-1$
        }

        String lastchar = location.substring(location.length() - 1, location.length());

        if ((lastchar.compareTo("/") == 0) || (lastchar.compareTo("\\") == 0)) { //$NON-NLS-1$ //$NON-NLS-2$

            return ""; //$NON-NLS-1$
        }

        return IOGeneralFile.getFile(new File(location));
    }

    /**
     * na
     *
     * @param length na
     *
     * @return
     */
    public static ByteTo getNearest(final long length) {
        if ((ByteTo.TerraByte.doubleValue() * length) >= 1.0d) {
            return ByteTo.GigaByte;
        }

        if ((ByteTo.GigaByte.doubleValue() * length) >= 1.0d) {
            return ByteTo.GigaByte;
        }

        if ((ByteTo.MegaByte.doubleValue() * length) >= 1.0d) {
            return ByteTo.MegaByte;
        }

        if ((ByteTo.KiloByte.doubleValue() * length) >= 1.0d) {
            return ByteTo.KiloByte;
        }

        return ByteTo.Byte;
    }

    /**
     * gets the filename without the extension and the pathname
     *
     * @param file : File : file
     *
     * @return : String : short filename
     */
    public static String getShortFile(final File file) {
        return IOGeneralFile.getShortFile(file.getName());
    }

    /**
     * gets the filename without the extension and the pathname
     *
     * @param name : String : filename
     *
     * @return : String : short filename
     */
    public static String getShortFile(String name) {
        name = name.replace('\\', '/');

        int p1 = name.lastIndexOf("/"); //$NON-NLS-1$

        if (p1 == -1) {
            p1 = 0;
        } else {
            p1++;
        }

        int p2 = name.lastIndexOf('.');

        if (p2 == -1) {
            p2 = name.length();
        }

        name = name.substring(p1, p2);

        return name;
    }

    /**
     * is location a directory or not
     *
     * @param location : String : location
     *
     * @return : boolean
     */
    public static boolean isDirectory(final File location) {
        if (location == null) {
            return false;
        }

        String s1 = IOGeneralFile.getDirectory(location.getAbsolutePath());
        String s2 = location.getAbsolutePath();

        if (s1.compareToIgnoreCase(s2) == 0) {
            return true;
        }

        return false;
    }

    /**
     * is location a directory or not
     *
     * @param location : File : location
     *
     * @return : boolean
     */
    public static boolean isDirectory(final String location) {
        if ((location == null) || (location.length() == 0)) {
            return false;
        }

        String s1 = new File(IOGeneralFile.getDirectory(location)).getAbsolutePath();
        String s2 = new File(location).getAbsolutePath();

        if (s1.compareToIgnoreCase(s2) == 0) {
            return true;
        }

        return false;
    }

    /**
     * is location a file or not
     *
     * @param location : File : location
     *
     * @return : boolean
     */
    public static boolean isFile(final File location) {
        if (location == null) {
            return false;
        }

        String s1 = IOGeneralFile.getFile(location);
        String s2 = location.getName();

        if ((s2.length() > 0) && (s1.compareToIgnoreCase(s2) == 0)) {
            return true;
        }

        return false;
    }

    /**
     * is location a file or not
     *
     * @param location : String : location
     *
     * @return : boolean
     */
    public static boolean isFile(final String location) {
        if ((location == null) || (location.length() == 0)) {
            return false;
        }

        String s1 = IOGeneralFile.getFile(location);
        String s2 = new File(location).getName();

        if ((s2.length() > 0) && (s1.compareToIgnoreCase(s2) == 0)) {
            return true;
        }

        return false;
    }

    /**
     * checks if a file is in the classpath, not only in a directory in a classpath dir but as a file in the classpath
     *
     * @param fileName : String : file to search for
     *
     * @return : boolean : found or not
     */
    public static boolean isFileInClasspath(final String fileName) {
        String[] cp = System.getProperty("java.class.path").split(";"); //$NON-NLS-1$ //$NON-NLS-2$

        for (String element : cp) {
            String[] cps = element.split("\\\\"); //$NON-NLS-1$

            if (cps[cps.length - 1].compareToIgnoreCase(fileName) == 0) {
                return true;
            }
        }

        return false;
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
    public static String legalizeFilenameVs(String filename, final OSGroup os) throws IOException {
        filename = new File(filename).getName();

        while (filename.substring(0, 1).compareTo(".") == 0) { //$NON-NLS-1$
            filename = filename.substring(1, filename.length());
        }

        char[] c = new char[0];

        if (os == OSGroup.Dos) {
            String[] parts = filename.split("\\."); //$NON-NLS-1$

            if (parts.length == 1) {
                if (filename.substring(filename.length() - 1, filename.length()).compareTo(".") != 0) { //$NON-NLS-1$
                    filename = filename + "."; //$NON-NLS-1$
                }

                filename = filename + "ext"; //$NON-NLS-1$
            }

            if (parts.length > 2) {
                filename = parts[0] + "." + parts[parts.length - 1]; //$NON-NLS-1$
            }

            parts = filename.split("\\."); //$NON-NLS-1$

            if (parts[0].length() > 8) {
                parts[0] = parts[0].substring(0, 8);
            }

            if (parts[1].length() > 3) {
                parts[1] = parts[1].substring(0, 3);
            }

            filename = parts[0] + "." + parts[1]; //$NON-NLS-1$
            c = Utils.legal(OSGroup.Dos);
        }

        if (os == OSGroup.Windows) {
            c = Utils.legal(OSGroup.Windows);
        }

        if (os == OSGroup.Nix) {
            c = Utils.legal(OSGroup.Nix);
        }

        if (c.length == 0) {
            throw new IOException("characters for OS not found"); //$NON-NLS-1$
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

        return new String(fn);
    }

    /**
     * makes sure a file path string with mixed seprator characters is a correct one, replaces the / and \ character and any combination of them (fe
     * /\ or \/) by the default system separator
     *
     * @param mixedFilePathString : String : mixed string
     *
     * @return : String : file path with correct separator
     */
    public static String makeSystemSeparator(final String mixedFilePathString) {
        return IOGeneralFile.makeSystemSeparator(mixedFilePathString, File.separator);
    }

    /**
     * makes sure a file path string with mixed seprator characters is a correct one, replaces the / and \ character and any combination of them (fe
     * /\ or \/) by the given separator
     *
     * @param mixedFilePathString : String : mixed string
     * @param sep : String : separator character (normally either / or \ )
     *
     * @return : String : file path with correct separator
     */
    public static String makeSystemSeparator(final String mixedFilePathString, final String sep) {
        if ((mixedFilePathString == null) || (mixedFilePathString.length() == 0)) {
            return ""; //$NON-NLS-1$
        }

        char[] ca = mixedFilePathString.toCharArray();
        StringBuilder sb = new StringBuilder(48);
        boolean lastWasSeparator = false;

        for (char element : ca) {
            if ((element == '\\') || (element == '/')) {
                if (!lastWasSeparator) {
                    sb.append(sep);
                    lastWasSeparator = true;
                }
            } else {
                lastWasSeparator = false;
                sb.append(element);
            }
        }

        return sb.toString();
    }

    /**
     * na
     *
     * @param s na
     *
     * @return
     */
    private static String replacer(String s) {
        String tmp = s.replace('\\', '/').replaceAll("/\\Q.\\E/", "/").replaceAll("//", "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        if (tmp.endsWith("/.")) { //$NON-NLS-1$
            tmp = tmp.substring(0, tmp.length() - 2);
        }

        if (tmp.endsWith("/")) { //$NON-NLS-1$

            if (!tmp.equals("/")) { //$NON-NLS-1$
                tmp = tmp.substring(0, tmp.length() - 1);
            }
        }

        if (tmp.endsWith(":")) { //$NON-NLS-1$
            tmp = tmp + "/"; //$NON-NLS-1$
        }

        return tmp;
    }

    /**
     * na
     *
     * @param d na
     * @param t na
     *
     * @return
     */
    protected static double round(final double d, final int t) {
        int tt = (int) java.lang.Math.pow(10, t);

        return (double) java.lang.Math.round(d * tt) / tt;
    }

    /**
     * cfr function searchFile(String, int) but searches everywhere
     *
     * @param file : String : file name without path
     *
     * @return : IOFile : IOFile file or <i>null</i> when not found
     */
    public static IOFile searchFile(final String file) {
        return IOGeneralFile.searchFile(file, Search.EVERYWHERE);
    }

    /**
     * searches for a file in the current directory, the current user dir, the complete class path and the complete library path in this order, return
     * the complete path from the moment when found or null when not found
     *
     * @param file : String : file name without path
     * @param parameter : int : file name with path where first found, EVERYWHERE, CLASSPATH_ONLY, LIBRARYPATH_ONLY
     *
     * @return : IOFile : IOFile file or <i>null</i> when not found
     */
    public static IOFile searchFile(final String file, final Search parameter) {
        IOGeneralFile.checkNotNull(file);

        if (new File(file).exists()) {
            return new IOFile(file);
        }

        String tmp = ""; //$NON-NLS-1$

        if (parameter != Search.EVERYWHERE) {
            tmp = IOGeneralFile.searchFileInPath(System.getProperty("user.dir"), file); //$NON-NLS-1$

            if (tmp != null) {
                return new IOFile(tmp);
            }

            tmp = IOGeneralFile.searchFileInPath(System.getProperty("user.home"), file); //$NON-NLS-1$

            if (tmp != null) {
                return new IOFile(tmp);
            }

            tmp = IOGeneralFile.searchFileInPath(IODirectory.TEMPDIR.getAbsolutePath(), file);

            if (tmp != null) {
                return new IOFile(tmp);
            }
        }

        if (parameter != Search.LIBRARYPATH_ONLY) {
            String[] cp = System.getProperty("java.class.path").split(";"); //$NON-NLS-1$ //$NON-NLS-2$

            for (String element : cp) {
                tmp = IOGeneralFile.searchFileInPath(element, file);

                if (tmp != null) {
                    return new IOFile(tmp);
                }
            }
        }

        if (parameter != Search.CLASSPATH_ONLY) {
            String[] lp = System.getProperty("java.library.path").split(";"); //$NON-NLS-1$ //$NON-NLS-2$

            for (String element : lp) {
                tmp = IOGeneralFile.searchFileInPath(element, file);

                if (tmp != null) {
                    return new IOFile(tmp);
                }
            }
        }

        return null;
    }

    /**
     * cfr function searchFile(String, int) but searches everywhere
     *
     * @param file : String : file name without path
     *
     * @return : String : complete file path or <i>null</i> when not found
     */
    public static String searchFile2String(final String file) {
        IOFile tmp = IOGeneralFile.searchFile(file);

        if (tmp == null) {
            return null;
        }

        return tmp.getAbsolutePath();
    }

    /**
     * cfr function searchFile(String, int) but searches everywhere
     *
     * @param file : String : file name without path
     * @param parameter : int : file name with path where first found, EVERYWHERE, CLASSPATH_ONLY, LIBRARYPATH_ONLY
     *
     * @return : String : complete file path or <i>null</i> when not found
     */
    public static String searchFile2String(final String file, final Search parameter) {
        IOFile tmp = IOGeneralFile.searchFile(file, parameter);

        if (tmp == null) {
            return null;
        }

        return tmp.getAbsolutePath();
    }

    /**
     * used internally by function {@link #searchFile(String)}
     *
     * @param path : String : full filepath without filename
     * @param file : String : filename withou path
     *
     * @return : boolean : exists or not
     */
    protected static String searchFileInPath(final String path, final String file) {
        if (file == null) {
            return null;
        }

        int pos1 = path.lastIndexOf("/"); //$NON-NLS-1$
        int pos2 = path.lastIndexOf("\\"); //$NON-NLS-1$
        int pos = 1;

        if (pos1 > pos2) {
            pos += pos1;
        } else {
            pos += pos2;
        }

        if (path.substring(pos, path.length()).compareTo(file) == 0) {
            return path;
        }

        String tmp = path + File.separator + file;

        if (new File(tmp).exists()) {
            return tmp;
        }

        return null;
    }

    /**
     * double to string
     *
     * @param round
     *
     * @return
     */
    protected static String toString(double round) {
        return IOGeneralFile.instance.format(round);
    }

    private static final long serialVersionUID = 7931119843811634983L;

    /** file date comparator */
    protected static Comparator<IOGeneralFile<?>> fileDateComparator = new FileDateComparator();

    /** file size comparator */
    protected static Comparator<IOFile> fileSizeComparator = new FileSizeComparator();

    /** constants for overwrite file dialog box: possible options */
    protected static final String[] overwriteOptions = new String[] {
            "yes", //$NON-NLS-1$
            "no", //$NON-NLS-1$
            "yes to all", //$NON-NLS-1$
            "no to all" //$NON-NLS-1$
    };

    /** na */
    private static final NumberFormat instance = NumberFormat.getInstance(Locale.getDefault());

    static {
        IOGeneralFile.instance.setMaximumFractionDigits(20);
        IOGeneralFile.instance.setRoundingMode(RoundingMode.UNNECESSARY);
    }

    /** constants for overwrite file dialog box: what option is chosen */
    protected String overwriteOptionChosen = null;

    /**
     * Creates a new IoGeneralFile object.
     *
     * @param file : File : file or directory
     */
    public IOGeneralFile(final File file) {
        this(IOGeneralFile.getCanonicalPath(IOGeneralFile.checkNotNull(file)));
    }

    /**
     * Creates a new IOGeneralFile object.
     *
     * @param parent : File : directory
     * @param child : String : short name of file or directory
     */
    public IOGeneralFile(final File parent, final String child) {
        this(IOGeneralFile.getCanonicalPath(new File(IOGeneralFile.checkNotNull(parent), IOGeneralFile.checkNotNull(child).toString())));
    }

    /**
     * Creates a new IOGeneralFile object.
     *
     * @param pathname : String : full path name of file or directory; when only a name is given, the file or directory will be in the current working
     *            directory
     */
    public IOGeneralFile(final String pathname) {
        super(IOGeneralFile.getCanonicalPath(new File(IOGeneralFile.replacer(IOGeneralFile.checkNotNull(pathname).toString()))));
    }

    /**
     * checks if exists
     *
     * @return
     * @throws FileNotFoundException
     */
    @SuppressWarnings({ "unchecked" })
    public T checkExistence() throws FileNotFoundException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.getAbsolutePath());
        }

        return (T) this;
    }

    /**
     * checks if not exists
     *
     * @return
     * @throws FileNotFoundException
     *
     */
    @SuppressWarnings({ "unchecked" })
    public T checkNonExistence() throws FileNotFoundException {
        if (this.exists()) {
            throw new FileNotFoundException(this.getAbsolutePath());
        }

        return (T) this;
    }

    /**
     * @see java.io.File#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IOGeneralFile)) {
            return false;
        }
        IOGeneralFile<?> castOther = (IOGeneralFile<?>) other;
        return new EqualsBuilder().append(this.getAbsolutePath(), castOther.getAbsolutePath()).isEquals();
    }

    /**
     *
     * @see java.io.File#getAbsolutePath()
     */
    @Override
    public String getAbsolutePath() {
        return super.getAbsolutePath();
    }

    /**
     * na
     *
     * @return
     */
    @SuppressWarnings("restriction")
    public Icon getLargeIcon() {
        try {
            return new ImageIcon(sun.awt.shell.ShellFolder.getShellFolder(this).getIcon(true));
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * see File.length()
     *
     * @return see File.length()
     *
     * @see java.io.File#length()
     */
    public long getLength() {
        return this.length();
    }

    /**
     * get parent directory
     *
     * @return : IODirectory : parent directory
     */
    public IODirectory getParentDirectory() {
        File p = this.getParentFile();

        return (p == null) ? null : new IODirectory(p);
    }

    /**
     *
     * @see java.io.File#getPath()
     */
    @Override
    public String getPath() {
        return super.getPath();
    }

    /**
     * get disk root for this file or directory
     *
     * @return
     */
    public IODirectory getRoot() {
        String path = this.getAbsolutePath();

        if (path.startsWith("/")) {
            return new IODirectory("/");
        }
        return new IODirectory(path.charAt(0) + ":/");
    }

    /**
     * gets the extension from given file and maps it to an image icon (extracted from the system)
     *
     * @return : Icon
     */
    public Icon getSmallIcon() {
        return SystemIcon.getSystemIcon(new FilePath(this));
    }

    /**
     * @see java.io.File#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1941084191, -565286407).append(this.getAbsolutePath()).toHashCode();
    }

    /**
     *
     * @see java.io.File#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @see java.io.File#toURI()
     */
    @Override
    public URL toURL() {
        try {
            return super.toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new Error(ex);
        }
    }
}
