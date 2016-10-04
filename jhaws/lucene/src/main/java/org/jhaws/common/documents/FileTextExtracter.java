package org.jhaws.common.documents;

import java.util.Arrays;
import java.util.List;

import org.jhaws.common.io.FilePath;

@FunctionalInterface
public interface FileTextExtracter {
	default List<String> accepts() {
		return Arrays.asList(getClass().getSimpleName().substring(0, 3).toLowerCase());
	}

	default boolean accept(FilePath file) {
		return new org.jhaws.common.io.FilePath.Filters.ExtensionFilter(accepts()).accept(file);
	}

	default String extract(FilePath file) {
		FilePath target = file.appendExtension("txt");
		extract(file, target);
		return target.readAll();
	}

	void extract(FilePath file, FilePath target);
}
