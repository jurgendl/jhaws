package org.jhaws.common.docimport;

import java.io.File;

import org.jhaws.common.ModuleLoader;

/**
 * na
 * 
 * @author Jurgen
 */
public class DocumentFactory {
    /** field */
    private static ModuleLoader<ImportDocument> loader = ModuleLoader.getModuleLoader(ImportDocument.class);

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public static ImportDocument getConvertor(File file) {
        return DocumentFactory.loader.getImplementation(DocumentFactory.getKey(file));
    }

    /**
     * na
     * 
     * @param s na
     * 
     * @return
     */
    public static ImportDocument getConvertor(String s) {
        return DocumentFactory.loader.getImplementation(DocumentFactory.getKey(s));
    }

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    private static String getKey(File file) {
        return DocumentFactory.getKey(file.getName());
    }

    /**
     * na
     * 
     * @param s na
     * 
     * @return
     */
    private static String getKey(String s) {
        return s.substring(s.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * na
     * 
     * @return
     */
    public static String[] getSupportedExtensions() {
        return DocumentFactory.loader.getKeys();
    }

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public static boolean supports(File file) {
        return DocumentFactory.loader.supports(DocumentFactory.getKey(file));
    }

    /**
     * na
     * 
     * @param s na
     * 
     * @return
     */
    public static boolean supports(String s) {
        return DocumentFactory.loader.supports(DocumentFactory.getKey(s));
    }

    /**
     * Creates a new DocumentFactory object.
     */
    protected DocumentFactory() {
        super();
    }
}
