package org.tools.hqlbuilder.webservice.wicket.components.tree;


public interface DataNode<T> extends Node<T>, Comparable<DataNode<T>> {
	Boolean checked();

	String name();

	String value();
}
