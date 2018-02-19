package org.jhaws.common.svn.xml;

public class SvnRootBean {
    private transient String toString;

    @Override
    public String toString() {
        return toString;
    }

    public void toString(String string) {
        toString = string;
    }
}