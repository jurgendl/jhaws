package org.jhaws.common.io.win;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class WinRegistry {
	// inspired by
	// http://javabyexample.wisdomplug.com/java-concepts/34-core-java/62-java-registry-wrapper.html
	// http://www.snipcode.org/java/1-java/23-java-class-for-accessing-reading-and-writing-from-windows-registry.html
	// http://snipplr.com/view/6620/accessing-windows-registry-in-java/
	public static final int HKEY_CURRENT_USER = 0x80000001;

	public static final int HKEY_LOCAL_MACHINE = 0x80000002;

	public static final int REG_SUCCESS = 0;

	public static final int REG_NOTFOUND = 2;

	public static final int REG_ACCESSDENIED = 5;

	private static final int KEY_ALL_ACCESS = 0xf003f;

	private static final int KEY_READ = 0x20019;

	private static Preferences userRoot = Preferences.userRoot();

	private static Preferences systemRoot = Preferences.systemRoot();

	private static Class<? extends Preferences> userClass = WinRegistry.userRoot.getClass();

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
			WinRegistry.regOpenKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] { int.class, byte[].class, int.class });
			WinRegistry.regOpenKey.setAccessible(true);
			WinRegistry.regCloseKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });
			WinRegistry.regCloseKey.setAccessible(true);
			WinRegistry.regQueryValueEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[] { int.class, byte[].class });
			WinRegistry.regQueryValueEx.setAccessible(true);
			WinRegistry.regEnumValue = WinRegistry.userClass.getDeclaredMethod("WindowsRegEnumValue", new Class[] { int.class, int.class, int.class });
			WinRegistry.regEnumValue.setAccessible(true);
			WinRegistry.regQueryInfoKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { int.class });
			WinRegistry.regQueryInfoKey.setAccessible(true);
			WinRegistry.regEnumKeyEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegEnumKeyEx", new Class[] { int.class, int.class, int.class });
			WinRegistry.regEnumKeyEx.setAccessible(true);
			WinRegistry.regCreateKeyEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[] { int.class, byte[].class });
			WinRegistry.regCreateKeyEx.setAccessible(true);
			WinRegistry.regSetValueEx = WinRegistry.userClass.getDeclaredMethod("WindowsRegSetValueEx", new Class[] { int.class, byte[].class, byte[].class });
			WinRegistry.regSetValueEx.setAccessible(true);
			WinRegistry.regDeleteValue = WinRegistry.userClass.getDeclaredMethod("WindowsRegDeleteValue", new Class[] { int.class, byte[].class });
			WinRegistry.regDeleteValue.setAccessible(true);
			WinRegistry.regDeleteKey = WinRegistry.userClass.getDeclaredMethod("WindowsRegDeleteKey", new Class[] { int.class, byte[].class });
			WinRegistry.regDeleteKey.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a key
	 *
	 * @param hkey
	 *            HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
	 * @param key
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void createKey(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int[] ret;
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			ret = WinRegistry.createKey(WinRegistry.systemRoot, hkey, key);
			WinRegistry.regCloseKey.invoke(WinRegistry.systemRoot, new Object[] { new Integer(ret[0]) });
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			ret = WinRegistry.createKey(WinRegistry.userRoot, hkey, key);
			WinRegistry.regCloseKey.invoke(WinRegistry.userRoot, new Object[] { new Integer(ret[0]) });
		} else {
			throw new IllegalArgumentException("hkey=" + hkey);
		}
		if (ret[1] != WinRegistry.REG_SUCCESS) {
			throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
		}
	}

	private static int[] createKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return (int[]) WinRegistry.regCreateKeyEx.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key) });
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
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			rc = WinRegistry.deleteKey(WinRegistry.systemRoot, hkey, key);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			rc = WinRegistry.deleteKey(WinRegistry.userRoot, hkey, key);
		}
		if (rc != WinRegistry.REG_SUCCESS) {
			throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
		}
	}

	private static int deleteKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int rc = ((Integer) WinRegistry.regDeleteKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key) })).intValue();
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
		int rc = -1;
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			rc = WinRegistry.deleteValue(WinRegistry.systemRoot, hkey, key, value);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			rc = WinRegistry.deleteValue(WinRegistry.userRoot, hkey, key, value);
		}
		if (rc != WinRegistry.REG_SUCCESS) {
			throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
		}
	}

	private static int deleteValue(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int[] handles = (int[]) WinRegistry.regOpenKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key), new Integer(WinRegistry.KEY_ALL_ACCESS) });
		if (handles[1] != WinRegistry.REG_SUCCESS) {
			return handles[1]; // can be REG_NOTFOUND, REG_ACCESSDENIED
		}
		int rc = ((Integer) WinRegistry.regDeleteValue.invoke(root, new Object[] { new Integer(handles[0]), WinRegistry.toCstr(value) })).intValue();
		WinRegistry.regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
		return rc;
	}

	/**
	 * Read a value from key and value name
	 *
	 * @param hkey
	 *            HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
	 * @param key
	 * @param valueName
	 * @return the value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String readString(int hkey, String key, String valueName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			return WinRegistry.readString(WinRegistry.systemRoot, hkey, key, valueName);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			return WinRegistry.readString(WinRegistry.userRoot, hkey, key, valueName);
		} else {
			throw new IllegalArgumentException("hkey=" + hkey);
		}
	}

	private static String readString(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int[] handles = (int[]) WinRegistry.regOpenKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key), new Integer(WinRegistry.KEY_READ) });
		if (handles[1] != WinRegistry.REG_SUCCESS) {
			return null;
		}
		byte[] valb = (byte[]) WinRegistry.regQueryValueEx.invoke(root, new Object[] { new Integer(handles[0]), WinRegistry.toCstr(value) });
		WinRegistry.regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
		return valb != null ? new String(valb).trim() : null;
	}

	// =====================

	/**
	 * Read the value name(s) from a given key
	 *
	 * @param hkey
	 *            HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
	 * @param key
	 * @return the value name(s)
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static List<String> readStringSubKeys(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			return WinRegistry.readStringSubKeys(WinRegistry.systemRoot, hkey, key);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			return WinRegistry.readStringSubKeys(WinRegistry.userRoot, hkey, key);
		} else {
			throw new IllegalArgumentException("hkey=" + hkey);
		}
	}

	private static List<String> readStringSubKeys(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<String> results = new ArrayList<>();
		int[] handles = (int[]) WinRegistry.regOpenKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key), new Integer(WinRegistry.KEY_READ) });
		if (handles[1] != WinRegistry.REG_SUCCESS) {
			return null;
		}
		int[] info = (int[]) WinRegistry.regQueryInfoKey.invoke(root, new Object[] { new Integer(handles[0]) });

		int count = info[2]; // count
		int maxlen = info[3]; // value length max
		for (int index = 0; index < count; index++) {
			byte[] name = (byte[]) WinRegistry.regEnumKeyEx.invoke(root, new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
			results.add(new String(name).trim());
		}
		WinRegistry.regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
		return results;
	}

	/**
	 * Read value(s) and value name(s) form given key
	 *
	 * @param hkey
	 *            HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
	 * @param key
	 * @return the value name(s) plus the value(s)
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Map<String, String> readStringValues(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			return WinRegistry.readStringValues(WinRegistry.systemRoot, hkey, key);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			return WinRegistry.readStringValues(WinRegistry.userRoot, hkey, key);
		} else {
			throw new IllegalArgumentException("hkey=" + hkey);
		}
	}

	private static Map<String, String> readStringValues(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, String> results = new LinkedHashMap<>();
		int[] handles = (int[]) WinRegistry.regOpenKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key), new Integer(WinRegistry.KEY_READ) });
		if (handles[1] != WinRegistry.REG_SUCCESS) {
			return null;
		}
		int[] info = (int[]) WinRegistry.regQueryInfoKey.invoke(root, new Object[] { new Integer(handles[0]) });

		int count = info[2]; // count
		int maxlen = info[3]; // value length max
		for (int index = 0; index < count; index++) {
			byte[] name = (byte[]) WinRegistry.regEnumValue.invoke(root, new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
			String value = WinRegistry.readString(hkey, key, new String(name));
			results.put(new String(name).trim(), value);
		}
		WinRegistry.regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
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
		if (hkey == WinRegistry.HKEY_LOCAL_MACHINE) {
			WinRegistry.writeStringValue(WinRegistry.systemRoot, hkey, key, valueName, value);
		} else if (hkey == WinRegistry.HKEY_CURRENT_USER) {
			WinRegistry.writeStringValue(WinRegistry.userRoot, hkey, key, valueName, value);
		} else {
			throw new IllegalArgumentException("hkey=" + hkey);
		}
	}

	private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int[] handles = (int[]) WinRegistry.regOpenKey.invoke(root, new Object[] { new Integer(hkey), WinRegistry.toCstr(key), new Integer(WinRegistry.KEY_ALL_ACCESS) });

		WinRegistry.regSetValueEx.invoke(root, new Object[] { new Integer(handles[0]), WinRegistry.toCstr(valueName), WinRegistry.toCstr(value) });
		WinRegistry.regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
	}

	private WinRegistry() {}
}
