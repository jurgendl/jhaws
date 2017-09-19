package org.jhaws.common.math.graph;

import java.io.Serializable;

public class Edge<T, N extends Number> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Node<T> from;

    protected Node<T> to;

    protected N weight;

    public Edge(Node<T> from, Node<T> to) {
        super();
        this.from = from;
        this.to = to;
    }

    public Node<T> getFrom() {
        return this.from;
    }

    public void setFrom(Node<T> from) {
        this.from = from;
    }

    public Node<T> getTo() {
        return this.to;
    }

    public void setTo(Node<T> to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "{" + from.toString() + "," + to.toString() + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.from == null) ? 0 : this.from.hashCode());
        result = prime * result + ((this.to == null) ? 0 : this.to.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Edge<?, ?> other = (Edge<?, ?>) obj;
        if (this.from == null) {
            if (other.from != null) return false;
        } else if (!this.from.equals(other.from)) return false;
        if (this.to == null) {
            if (other.to != null) return false;
        } else if (!this.to.equals(other.to)) return false;
        return true;
    }

    public N getWeight() {
        return this.weight;
    }

    public void setWeight(N weight) {
        this.weight = weight;
    }

    public Edge<T, N> weight(N weight) {
        this.weight = weight;
        return this;
    }
}
