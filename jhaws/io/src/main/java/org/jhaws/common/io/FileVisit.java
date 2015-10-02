package org.jhaws.common.io;

@FunctionalInterface
public interface FileVisit {
	boolean visit(FilePath file);
}
