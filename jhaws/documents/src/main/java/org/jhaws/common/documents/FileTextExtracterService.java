package org.jhaws.common.documents;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Filters.ExtensionFilter;
import org.jhaws.common.lang.CollectionUtils8;

public class FileTextExtracterService {
    private Map<ExtensionFilter, List<FileTextExtracter>> fileTextExtractersMap;

    public FileTextExtracterService() {
        fileTextExtractersMap = CollectionUtils8.stream(ServiceLoader.load(org.jhaws.common.documents.FileTextExtracter.class).iterator()).collect(Collectors.groupingBy(//
                fileTextExtracter -> new ExtensionFilter(fileTextExtracter.accepts())//
                , LinkedHashMap::new//
                , Collectors.toList()//
        ));
    }

    public boolean supports(FilePath file) {
        return fileTextExtractersMap.keySet().stream().filter(ex -> ex.accept(file)).findAny().isPresent();
    }

    public FileTextExtracter getFileTextExtracter(FilePath file) {
        return getFileTextExtracters(file).get(0);
    }

    public List<FileTextExtracter> getFileTextExtracters(FilePath file) {
        return fileTextExtractersMap.entrySet().stream().filter(ex -> ex.getKey().accept(file)).map(ex -> ex.getValue()).map(ex -> ex.stream()).flatMap(Function.identity()).collect(Collectors.toList());
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
