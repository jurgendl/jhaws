package org.jhaws.common.io;

import javax.swing.Icon;

/**
 * na
 * 
 * @author Jurgen
 */
public interface ExtensionIconFinder {
    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public abstract Icon getLargeIcon(IOFile file);

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     */
    public abstract Icon getSmallIcon(IOFile file);
}