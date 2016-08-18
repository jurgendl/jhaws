package org.tools.hqlbuilder.webservice.wicket.components.tree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public interface Node<T> extends Iterable<Node<T>>, Serializable {
    List<Node<T>> children();

    T get();

    /* default */Node<T> getChild(int i);/*
                                          * { return this.children().get(i); }
                                          */

    /* default */int getChildCount(); /*
                                       * { return this.children().size(); }
                                       */

    @Override
    /* default */Iterator<Node<T>> iterator();/*
                                               * { return this.children().iterator(); }
                                               */
}