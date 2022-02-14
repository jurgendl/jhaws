package org.jhaws.common.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jurgen
 */
public class SystemSettings {
    public static enum BIT {
        _32, _64, unknown;
    }

    public static enum OS {
        AIX, //
        Digital_Unix, //
        FreeBSD, //
        HP_UX, //
        Irix, //
        Linux, //
        MPE_iX, //
        Mac_OS, //
        Mac_OS_X, //
        Netware_4_11, //
        OS_2, //
        Solaris, //
        SunOS, //
        Windows_2000, //
        Windows_7, //
        Windows_8, //
        Windows_8_1, //
        Windows_10, //
        Windows_95, //
        Windows_98, //
        Windows_NT, //
        Windows_2003, //
        Windows_Vista, //
        Windows_XP, //
        unknown;//
    }

    public static enum OS_GROUP {
        Mac, Nix, Windows, unknown;
    }

    /** 32/64 bit */
    public static final BIT bit;

    /** see enum */
    public static final OS os;

    /** see enum */
    public static final OS_GROUP osgroup;

    /** nix /usr/lib/ */
    public static final File NIX_LIB;

    /** default open file commands for windows */
    public static final Map<String, String> WIN_FILE_OPEN_CMDS = new HashMap<>();

    /** file extension map for windows */
    public static final Map<String, String> WIN_FILE_EXTS = new HashMap<>();

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

    public static final String LOCALE = "locale";

    public static final String TMPDIR = "tmpdir";

    public static final String NEWLINE = "newline";

    public static final String CLIPBOARD = "clipboard";

    /** singleton */
    protected static final SystemSettings singleton = new SystemSettings();

    protected static String newline;

    protected static File tmpdir;

