package org.jhaws.common.io;

@SuppressWarnings("deprecation")
@Deprecated
public class RecyclerFactory {
	private static IODirectory oldTrashCan;

	private static boolean deleteDirectory(IODirectory directory) {
		if (directory.exists()) {
			for (IOGeneralFile<?> file : directory.listIOGeneralFile()) {
				if (file.isDirectory()) {
					if (!RecyclerFactory.deleteDirectory((IODirectory) file)) {
						return false;
					}
				} else {
					if (!((IOFile) file).delete()) {
						return false;
					}
				}
			}
			return directory.delete0();
		}
		return true;
	}

	public static IODirectory getDefaultRecycleDirectory() {
		return RecyclerFactory.oldTrashCan;
	}

	public static boolean recycle(IODirectory dir) {
		return RecyclerFactory.deleteDirectory(dir);
	}

	public static boolean recycle(IOFile child) {
		return child.erase();
	}

	public static void setDefaultRecycleDirectory(IODirectory oldTrashCan) {
		RecyclerFactory.oldTrashCan = oldTrashCan;
	}
}
