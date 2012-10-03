package org.jhaws.common.io;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * OSUtils
 */
public class IOnOSUtils {
    public static enum BIT {
        _32, _64, unknown;
    }

    /**
     * byte buffer
     */
    public static class ByteBuffer {
        /** buffer */
        private ByteArrayOutputStream buffer;

        /**
         * Creates a new Buffer object.
         */
        public ByteBuffer() {
            this.reset();
        }

        /**
         * Creates a new Buffer object.
         * 
         * @param data
         */
        public ByteBuffer(byte[] data) {
            this.setBytes(data);
        }

        /**
         * get buffered data
         * 
         * @return
         */
        public byte[] getBytes() {
            return this.buffer.toByteArray();
        }

        /**
         * get OutputStream
         * 
         * @return
         */
        public OutputStream getOutputStream() {
            return this.buffer;
        }

        /**
         * new InputStream
         * 
         * @return
         */
        public InputStream newInputStream() {
            return new ByteArrayInputStream(this.getBytes());
        }

        /**
         * reset data
         */
        public void reset() {
            this.reset(IOnOSUtils.DEFAULT_BUFFER_LEN);
        }

        private void reset(int length) {
            this.buffer = new ByteArrayOutputStream(length);
        }

        /**
         * set buffer data
         * 
         * @param data
         * 
         * @throws RuntimeException
         */
        public void setBytes(byte[] data) {
            try {
                this.reset(data.length);
                this.buffer.write(data);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static enum OS {
        AIX, Digital_Unix, FreeBSD, HP_UX, Irix, Linux, MPE_iX, Mac_OS, Mac_OS_X, Netware_4_11, OS_2, Solaris, Windows_2000, Windows_7, Windows_95,
        Windows_98, Windows_NT, Windows_Vista, Windows_XP, unknown;
    }

    public static enum OS_GROUP {
        Mac, Nix, Windows, unknown;
    }

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(IOnOSUtils.class);

    /** 8kB */
    public static final int DEFAULT_BUFFER_LEN = 1024 * 8;

    /** 32/64 bit */
    public static final BIT bit;

    /** see enum */
    public static final OS os;

    /** see enum */
    public static final OS_GROUP osgroup;

    /** nix /usr/lib/ */
    public static final File NIX_LIB;

    /** default open file commands for windows */
    public static final Map<String, String> WIN_FILE_OPEN_CMDS = new HashMap<String, String>();

    /** file extension map for windows */
    public static final Map<String, String> WIN_FILE_EXTS = new HashMap<String, String>();

    /** windows specific dir */
    public static final String WINDIR_PROGRAM_FILES;

    /** windows specific dir */
    public static final String WINDIR_SYSTEM_ROOT;

    /** windows specific dir */
    public static final String WINDIR_WINDIR;

    /** windows specific dir */
    public static final String WINDIR_SYSTEM32;

    /** windows specific dir */
    public static final String WINDIR_USER_PROFILE;

    static {
        String WINDIR_PROGRAM_FILES0 = "%ProgramFiles%";
        String WINDIR_SYSTEM_ROOT0 = "%SystemRoot%";
        String WINDIR_WINDIR0 = "%WinDir%";
        String WINDIR_USER_PROFILE0 = "%UserProfile%";
        String WINDIR_SYSTEM320 = "%SystemRoot%/system32";

        BIT _bit = BIT.unknown;

        try {
            _bit = BIT.valueOf("_" + System.getProperty("sun.arch.data.model"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        bit = _bit;

        OS _os = OS.unknown;

        try {
            _os = OS.valueOf(System.getProperty("os.name").replace('.', '_').replace(' ', '_').replace('/', '_'));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        os = _os;

        OS_GROUP _osgroup = OS_GROUP.unknown;

        try {
            switch (IOnOSUtils.os) {
                case Digital_Unix:
                case Linux:
                    _osgroup = OS_GROUP.Nix;

                    break;

                case Mac_OS:
                case Mac_OS_X:
                    _osgroup = OS_GROUP.Mac;

                    break;

                case Windows_2000:
                case Windows_7:
                case Windows_95:
                case Windows_98:
                case Windows_NT:
                case Windows_Vista:
                case Windows_XP:
                    _osgroup = OS_GROUP.Windows;

                    break;

                case unknown:
                case AIX:
                case FreeBSD:
                case HP_UX:
                case Irix:
                case MPE_iX:
                case Netware_4_11:
                case OS_2:
                case Solaris:
                    _osgroup = OS_GROUP.unknown;

                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        osgroup = _osgroup;

        if (IOnOSUtils.osgroup == OS_GROUP.Windows) {
            // http://technet.microsoft.com/en-us/library/bb490910.aspx
            Pattern dirProgramFilesPattern = Pattern.compile("ProgramFiles", Pattern.CASE_INSENSITIVE);
            Pattern dirSystemRootPattern = Pattern.compile("SystemRoot", Pattern.CASE_INSENSITIVE);
            Pattern dirWinDirPattern = Pattern.compile("WinDir", Pattern.CASE_INSENSITIVE);
            Pattern dirUserProfilePattern = Pattern.compile("UserProfile", Pattern.CASE_INSENSITIVE);

            try {
                WINDIR_PROGRAM_FILES0 = IOnOSUtils.capture1("cmd /c echo %ProgramFiles%");
                WINDIR_SYSTEM_ROOT0 = IOnOSUtils.capture1("cmd /c echo %SystemRoot%");
                WINDIR_WINDIR0 = IOnOSUtils.capture1("cmd /c echo %WinDir%");
                WINDIR_SYSTEM320 = WINDIR_WINDIR0 + "/system32";
                WINDIR_USER_PROFILE0 = IOnOSUtils.capture1("cmd /c echo %UserProfile%");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                for (String record : IOnOSUtils.capture("cmd /c ftype")) {
                    String[] parts = record.split("=");

                    String cmd = parts[1];

                    {
                        Matcher matcher = dirProgramFilesPattern.matcher(cmd);

                        if (matcher.find()) {
                            cmd = cmd.substring(0, matcher.start() - 1) + WINDIR_PROGRAM_FILES0 + cmd.substring(matcher.end() + 1);
                        }
                    }

                    {
                        Matcher matcher = dirSystemRootPattern.matcher(cmd);

                        if (matcher.find()) {
                            cmd = cmd.substring(0, matcher.start() - 1) + WINDIR_SYSTEM_ROOT0 + cmd.substring(matcher.end() + 1);
                        }
                    }

                    {
                        Matcher matcher = dirWinDirPattern.matcher(cmd);

                        if (matcher.find()) {
                            cmd = cmd.substring(0, matcher.start() - 1) + WINDIR_WINDIR0 + cmd.substring(matcher.end() + 1);
                        }
                    }

                    {
                        Matcher matcher = dirUserProfilePattern.matcher(cmd);

                        if (matcher.find()) {
                            cmd = cmd.substring(0, matcher.start() - 1) + WINDIR_USER_PROFILE0 + cmd.substring(matcher.end() + 1);
                        }
                    }

                    IOnOSUtils.WIN_FILE_OPEN_CMDS.put(parts[0], cmd);
                }

                for (String record : IOnOSUtils.capture("cmd /c assoc")) {
                    String[] parts = record.split("=");

                    if (parts.length == 2) {
                        IOnOSUtils.WIN_FILE_EXTS.put(parts[0].substring(1).toLowerCase(), parts[1]);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        WINDIR_PROGRAM_FILES = WINDIR_PROGRAM_FILES0;
        WINDIR_SYSTEM_ROOT = WINDIR_SYSTEM_ROOT0;
        WINDIR_WINDIR = WINDIR_WINDIR0;
        WINDIR_USER_PROFILE = WINDIR_USER_PROFILE0;
        WINDIR_SYSTEM32 = WINDIR_SYSTEM320;

        File _nix_lib = null;

        if (IOnOSUtils.osgroup == OS_GROUP.Nix) {
            _nix_lib = new File("/usr/lib/");
        }

        NIX_LIB = _nix_lib;
    }

    /**
     * voert command uit
     * 
     * @param command
     * 
     * @return
     * 
     * @throws IOException
     * 
     * @see {@link #process(String, boolean, boolean)} met true false
     */
    public static List<String> capture(String command) throws IOException {
        return IOnOSUtils.process(command, true, false);
    }

    /**
     * capture 1 line
     * 
     * @param command
     * 
     * @return
     * 
     * @throws IOException
     */
    public static String capture1(String command) throws IOException {
        return IOnOSUtils.capture(command).get(0);
    }

    /**
     * copy inputstream naar outputstream
     * 
     * @param in
     * @param out
     * 
     * @throws IOException
     * @throws NullPointerException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException, NullPointerException {
        try {
            byte[] buffer = new byte[IOnOSUtils.DEFAULT_BUFFER_LEN];
            int read;

            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                //
            }

            try {
                out.close();
            } catch (Exception ex) {
                //
            }
        }
    }

    /**
     * Kopieert een file, NIO
     * 
     * @param in
     * @param out
     * 
     * @throws IOException
     */
    public static void copyFile(File in, File out) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();

        try {
            // fix copy bestanden groter dan 64MB (zie link)
            // // magic number for Windows, 64Mb - 32Kb)
            // int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            // long size = inChannel.size();
            // long position = 0;
            // while (position < size) {
            // position +=
            // inChannel.transferTo(position, maxCount, outChannel);
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }

            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * copy classpath resource naar outputstream
     * 
     * @param path
     * @param out
     * 
     * @throws IOException
     * @throws NullPointerException
     */
    public static void copyResource(String path, OutputStream out) throws IOException, NullPointerException {
        IOnOSUtils.copy(IOnOSUtils.class.getClassLoader().getResourceAsStream(path), out);
    }

    /**
     * creeer tijdelijk bestand met extensie
     * 
     * @param extensie
     * 
     * @return
     * 
     * @throws IOException
     */
    public static File createTempFile(String extensie) throws IOException {
        return IOnOSUtils.createTempFile(new Date().toString().replace(' ', '_').replace(':', '_'), extensie);
    }

    /**
     * creeer tijdelijk bestand met extensie en naam
     * 
     * @param naam
     * @param extensie
     * 
     * @return
     * 
     * @throws IOException
     */
    public static File createTempFile(String naam, String extensie) throws IOException {
        File tempFile = File.createTempFile(naam, "." + extensie);
        tempFile.deleteOnExit();

        return tempFile;
    }

    private static void dynamic(String libname, String ext) throws IOException, FileNotFoundException {
        File dllfile = File.createTempFile(libname, "." + ext);

        if (!dllfile.exists()) {
            IOnOSUtils.copyResource(libname + "." + ext, new FileOutputStream(dllfile));
            dllfile.deleteOnExit();
        }

        System.load(dllfile.getAbsolutePath());
    }

    /**
     * load library
     * 
     * @param libname
     * 
     * @throws IOException
     * @throws NullPointerException
     * @throws UnsatisfiedLinkError
     */
    public static void dynamicLoadLibrary(String libname) throws IOException, NullPointerException, UnsatisfiedLinkError {
        IOnOSUtils.dynamicLoadLibrary(libname, null, true);
    }

    /**
     * probeer library met naam en extentie in te lezen van libpath en als dat niet lukt, schrijft tmp file vanuit classpath en leest dat in
     * 
     * @param libname
     * @param ext
     * 
     * @throws IOException
     * @throws NullPointerException
     * @throws UnsatisfiedLinkError
     */
    public static void dynamicLoadLibrary(String libname, String ext) throws IOException, NullPointerException, UnsatisfiedLinkError {
        IOnOSUtils.dynamicLoadLibrary(libname, ext, true);
    }

    /**
     * probeer library met naam en extentie in te lezen van libpath en als dat niet lukt, schrijft tmp file vanuit classpath en leest dat in
     * 
     * @param libname
     * @param ext
     * @param temp
     * 
     * @throws IOException
     * @throws NullPointerException
     * @throws UnsatisfiedLinkError
     * @throws RuntimeException
     */
    public static void dynamicLoadLibrary(String libname, String ext, boolean temp) throws IOException, NullPointerException, UnsatisfiedLinkError {
        if (ext == null) {
            ext = IOnOSUtils.getDefaultLibraryExtention();
        }

        try {
            System.loadLibrary(libname);
        } catch (UnsatisfiedLinkError e) {
            if (temp) {
                IOnOSUtils.dynamic(libname, ext);
            } else {
                switch (IOnOSUtils.osgroup) {
                    case Windows:
                        IOnOSUtils.dynamicLoadWinLibrary(libname, ext, false);

                        break;

                    default:
                        throw new RuntimeException("not implemented for " + IOnOSUtils.osgroup);
                }
            }
        }
    }

    /**
     * dynamicLoadWinLibrary
     * 
     * @param libname
     * @param ext
     * @param onlywrite
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void dynamicLoadWinLibrary(String libname, String ext, boolean onlywrite) throws IOException, FileNotFoundException {
        File dllfile = new File(IOnOSUtils.WINDIR_SYSTEM32, libname + "." + ext);

        if (!dllfile.exists()) {
            IOnOSUtils.copyResource(libname + "." + ext, new FileOutputStream(dllfile));
        }

        if (onlywrite) {
            return;
        }

        System.loadLibrary(libname);
    }

    /**
     * voert command uit
     * 
     * @param command
     * 
     * @throws IOException
     * 
     * @see {@link #process(String, boolean, boolean)} met false false
     */
    public static void execute(String command) throws IOException {
        IOnOSUtils.process(command, false, false);
    }

    /**
     * get default library extention name for current OS
     * 
     * @return
     */
    public static String getDefaultLibraryExtention() {
        switch (IOnOSUtils.osgroup) {
            case Mac:
                return "jnilib";

            case Nix:
                return "so";

            case Windows:
                return "dll";

            default:
                return null;
        }
    }

    /**
     * open bestand met default voor OS, wanneer niet ondersteund en op windows pobeer via file association command; wanneer pdf probeer eerst acrobat
     * 
     * @param file
     * 
     * @throws IOException
     */
    public static void open(File file, String... cmdparameters) throws IOException {
        if (!IOnOSUtils.openAndCheck(file, cmdparameters)) {
            throw new IOException("could not open file " + file);
        }
    }

    /**
     * open bestand met default voor OS, wanneer niet ondersteund en op windows pobeer via file association command; wanneer pdf probeer eerst acrobat
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static boolean openAndCheck(File file, String... cmdparameters) throws IOException {
        if ((file == null) || !file.exists()) {
            throw new FileNotFoundException((file == null) ? "" : file.getAbsolutePath());
        }

        if (IOnOSUtils.osgroup == OS_GROUP.Windows) {
            return IOnOSUtils.openCommand(file, false, cmdparameters);
        }

        return IOnOSUtils.openDesktop(file);
    }

    /**
     * openCommand
     * 
     * @param file
     * @param prefixedparameters
     * 
     * @return
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static boolean openCommand(File file, boolean prefixedparameters, String... cmdparameters) throws IOException {
        // om te vermijden dat we fouten van het programma dat de meegegeven file normaal opent krijgen
        if (!file.exists() || (file.length() == 0)) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        String filename = file.getName();
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        // %0 or %1 are replaced with the file name that you want to open.
        // %* is replaced with all of the parameters.
        // %~ n is replaced with all of the remaining parameters, starting with the nth parameter, where n can be any number from 2 to 9.
        // %2 is replaced with the first parameter, %3 with the second, and so on.
        String extname = IOnOSUtils.WIN_FILE_EXTS.get(ext);

        if (extname == null) {
            throw new IOException("file association not found: " + ext);
        }

        String opencommand = IOnOSUtils.WIN_FILE_OPEN_CMDS.get(extname);

        if (opencommand == null) {
            throw new IOException("file association not found: " + ext + "=" + extname);
        }

        String tmp = opencommand;

        if (!opencommand.contains("%")) {
            // voe toe als prefix
            if (prefixedparameters && (cmdparameters.length > 0)) {
                for (String cmdparameter : cmdparameters) {
                    opencommand += (" " + cmdparameter);
                }
            }

            // voeg filename toe
            opencommand += (" " + file.getAbsoluteFile().getAbsolutePath());

            // voe toe als suffix
            if (!prefixedparameters && (cmdparameters.length > 0)) {
                for (String cmdparameter : cmdparameters) {
                    opencommand += (" " + cmdparameter);
                }
            }
        } else {
            // vervang filename
            if (opencommand.contains("%0") || opencommand.contains("%1")) {
                opencommand = StringUtils.replace(opencommand, "%0", file.getAbsoluteFile().getAbsolutePath());
                opencommand = StringUtils.replace(opencommand, "%1", file.getAbsoluteFile().getAbsolutePath());

                if (opencommand.contains("%")) {
                    for (int i = 0; i < cmdparameters.length; i++) {
                        opencommand = StringUtils.replace(opencommand, "%" + (i + 2), cmdparameters[i]);
                    }
                } else {
                    // voe toe als prefix
                    if (prefixedparameters) {
                        int pos = opencommand.indexOf(file.getAbsoluteFile().getAbsolutePath());
                        pos = opencommand.substring(0, pos).lastIndexOf(' ') + 1;

                        String tmp1 = opencommand.substring(0, pos);
                        String tmp2 = opencommand.substring(pos);

                        opencommand = tmp1;

                        for (String cmdparameter : cmdparameters) {
                            opencommand += (" " + cmdparameter);
                        }

                        opencommand += (" " + tmp2);
                    } else {
                        // voe toe als suffix
                        for (String cmdparameter : cmdparameters) {
                            opencommand += (" " + cmdparameter);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("format not supported: " + tmp);
            }
        }

        IOnOSUtils.logger.info(opencommand);
        IOnOSUtils.execute(opencommand);

        return true;
    }

    private static boolean openDesktop(File file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }

        Desktop.getDesktop().open(file);

        return true;
    }

    /**
     * voer command uit
     * 
     * @param command het commando
     * @param capture wanneer dit aan staat zal de uitvoerende thread blokeren tot wanneer alle uitvoer van het commando voltooid is!!!
     * @param log wanneer capture aan staat, log ook naar System.out
     * 
     * @return wanneer capture aan staat, de uitvoer ervan, lijnen
     * 
     * @throws IOException
     */
    private static List<String> process(String command, boolean capture, boolean log) throws IOException {
        List<String> lines = new ArrayList<String>();
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        Process p = pb.start();

        if (capture) {
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                if (capture) {
                    lines.add(line);
                }

                if (log) {
                    System.out.println(line);
                }
            }

            is.close();
        }

        return lines;
    }

    /**
     * get file content
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public static byte[] read(File file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOnOSUtils.copy(new FileInputStream(file), out);

        return out.toByteArray();
    }

    /**
     * schrijf data naar bestand
     * 
     * @param data
     * @param file
     * 
     * @throws NullPointerException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(byte[] data, File file) throws NullPointerException, FileNotFoundException, IOException {
        IOnOSUtils.copy(new ByteArrayInputStream(data), new FileOutputStream(file));
    }

    /**
     * zip entries from multiple files
     * 
     * @param out
     * 
     * @throws IOException
     */
    public static void zip(OutputStream out, File... files) throws IOException {
        byte[] buffer = new byte[IOnOSUtils.DEFAULT_BUFFER_LEN];
        int read;

        ZipOutputStream zout = new ZipOutputStream(out);
        int count = 0;

        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            zout.putNextEntry(new ZipEntry(file.getName()));

            FileInputStream fin = new FileInputStream(file);

            while ((read = fin.read(buffer)) > 0) {
                zout.write(buffer, 0, read);
            }

            fin.close();
            zout.closeEntry();
            count++;
        }

        zout.close();

        if (count == 0) {
            throw new IOException("nothing to zip");
        }
    }

    /**
     * zip data + name
     * 
     * @param out
     * @param entryname
     * @param data
     * 
     * @throws IOException
     */
    public static void zip(OutputStream out, String entryname, byte[] data) throws IOException {
        IOnOSUtils.zip(out, entryname, new ByteArrayInputStream(data));
    }

    /**
     * zip single entry from inputstream
     * 
     * @param out
     * @param entryname
     * @param in
     * 
     * @throws IOException
     */
    public static void zip(OutputStream out, String entryname, InputStream in) throws IOException {
        byte[] buffer = new byte[IOnOSUtils.DEFAULT_BUFFER_LEN];
        int read;

        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(new ZipEntry(entryname));

        while ((read = in.read(buffer)) > 0) {
            zout.write(buffer, 0, read);
        }

        in.close();
        zout.closeEntry();

        zout.close();
    }
}
