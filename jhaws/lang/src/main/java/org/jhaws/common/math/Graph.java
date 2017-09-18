package org.jhaws.common.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

// https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)
// http://www.geeksforgeeks.org/?p=18212
// http://algs4.cs.princeton.edu/41graph/
public class Graph<T> {
    public static class Vertex<T> {
        protected T item;

        public Vertex() {
            super();
        }

        public Vertex(T item) {
            super();
            this.item = item;
        }

        public T getItem() {
            return this.item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return item.toString();
        }
    }

    public static class Edge<T> {
        protected Vertex<T> from;

        protected Vertex<T> to;

        public Edge(Vertex<T> from, Vertex<T> to) {
            super();
            this.from = from;
            this.to = to;
        }

        public Vertex<T> getFrom() {
            return this.from;
        }

        public void setFrom(Vertex<T> from) {
            this.from = from;
        }

        public Vertex<T> getTo() {
            return this.to;
        }

        public void setTo(Vertex<T> to) {
            this.to = to;
        }

        @Override
        public String toString() {
            return from.toString() + "->" + to.toString();
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
            Edge<?> other = (Edge<?>) obj;
            if (this.from == null) {
                if (other.from != null) return false;
            } else if (!this.from.equals(other.from)) return false;
            if (this.to == null) {
                if (other.to != null) return false;
            } else if (!this.to.equals(other.to)) return false;
            return true;
        }
    }

    protected List<Vertex<T>> vertices = new ArrayList<>();

    protected List<Edge<T>> edges = new ArrayList<>();

    public int add(Vertex<T> vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public int addEdge(int from, int to) {
        return add(new Edge<>(vertex(from), vertex(to)));
    }

    public int add(Edge<T> edge) {
        edges.add(edge);
        return edges.size() - 1;
    }

    public Edge<T> edge(int index) {
        return edges.get(index);
    }

    public Vertex<T> vertex(int index) {
        return vertices.get(index);
    }

    public int index(Vertex<T> vertex) {
        return vertices.indexOf(vertex);
    }

    public List<Vertex<T>> findNeighbours(Vertex<T> x) {
        List<Vertex<T>> neighbours = edges.stream().filter(e -> e.from == x).map(e -> e.to).collect(Collectors.toList());
        edges.stream().filter(e -> e.to == x).map(e -> e.from).forEach(neighbours::add);
        return neighbours;
    }

    public List<Vertex<T>> breathFirst(Vertex<T> node) {
        return new BreathFirst().bfs(node);
    }

    public List<Vertex<T>> depthFirst(Vertex<T> node) {
        return new DepthFirst().dfsUsingStack(node);
    }

    // https://www.java2blog.com/breadth-first-search-in-java/
    protected class BreathFirst {
        protected Queue<Vertex<T>> queue = new LinkedList<>();

        protected Map<Vertex<T>, Boolean> visited = new HashMap<>();

        public List<Vertex<T>> bfs(Vertex<T> node) {
            List<Vertex<T>> bfs = new ArrayList<>();
            queue.add(node);
            visited.put(node, true);
            while (!queue.isEmpty()) {
                Vertex<T> element = queue.remove();
                bfs.add(element);
                List<Vertex<T>> neighbours = findNeighbours(element);
                for (int i = 0; i < neighbours.size(); i++) {
                    Vertex<T> n = neighbours.get(i);
                    if (n != null && !Boolean.TRUE.equals(visited.get(n))) {
                        queue.add(n);
                        visited.put(n, true);
                    }
                }
            }
            return bfs;
        }
    }

    // https://www.java2blog.com/depth-first-search-in-java/
    protected class DepthFirst {
        protected Map<Vertex<T>, Boolean> visited = new HashMap<>();

        public List<Vertex<T>> dfsUsingStack(Vertex<T> node) {
            List<Vertex<T>> dfs = new ArrayList<>();
            Stack<Vertex<T>> stack = new Stack<Vertex<T>>();
            stack.add(node);
            visited.put(node, true);
            while (!stack.isEmpty()) {
                Vertex<T> element = stack.pop();
                dfs.add(element);
                List<Vertex<T>> neighbours = findNeighbours(element);
                for (int i = 0; i < neighbours.size(); i++) {
                    Vertex<T> n = neighbours.get(i);
                    if (n != null && !Boolean.TRUE.equals(visited.get(n))) {
                        stack.add(n);
                        visited.put(n, true);
                    }
                }
            }
            return dfs;
        }

        public List<Vertex<T>> dfs(Vertex<T> node) {
            ArrayList<Vertex<T>> dfs = new ArrayList<>();
            dfs(dfs, node);
            return dfs;
        }

        public void dfs(List<Vertex<T>> dfs, Vertex<T> node) {
            dfs.add(node);
            List<Vertex<T>> neighbours = findNeighbours(node);
            for (int i = 0; i < neighbours.size(); i++) {
                Vertex<T> n = neighbours.get(i);
                if (n != null && !Boolean.TRUE.equals(visited.get(n))) {
                    dfs(dfs, n);
                    visited.put(node, true);
                }
            }
        }
    }
}
