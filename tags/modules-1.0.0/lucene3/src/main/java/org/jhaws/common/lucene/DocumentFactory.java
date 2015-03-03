package org.jhaws.common.lucene;

import java.io.File;

/**
 * na
 * 
 * @author Jurgen
 */
public class DocumentFactory {
    /** field */
    private static ModuleLoader<ToLuceneDocument> loader = ModuleLoader.getModuleLoader(ToLuceneDocument.class);

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public static ToLuceneDocument getConvertor(File file) {
        return loader.getImplementation(DocumentFactory.getKey(file));
    }

    /**
     * na
     * 
     * @param s na
     * 
     * @return
     */
    public static ToLuceneDocument getConvertor(String s) {
        return loader.getImplementation(DocumentFactory.getKey(s));
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
        return loader.getKeys();
    }

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public static boolean supports(File file) {
        return loader.supports(DocumentFactory.getKey(file));
    }

    /**
     * na
     * 
     * @param s na
     * 
     * @return
     */
    public static boolean supports(String s) {
        return loader.supports(DocumentFactory.getKey(s));
    }

    /**
     * Creates a new DocumentFactory object.
     */
    protected DocumentFactory() {
        super();
    }
}