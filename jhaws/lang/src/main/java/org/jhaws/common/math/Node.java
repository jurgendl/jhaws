package org.jhaws.common.math;

import java.io.Serializable;

public class Node<T> implements Serializable {
    private static final long serialVersionUID = 5669447844282415452L;

    protected Node<T> next;

    protected T item;

    public Node(T item) {
        this.item = item;
    }

    public Node(Node<T> previous, T item) {
        this(item);
        if (previous != null) previous.next = this;
    }

    public Node(T item, Node<T> next) {
        this(item);
        this.next = next;
    }

    @Override
    public String toString() {
        return String.valueOf(item);
    }

    public Node<T> next() {
        return next;
    }

    public void next(Node<T> next) {
        this.next = next;
    }

    public T item() {
        return item;
    }

    public void item(T item) {
        this.item = item;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