    /**
     * initialise settings
     */
    static {
        SystemSettings.newline = System.getProperty("line.separator");

        SystemSettings.tmpdir = new File(System.getProperty("java.io.tmpdir"));

        try {
            SystemSettings.tmpdir = new File(System.getProperty("java.io.tmpdir"));
        } catch (Exception ex) {
            try {
                SystemSettings.tmpdir = File.createTempFile("prefix", "suffix").getParentFile();
            } catch (Exception ex2) {
                //
            }
        }

        String WINDIR_PROGRAM_FILES0 = "%ProgramFiles%";
        String WINDIR_SYSTEM_ROOT0 = "%SystemRoot%";
        String WINDIR_WINDIR0 = "%WinDir%";
        String WINDIR_USER_PROFILE0 = "%UserProfile%";
        String WINDIR_SYSTEM320 = "%SystemRoot%/system32";

        BIT _bit = BIT.unknown;

        try {
            _bit = BIT.valueOf("_" + System.getProperty("sun.arch.data.model"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out); // gebeurt normaal niet en als het gebeurt is het niet erg
        }

        bit = _bit;

        OS _os = OS.unknown;

        try {
            _os = OS.valueOf(System.getProperty("os.name").replace('.', '_').replace(' ', '_').replace('/', '_'));
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace(System.out); // gebeurt normaal niet en als het gebeurt is het niet erg
        } catch (Exception ex) {
            ex.printStackTrace(System.out); // gebeurt normaal niet en als het gebeurt is het niet erg
        }

        os = _os;

        OS_GROUP _osgroup = OS_GROUP.unknown;

        try {
            switch (SystemSettings.os) {
                case Digital_Unix:
                case Linux:
                    _osgroup = OS_GROUP.Nix;

                    break;

                case Mac_OS:
                case Mac_OS_X:
                    _osgroup = OS_GROUP.Mac;

                    break;

                case Windows_95:
                case Windows_98:
                case Windows_2000:
                case Windows_NT:
                case Windows_2003:
                case Windows_XP:
                case Windows_Vista:
                case Windows_7:
                case Windows_8:
                case Windows_8_1:
                case Windows_10:
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
                case SunOS:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out); // gebeurt normaal niet en als het gebeurt is het niet erg
        }

        osgroup = _osgroup;

        if (SystemSettings.osgroup == OS_GROUP.Windows) {
            // http://technet.microsoft.com/en-us/library/bb490910.aspx
            Pattern dirProgramFilesPattern = Pattern.compile("ProgramFiles", Pattern.CASE_INSENSITIVE);
            Pattern dirSystemRootPattern = Pattern.compile("SystemRoot", Pattern.CASE_INSENSITIVE);
            Pattern dirWinDirPattern = Pattern.compile("WinDir", Pattern.CASE_INSENSITIVE);
            Pattern dirUserProfilePattern = Pattern.compile("UserProfile", Pattern.CASE_INSENSITIVE);

            try {
                WINDIR_PROGRAM_FILES0 = SystemSettings.capture1("cmd", "/c", "echo", "%ProgramFiles%");
                WINDIR_SYSTEM_ROOT0 = SystemSettings.capture1("cmd", "/c", "echo", "%SystemRoot%");
                WINDIR_WINDIR0 = SystemSettings.capture1("cmd", "/c", "echo", "%WinDir%");
                WINDIR_SYSTEM320 = WINDIR_WINDIR0 + "/system32";
                WINDIR_USER_PROFILE0 = SystemSettings.capture1("cmd", "/c", "echo", "%UserProfile%");
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }

            try {
                for (String record : SystemSettings.capture("cmd", "/c", "ftype")) {
                    String[] parts = record.split("=");

                    if (parts.length > 1) {
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

                        SystemSettings.WIN_FILE_OPEN_CMDS.put(parts[0], cmd);
                    }
                }

                for (String record : SystemSettings.capture("cmd", "/c", "assoc")) {
                    String[] parts = record.split("=");

                    if (parts.length == 2) {
                        SystemSettings.WIN_FILE_EXTS.put(parts[0].substring(1).toLowerCase(), parts[1]);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

        WINDIR_PROGRAM_FILES = WINDIR_PROGRAM_FILES0;
        WINDIR_SYSTEM_ROOT = WINDIR_SYSTEM_ROOT0;
        WINDIR_WINDIR = WINDIR_WINDIR0;
        WINDIR_USER_PROFILE = WINDIR_USER_PROFILE0;
        WINDIR_SYSTEM32 = WINDIR_SYSTEM320;

        File _nix_lib = null;

        if (SystemSettings.osgroup == OS_GROUP.Nix) {
            _nix_lib = new File("/usr/lib/");
        }

        NIX_LIB = _nix_lib;
    }

    /**
     * internal use
     */
    private static List<String> capture(String... command) throws IOException {
        return SystemSettings.execute(command);
    }

    /**
     * internal use
     */
    private static String capture1(String... command) throws IOException {
        return SystemSettings.capture(command).get(0);
    }

    public static long copy(InputStream in, OutputStream out) throws IOException, NullPointerException {
        long total = 0;
        try {
            byte[] buffer = new byte[8 * 1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                total += read;
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
        return total;
    }

    public static void copyResource(String path, OutputStream out) throws IOException, NullPointerException {
        SystemSettings.copy(SystemSettings.class.getClassLoader().getResourceAsStream(path), out);
    }

    public static File createTempFile(String naam, String extensie) throws IOException {
        File tempFile = File.createTempFile(naam, "." + extensie);
        // zodoende worden deze verwijderd bij aflsuiten applicatie door shutdownhook
        // Windows: op voorwaarde dat de bestanden niet in gebruik zijn!
        tempFile.deleteOnExit();

        return tempFile;
    }

    /**
     * internal use
     */
    private static void dynamic(String libname, String ext, String tempSuffix) throws IOException, FileNotFoundException {
        File dllfile;

        if (tempSuffix != null) {
            dllfile = new File(SystemSettings.tmpdir, libname + "_" + tempSuffix + "." + ext); // wordt niet verwijderd bij afsluiten
        } else {
            dllfile = SystemSettings.createTempFile(libname + "_", ext); // wordt verwijderd bij afsluiten
        }

        if (!dllfile.exists() || dllfile.length() == 0) {
            SystemSettings.copyResource(libname + "." + ext, new FileOutputStream(dllfile));
        }

        System.load(dllfile.getAbsolutePath());
    }

    /**
     * probeer library met naam en extentie in te lezen van libpath en als dat niet lukt, schrijft tmp file vanuit classpath en leest dat in
     *
     * @param libname libname
     * @param ext lib extentie, neem null voor default op besturingssysteem
     * @param temp maak een temp file en probeer van daar in te laden
     * @param tempSuffix enkel van belang wanneer temp=true; bv versie library als die niet in libname zelf zit, wanneer null wordt dat niet in de temp filename gestoken; als deze
     *            waarde niet null is wordt een tmp file met een vaste naam gegenereerd zodat deze bij volgende keer kan worden hergebruikt in tegenstelling tot volledig random
     *            naam wanneer deze parameter null is
     * @throws IOException
     * @throws NullPointerException
     * @throws UnsatisfiedLinkError
     * @throws RuntimeException
     */
    public static void dynamicLoadLibrary(String libname, String ext, boolean temp, String tempSuffix) throws IOException, NullPointerException, UnsatisfiedLinkError {
        if (ext == null) {
            ext = SystemSettings.getDefaultLibraryExtention();
        }

        try {
            System.loadLibrary(libname);
        } catch (UnsatisfiedLinkError e) {
            if (temp) {
                SystemSettings.dynamic(libname, ext, tempSuffix);
            } else {
                switch (SystemSettings.osgroup) {
                    case Windows:
                        SystemSettings.dynamicLoadWinLibrary(libname, ext, false);

                        break;

                    default:
                        throw new RuntimeException("not implemented for " + SystemSettings.osgroup);
                }
            }
        }
    }

    /**
     * internal use
     */
    private static void dynamicLoadWinLibrary(String libname, String ext, boolean onlywrite) throws IOException, FileNotFoundException {
        File dllfile = new File(SystemSettings.WINDIR_SYSTEM32, libname + "." + ext);

        if (!dllfile.exists()) {
            SystemSettings.copyResource(libname + "." + ext, new FileOutputStream(dllfile));
        }

        if (onlywrite) {
            return;
        }

        System.loadLibrary(libname);
    }

    public static List<String> execute(String... command) throws IOException {
        List<String> lines = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder(Arrays.asList(command));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (InputStream is = p.getInputStream(); InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            is.close();
            return lines;
        }
    }

    /**
     * get library extension
     */
    public static String getDefaultLibraryExtention() {
        switch (SystemSettings.osgroup) {
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

    public static String getFileExtension(File file) {
        String filename = file.getName();
        int pos = filename.lastIndexOf('.');
        if (pos == -1) {
            return null;
        }
        return filename.substring(pos + 1);
    }

    public static String getNewline() {
        return SystemSettings.newline;
    }

    public static SystemSettings getSingleton() {
        return SystemSettings.singleton;
    }

    public static File getTmpdir() {
        return SystemSettings.tmpdir;
    }

    /**
     * singleton
     */
    protected SystemSettings() {
        super();
    }
}
