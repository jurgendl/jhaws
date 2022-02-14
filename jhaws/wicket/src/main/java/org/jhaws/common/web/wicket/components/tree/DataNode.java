package org.jhaws.common.web.wicket.components.tree;

public interface DataNode<T> extends Node<T>, Comparable<DataNode<T>> {
    Boolean checked();

    String name();

    String value();
}
