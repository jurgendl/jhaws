package org.jhaws.common.math;

import java.util.ArrayList;
import java.util.List;

// https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)
// http://www.geeksforgeeks.org/?p=18212
// http://algs4.cs.princeton.edu/41graph/
public class Graph<T> {
    public static class Vertex<T> {
        T item;
    }

    public static class Edge<T> {
        Vertex<T> from;

        Vertex<T> to;

        public Edge(Vertex<T> from, Vertex<T> to) {
            super();
            this.from = from;
            this.to = to;
        }
    }

    List<Vertex<T>> vertices = new ArrayList<>();

    List<Edge<T>> edges = new ArrayList<>();

    public void add(Vertex<T> vertex) {
        vertices.add(vertex);
    }

    public void add(Edge<T> edge) {
        edges.add(edge);
    }
}
