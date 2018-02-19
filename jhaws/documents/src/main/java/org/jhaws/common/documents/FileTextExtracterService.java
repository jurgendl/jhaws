package org.jhaws.common.documents;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Filters.ExtensionFilter;
import org.jhaws.common.lang.CollectionUtils8;

public class FileTextExtracterService {
    private Map<ExtensionFilter, FileTextExtracter> fileTextExtractersMap;

    public FileTextExtracterService() {
        fileTextExtractersMap = CollectionUtils8.getMap(
                CollectionUtils8.stream(ServiceLoader.load(org.jhaws.common.documents.FileTextExtracter.class).iterator()),
                fileTextExtracter -> new ExtensionFilter(fileTextExtracter.accepts()));
    }

    public boolean supports(FilePath file) {
        return fileTextExtractersMap.keySet().stream().filter(ex -> ex.accept(file)).findAny().isPresent();
    }

    public FileTextExtracter getFileTextExtracter(FilePath file) {
        return CollectionUtils8.getMapValue(fileTextExtractersMap, file);
    }

    public String extract(FilePath file, boolean writeFile) throws IOException {
        FileTextExtracter fileTextExtracter = getFileTextExtracter(file);
        if (fileTextExtracter != null) {
            return fileTextExtracter.extract(file, writeFile);
        }
        return null;
    }

    public boolean extract(FilePath file, FilePath target) throws IOException {
        FileTextExtracter fileTextExtracter = getFileTextExtracter(file);
        if (fileTextExtracter != null) {
            fileTextExtracter.extract(file, target);
            return true;
        }
        return false;
    }
}
