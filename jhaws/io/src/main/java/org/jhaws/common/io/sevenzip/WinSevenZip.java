package org.jhaws.common.io.sevenzip;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.win.WinRegistry;

public class WinSevenZip extends SevenZip {
	public static FilePath getWinExecutable(boolean compatibility) {
		// HKEY_CURRENT_USER\Software\7-Zip
		String path;
		try {
			if (compatibility)
				throw new IllegalArgumentException("compatibility");
			path = getWin64SevenZipPath().toString();
		} catch (Exception ex1) {
			try {
				path = getWin32SevenZipPath().toString();
			} catch (Exception ex2) {
				return null;
			}
		}
		// 7za.exe
		// 7z.exe
		FilePath dir = new FilePath(path);
		FilePath executableStandalone = dir.child("7za.exe");
		if (executableStandalone.exists()) {
			return executableStandalone;
		}
		FilePath executable = dir.child("7z.exe");
		if (executable.exists()) {
			return executable;
		}
		return null;
	}

	public static String getWin32SevenZipPath() {
		try {
			return WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\7-Zip", "Path").toString();
		} catch (Exception ex1) {
			try {
				return WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "Software\\7-Zip", "Path").toString();
			} catch (Exception ex2) {
				return null;
			}
		}
	}

	public static String getWin64SevenZipPath() {
		try {
			return WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Software\\7-Zip", "Path64").toString();
		} catch (Exception ex1) {
			try {
				return WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "Software\\7-Zip", "Path64").toString();
			} catch (Exception ex2) {
				return null;
			}
		}
	}

	public WinSevenZip(boolean compatibility) {
		super(getWinExecutable(compatibility));
	}

	public WinSevenZip() {
		this(true);
	}
}
