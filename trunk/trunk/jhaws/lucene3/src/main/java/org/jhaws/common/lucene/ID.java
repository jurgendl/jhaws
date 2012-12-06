package org.jhaws.common.lucene;

/**
 * 
 * different stored fields
 *
 * @author Jurgen De Landsheer
 * @since 1.5
 * @version 1.0.0 - 22 June 2006
 */
public enum ID {
/** textfield */
    text, 
/** descriptionfield */
    description, 
/** keywordsfield */
    keywords, 
/** languagefield */
    language, 
/** urlfield */
    url, 
/** sizefield */
    size, 
/** analyzerfield */
    analyzer, 
/** lastmodfield */
    lastmod, 
/** type file */
    filetype;
    /**
     * gets fieldname
     *
     * @return fieldname
     */
    public String f() {
        return toString();
    }
}
