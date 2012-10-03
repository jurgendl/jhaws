package org.jhaws.common.io;

import java.io.File;

/**
 * directory commpare listener
 * 
 * @author Jurgen De landsheer
 * @version 1.0.0 - 27 June 2005
 */
public interface IODirectoryCompareListener {
    /**
     * the two (f1 & f2) are files but are different
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void differs(final File f2, final File f1);

    /**
     * the original (f2) is a directory and the other (f1) does not exists
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void dirDoesNotExists(final File f2, final File f1);

    /**
     * the original (f2) is a file and the other (f1) does not exists
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void doesNotExists(final File f2, final File f1);

    /**
     * there is no original (f1) for an existing other (f2), being it a file or a directory
     * 
     * @param f2 : File : other file
     */
    public void extra(final File f2);

    /**
     * the two (f1 & f2) are files and are the same
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void same(final File f2, final File f1);

    /**
     * the original (f1) is a directory, the other (f2) exists but is not a directory
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void shouldBeADirectory(final File f2, final File f1);

    /**
     * the original (f1) is a file, the other (f2) exists but is not a file
     * 
     * @param f2 : File : other file
     * @param f1 : File : original file
     */
    public void shouldBeAFile(final File f2, final File f1);
}
