package org.jhaws.common.lucene;

import util.module.ModuleLoader;

import java.io.File;


/**
 * na
 *
 * @author Jurgen De Landsheer
 */
public class DocumentFactory {
    /** field */
    private static ModuleLoader<ToLuceneDocument> loader = ModuleLoader.getModuleLoader(ToLuceneDocument.class);

/**
     * Creates a new DocumentFactory object.
     */
    protected DocumentFactory() {
        super();
    }

    /**
     * na
     *
     * @param s na
     *
     * @return
     */
    public static boolean supports(String s) {
        return loader.supports(getKey(s));
    }

    /**
     * na
     *
     * @param file na
     *
     * @return
     */
    private static String getKey(File file) {
        return getKey(file.getName());
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
     * @param file na
     *
     * @return
     */
    public static boolean supports(File file) {
        return loader.supports(getKey(file));
    }

    /**
     * na
     *
     * @param file na
     *
     * @return
     */
    public static ToLuceneDocument getConvertor(File file) {
        return loader.getImplementation(getKey(file));
    }

    /**
     * na
     *
     * @param s na
     *
     * @return
     */
    public static ToLuceneDocument getConvertor(String s) {
        return loader.getImplementation(getKey(s));
    }

    /**
     * na
     *
     * @return
     */
    public static String[] getSupportedExtensions() {
        return loader.getKeys();
    }
}
