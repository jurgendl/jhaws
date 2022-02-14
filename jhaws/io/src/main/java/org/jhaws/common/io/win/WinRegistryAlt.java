package org.jhaws.common.io.win;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

// inspired by
// http://javabyexample.wisdomplug.com/java-concepts/34-core-java/62-java-registry-wrapper.html
// http://www.snipcode.org/java/1-java/23-java-class-for-accessing-reading-and-writing-from-windows-registry.html
// http://snipplr.com/view/6620/accessing-windows-registry-in-java/
// http://apidock.com/ruby/Win32/Registry/Constants
public class WinRegistryAlt {
    public static final int HKEY_CURRENT_USER = 0x80000001;

    public static final int HKEY_LOCAL_MACHINE = 0x80000002;

    // public static final int HKEY_CLASSES_ROOT = 0x80000000;

    public static final long REG_SUCCESS = 0;

    public static final long REG_NOTFOUND = 2;

    public static final long REG_ACCESSDENIED = 5;

    private static final int KEY_ALL_ACCESS = 0xf003f;

    private static final int KEY_READ = 0x20019;

    private static Preferences userRoot = Preferences.userRoot();

    private static Preferences systemRoot = Preferences.systemRoot();

    private static Class<? extends Preferences> userClass = WinRegistryAlt.userRoot.getClass();

    private static Method regOpenKey = null;

    private static Method regCloseKey = null;

    private static Method regQueryValueEx = null;

    private static Method regEnumValue = null;

    private static Method regQueryInfoKey = null;

    private static Method regEnumKeyEx = null;

    private static Method regCreateKeyEx = null;

    private static Method regSetValueEx = null;

    private static Method regDeleteKey = null;

    private static Method regDeleteValue = null;

