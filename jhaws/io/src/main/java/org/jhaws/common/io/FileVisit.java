package org.jhaws.common.io;

import java.nio.file.attribute.BasicFileAttributes;

public interface FileVisit {
    void visit(FilePath file, BasicFileAttributes attrs);
}
