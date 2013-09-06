package org.jhaws.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;

public class CommonUtils {
    public static void unzip(InputStream source, IODirectory target) throws IOException {
        ZipInputStream zin = new ZipInputStream(source);
        ZipEntry entry;
        int read;
        byte[] buffer = new byte[8 * 1024];

        while ((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                IOFile file = new IOFile(target, entry.getName());
                file.mkDir();
                OutputStream out = new FileOutputStream(file);
                while ((read = zin.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            } else {
                IODirectory dir = new IODirectory(target, entry.getName());
                dir.create();
            }
        }

        zin.close();
    }
}
