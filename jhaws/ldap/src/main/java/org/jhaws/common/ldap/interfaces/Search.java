package org.jhaws.common.ldap.interfaces;

import javax.naming.directory.SearchControls;

/**
 * na
 *
 * @author Jurgen
 */
public enum Search {
    SINGLE_LEVEL, DEEP;

    /**
     * na
     *
     * @return
     * @throws IllegalArgumentException na
     */
    public int value() {
        switch (this) {
            case SINGLE_LEVEL:
                return SearchControls.ONELEVEL_SCOPE;

            case DEEP:
                return SearchControls.SUBTREE_SCOPE;
        }

        throw new IllegalArgumentException();
    }
}
