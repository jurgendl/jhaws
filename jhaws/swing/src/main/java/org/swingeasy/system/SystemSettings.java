package org.swingeasy.system;

import java.awt.Desktop;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jhaws.common.g11n.G11n;
import org.swingeasy.EComponentI;
import org.swingeasy.PropertyChangeParent;

/**
 * @author Jurgen
 */
public class SystemSettings extends org.jhaws.common.system.SystemSettings {
    private static Clipboard clipboard;

    public static final PropertyChangeParent propertyChangeParent = new PropertyChangeParent() {
        //
    };

    /**
     * initialise settings
     */
    static {
        SystemSettings.clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public static Clipboard getClipboard() {
        return SystemSettings.clipboard;
    }

    /**
     * open File, can use command parameters (tested on Windows 7)
     */
    public static boolean open(File file, String... cmdparameters) throws IOException {
        String absolutePath = file.getAbsolutePath();
        if (!file.exists() || file.length() == 0) {
            throw new FileNotFoundException(absolutePath);
        }

        if (SystemSettings.osgroup != OS_GROUP.Windows) {
            if (!Desktop.isDesktopSupported()) {
                return false;
            }
            Desktop.getDesktop().open(file);
            return true;
        }

        String ext = SystemSettings.getFileExtension(file).toLowerCase();

        String extname = SystemSettings.WIN_FILE_EXTS.get(ext);
        if (extname == null) {
            throw new IOException("file association not found: " + ext);
        }

        String sysexec = SystemSettings.WIN_FILE_OPEN_CMDS.get(extname);
        if (sysexec == null) {
            throw new IOException("file association not found: " + ext + "=" + extname);
        }

        List<String> opencommand = new ArrayList<>();
        boolean fileAdded = false;
        for (String sysexecpart : SystemSettings.split(sysexec)) {
            // %0 or %1 are replaced with the file name that you want to open.
            // %* is replaced with all of the parameters.
            // %~ n is replaced with all of the remaining parameters, starting with the nth parameter, where n can be any number from 2 to 9.
            // %2 is replaced with the first parameter, %3 with the second, and so on.
            if ("%*".equals(sysexecpart)) {
                for (String cmdparameter : cmdparameters) {
                    opencommand.add(cmdparameter);
                }
                continue;
            }
            fileAdded |= sysexecpart.contains("%0") || sysexecpart.contains("%1");
            sysexecpart = sysexecpart.replace("%0", absolutePath);
            sysexecpart = sysexecpart.replace("%1", absolutePath);
            for (int i = 0; i < cmdparameters.length; i++) {
                sysexecpart = sysexecpart.replace("%" + (i + 2), cmdparameters[i]);
            }
            opencommand.add(sysexecpart);
        }

        if (!fileAdded) {
            if (absolutePath.contains(" ")) {
                opencommand.add("\"" + absolutePath + "\"");
            } else {
                opencommand.add(absolutePath);
            }
        }

        SystemSettings.execute(opencommand.toArray(new String[opencommand.size()]));

        return true;
    }

    public static void setClipboard(Clipboard clipboard) {
        Clipboard old = SystemSettings.clipboard;
        SystemSettings.clipboard = clipboard;
        propertyChangeParent.firePropertyChange(SystemSettings.CLIPBOARD, old, clipboard);
    }

    public static void setNewline(String newline) {
        String old = org.jhaws.common.system.SystemSettings.newline;
        org.jhaws.common.system.SystemSettings.newline = newline;
        propertyChangeParent.firePropertyChange(SystemSettings.NEWLINE, old, newline);
    }

    public static void setTmpdir(File tmpdir) {
        File old = SystemSettings.tmpdir;
        org.jhaws.common.system.SystemSettings.tmpdir = tmpdir;
        propertyChangeParent.firePropertyChange(SystemSettings.TMPDIR, old, tmpdir);
    }

    /**
     * {@link Locale#getDefault()}
     */
    public static Locale getCurrentLocale() {
        return G11n.getCurrentLocale();
    }

    /**
     * can also be set via command line parameter: "-Duser.country=UK -Duser.language=en"; use this instead of {@link Locale#setDefault(Locale)} to change Locale of all
     * {@link EComponentI}s dynamically
     */
    public static void setCurrentLocale(Locale currentLocale) {
        Locale old = G11n.getCurrentLocale();
        G11n.setCurrentLocale(currentLocale);
        propertyChangeParent.firePropertyChange(SystemSettings.LOCALE, old, currentLocale);
    }

    /**
     * internal use
     */
    private static List<String> split(String sysexec) {
        List<String> parts = new ArrayList<>();
        Matcher m = Pattern.compile("\"[^\"]++\"").matcher(sysexec);
        int pos = 0;
        boolean found = false;
        while (m.find()) {
            found = true;
            if (pos != m.start()) {
                String trimmed = sysexec.substring(pos, m.start()).trim();
                if (trimmed.length() > 0) {
                    for (String p : trimmed.split(" ")) {
                        parts.add(p);
                    }
                }
            }
            parts.add(m.group());
            pos = m.end();
        }
        if (found) {
            if (pos != sysexec.length() - 1) {
                String trimmed = sysexec.substring(pos).trim();
                if (trimmed.length() > 0) {
                    for (String p : trimmed.split(" ")) {
                        parts.add(p);
                    }
                }
            }
        } else {
            for (String p : sysexec.split(" ")) {
                parts.add(p);
            }
        }
        return parts;
    }

    /**
     * singleton
     */
    private SystemSettings() {
        super();
    }
}
