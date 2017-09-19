package org.jhaws.common.math;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

// https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)
// http://www.geeksforgeeks.org/?p=18212
// http://algs4.cs.princeton.edu/41graph/
public class Graph<T> {
    public static class Node<T> {
        protected T item;

        protected Double[] coordinates;

        public Node() {
            super();
        }

        public Node(T item) {
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

        public Double[] getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(Double[] coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class Edge<T> {
        protected Node<T> from;

        protected Node<T> to;

        protected Double weight;

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
            Edge<?> other = (Edge<?>) obj;
            if (this.from == null) {
                if (other.from != null) return false;
            } else if (!this.from.equals(other.from)) return false;
            if (this.to == null) {
                if (other.to != null) return false;
            } else if (!this.to.equals(other.to)) return false;
            return true;
        }

        public Double getWeight() {
            return this.weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }
    }

    protected List<Node<T>> nodes = new ArrayList<>();

    protected List<Edge<T>> edges = new ArrayList<>();

    public int add(Node<T> node) {
        nodes.add(node);
        return nodes.size() - 1;
    }

    public int addEdge(int from, int to) {
        return add(new Edge<>(node(from), node(to)));
    }

    public int add(Edge<T> edge) {
        edges.add(edge);
        return edges.size() - 1;
    }

    public Edge<T> edge(int index) {
        return edges.get(index);
    }

    public Node<T> node(int index) {
        return nodes.get(index);
    }

    public int index(Node<T> node) {
        return nodes.indexOf(node);
    }

    public List<Node<T>> findNeighbours(Node<T> node) {
        List<Node<T>> neighbours = edges.stream().filter(e -> e.from == node).map(e -> e.to).collect(Collectors.toList());
        edges.stream().filter(e -> e.to == node).map(e -> e.from).forEach(neighbours::add);
        return neighbours;
    }

    // https://www.java2blog.com/breadth-first-search-in-java/
    public List<Node<T>> breathFirst(Node<T> root) {
        HashSet<Node<T>> visited = new HashSet<>();
        Queue<Node<T>> queue = new LinkedList<>();
        List<Node<T>> result = new ArrayList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Node<T> element = queue.remove();
            result.add(element);
            List<Node<T>> neighbours = findNeighbours(element);
            for (int i = 0; i < neighbours.size(); i++) {
                Node<T> n = neighbours.get(i);
                if (n != null && !visited.contains(n)) {
                    queue.add(n);
                    visited.add(root);
                }
            }
        }
        return result;
    }

    // https://www.java2blog.com/depth-first-search-in-java/
    public List<Node<T>> depthFirst(Node<T> root) {
        HashSet<Node<T>> visited = new HashSet<>();
        List<Node<T>> result = new ArrayList<>();
        Stack<Node<T>> stack = new Stack<Node<T>>();
        stack.add(root);
        visited.add(root);
        while (!stack.isEmpty()) {
            Node<T> element = stack.pop();
            result.add(element);
            List<Node<T>> neighbours = findNeighbours(element);
            for (int i = 0; i < neighbours.size(); i++) {
                Node<T> n = neighbours.get(i);
                if (n != null && !visited.contains(n)) {
                    stack.add(n);
                    visited.add(root);
                }
            }
        }
        return result;
    }
    // public List<Node<T>> dfs(Node<T> node) {
    // ArrayList<Node<T>> dfs = new ArrayList<>();
    // dfs(dfs, node);
    // return dfs;
    // }
    // public void dfs(List<Node<T>> dfs, Node<T> node) {
    // dfs.add(node);
    // List<Node<T>> neighbours = findNeighbours(node);
    // for (int i = 0; i < neighbours.size(); i++) {
    // Node<T> n = neighbours.get(i);
    // if (n != null && !Boolean.TRUE.equals(visited.get(n))) {
    // dfs(dfs, n);
    // visited.put(node, true);
    // }
    // }
    // }
}
