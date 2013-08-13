package org.jhaws.common.io;

import java.io.File;

public class RecyclerFactory {
    private static IODirectory oldTrashCan;

    private static boolean deleteDirectory(java.io.File directory) {
        if (directory.exists()) {
            java.io.File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        RecyclerFactory.deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    public static IODirectory getDefaultRecycleDirectory() {
        return RecyclerFactory.oldTrashCan;
    }

    public static boolean recycle(IODirectory dir) {
        return RecyclerFactory.deleteDirectory(dir);
    }

    public static boolean recycle(IOFile child) {
        return child.delete();
    }

    public static void setDefaultRecycleDirectory(IODirectory oldTrashCan) {
        RecyclerFactory.oldTrashCan = oldTrashCan;
    }
}