    static {
        try {
            WinRegistryAlt.regOpenKey = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { long.class, byte[].class, int.class });
            WinRegistryAlt.regOpenKey.setAccessible(true);
            WinRegistryAlt.regCloseKey = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { long.class });
            WinRegistryAlt.regCloseKey.setAccessible(true);
            WinRegistryAlt.regQueryValueEx = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[] { long.class, byte[].class });
            WinRegistryAlt.regQueryValueEx.setAccessible(true);
            WinRegistryAlt.regEnumValue = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegEnumValue", new Class[] { long.class, int.class, int.class });
            WinRegistryAlt.regEnumValue.setAccessible(true);
            WinRegistryAlt.regQueryInfoKey = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { long.class });
            WinRegistryAlt.regQueryInfoKey.setAccessible(true);
            WinRegistryAlt.regEnumKeyEx = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegEnumKeyEx", new Class[] { long.class, int.class, int.class });
            WinRegistryAlt.regEnumKeyEx.setAccessible(true);
            WinRegistryAlt.regCreateKeyEx = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[] { long.class, byte[].class });
            WinRegistryAlt.regCreateKeyEx.setAccessible(true);
            WinRegistryAlt.regSetValueEx = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegSetValueEx", new Class[] { long.class, byte[].class, byte[].class });
            WinRegistryAlt.regSetValueEx.setAccessible(true);
            WinRegistryAlt.regDeleteValue = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegDeleteValue", new Class[] { long.class, byte[].class });
            WinRegistryAlt.regDeleteValue.setAccessible(true);
            WinRegistryAlt.regDeleteKey = WinRegistryAlt.userClass.getDeclaredMethod("WindowsRegDeleteKey", new Class[] { long.class, byte[].class });
            WinRegistryAlt.regDeleteKey.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void createKey(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] ret;
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            ret = WinRegistryAlt.createKey(WinRegistryAlt.systemRoot, hkey, key);
            WinRegistryAlt.regCloseKey.invoke(WinRegistryAlt.systemRoot, new Object[] { Long.valueOf(ret[0]) });
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            ret = WinRegistryAlt.createKey(WinRegistryAlt.userRoot, hkey, key);
            WinRegistryAlt.regCloseKey.invoke(WinRegistryAlt.userRoot, new Object[] { Long.valueOf(ret[0]) });
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
        if (ret[1] != WinRegistryAlt.REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    private static int[] createKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (int[]) WinRegistryAlt.regCreateKeyEx.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key) });
    }

    /**
     * Delete a given key
     *
     * @param hkey
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteKey(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int rc = -1;
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            rc = WinRegistryAlt.deleteKey(WinRegistryAlt.systemRoot, hkey, key);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            rc = WinRegistryAlt.deleteKey(WinRegistryAlt.userRoot, hkey, key);
        }
        if (rc != WinRegistryAlt.REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
        }
    }

    private static int deleteKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int rc = ((Integer) WinRegistryAlt.regDeleteKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key) })).intValue();
        return rc; // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    }

    /**
     * delete a value from a given key/value name
     *
     * @param hkey
     * @param key
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteValue(int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        long rc = -1;
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            rc = WinRegistryAlt.deleteValue(WinRegistryAlt.systemRoot, hkey, key, value);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            rc = WinRegistryAlt.deleteValue(WinRegistryAlt.userRoot, hkey, key, value);
        }
        if (rc != WinRegistryAlt.REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
        }
    }

    private static long deleteValue(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        long[] handles = (long[]) WinRegistryAlt.regOpenKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key), Integer.valueOf(WinRegistryAlt.KEY_ALL_ACCESS) });
        if (handles[1] != WinRegistryAlt.REG_SUCCESS) {
            return handles[1]; // can be REG_NOTFOUND, REG_ACCESSDENIED
        }
        int rc = ((Integer) WinRegistryAlt.regDeleteValue.invoke(root, new Object[] { Long.valueOf(handles[0]), WinRegistryAlt.toCstr(value) })).intValue();
        WinRegistryAlt.regCloseKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });
        return rc;
    }

    /**
     * Read a value from key and value name
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param valueName
     * @return the value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String readString(int hkey, String key, String valueName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            return WinRegistryAlt.readString(WinRegistryAlt.systemRoot, hkey, key, valueName);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            return WinRegistryAlt.readString(WinRegistryAlt.userRoot, hkey, key, valueName);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    private static String readString(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        long[] handles = (long[]) WinRegistryAlt.regOpenKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key), Integer.valueOf(WinRegistryAlt.KEY_READ) });
        if (handles[1] != WinRegistryAlt.REG_SUCCESS) {
            return null;
        }
        byte[] valb = (byte[]) WinRegistryAlt.regQueryValueEx.invoke(root, new Object[] { Long.valueOf(handles[0]), WinRegistryAlt.toCstr(value) });
        WinRegistryAlt.regCloseKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });
        return valb != null ? new String(valb).trim() : null;
    }

    // =====================

    /**
     * Read the value name(s) from a given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static List<String> readStringSubKeys(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            return WinRegistryAlt.readStringSubKeys(WinRegistryAlt.systemRoot, hkey, key);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            return WinRegistryAlt.readStringSubKeys(WinRegistryAlt.userRoot, hkey, key);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    private static List<String> readStringSubKeys(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        List<String> results = new ArrayList<>();
        long[] handles = (long[]) WinRegistryAlt.regOpenKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key), Integer.valueOf(WinRegistryAlt.KEY_READ) });
        if (handles[1] != WinRegistryAlt.REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) WinRegistryAlt.regQueryInfoKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });

        int count = info[2]; // count
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) WinRegistryAlt.regEnumKeyEx.invoke(root, new Object[] { Long.valueOf(handles[0]), Integer.valueOf(index), Integer.valueOf(maxlen + 1) });
            results.add(new String(name).trim());
        }
        WinRegistryAlt.regCloseKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });
        return results;
    }

    /**
     * Read value(s) and value name(s) form given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @return the value name(s) plus the value(s)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, String> readStringValues(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            return WinRegistryAlt.readStringValues(WinRegistryAlt.systemRoot, hkey, key);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            return WinRegistryAlt.readStringValues(WinRegistryAlt.userRoot, hkey, key);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    private static Map<String, String> readStringValues(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, String> results = new LinkedHashMap<>();
        long[] handles = (long[]) WinRegistryAlt.regOpenKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key), Integer.valueOf(WinRegistryAlt.KEY_READ) });
        if (handles[1] != WinRegistryAlt.REG_SUCCESS) {
            return null;
        }
        int[] info = (int[]) WinRegistryAlt.regQueryInfoKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });

        int count = info[2]; // count
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] name = (byte[]) WinRegistryAlt.regEnumValue.invoke(root, new Object[] { Long.valueOf(handles[0]), Integer.valueOf(index), Integer.valueOf(maxlen + 1) });
            String value = WinRegistryAlt.readString(hkey, key, new String(name));
            results.put(new String(name).trim(), value);
        }
        WinRegistryAlt.regCloseKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });
        return results;
    }

    // utility
    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

    /**
     * Write a value in a given key/value name
     *
     * @param hkey
     * @param key
     * @param valueName
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void writeStringValue(int hkey, String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == WinRegistryAlt.HKEY_LOCAL_MACHINE) {
            WinRegistryAlt.writeStringValue(WinRegistryAlt.systemRoot, hkey, key, valueName, value);
        } else if (hkey == WinRegistryAlt.HKEY_CURRENT_USER) {
            WinRegistryAlt.writeStringValue(WinRegistryAlt.userRoot, hkey, key, valueName, value);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        long[] handles = (long[]) WinRegistryAlt.regOpenKey.invoke(root, new Object[] { Long.valueOf(hkey), WinRegistryAlt.toCstr(key), Integer.valueOf(WinRegistryAlt.KEY_ALL_ACCESS) });
        WinRegistryAlt.regSetValueEx.invoke(root, new Object[] { Long.valueOf(handles[0]), WinRegistryAlt.toCstr(valueName), WinRegistryAlt.toCstr(value) });
        WinRegistryAlt.regCloseKey.invoke(root, new Object[] { Long.valueOf(handles[0]) });
    }

    private WinRegistryAlt() {
        super();
    }

    public static List<String> getRegValue(String path, String key, String type, boolean iterate) {
        try {
            String command = "reg query \"" + path + "\"" + (iterate ? " /s" : "") + (key == null ? " /ve" : " /v " + key);
            Process process = Runtime.getRuntime().exec(command);
            final InputStream is = process.getInputStream();
            final StringWriter sw = new StringWriter();
            Thread reader = new Thread(() -> {
                try {
                    int c;
                    while ((c = is.read()) != -1) {
                        sw.write(c);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            reader.setDaemon(true);
            reader.start();
            process.waitFor();
            reader.join();

            String result = sw.toString();
            StringTokenizer st = new StringTokenizer(result, "\r\n");
            List<String> results = new ArrayList<>();
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                if (key == null) {
                    if (token.startsWith("(Default)")) {
                        results.add(token.substring(token.indexOf(type) + type.length()).trim());
                    }
                } else {
                    if (token.startsWith(key)) {
                        results.add(token.substring(token.indexOf(type) + type.length()).trim());
                    }
                }
            }
            return results;
        } catch (Exception ex) {
            return null;
        }
    }
}
