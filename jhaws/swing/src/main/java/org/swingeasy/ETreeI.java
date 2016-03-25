package org.swingeasy;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public interface ETreeI<T> extends EComponentI {
    /**
     * @see JTree#expandPath(TreePath)
     */
    public abstract void expandPath(TreePath nextMatch);

    /**
     * JDOC
     * 
     * @param current
     * @param matcher
     * @return
     */
    public abstract TreePath getNextMatch(TreePath current, Matcher<T> matcher);

    public abstract TreePath getSelectedOrTopNodePath();

    public abstract TreePath getTopNodePath();

    /**
     * @see JTree#setSelectionPath(TreePath)
     */
    public abstract void setSelectionPath(TreePath nextMatch);
}
