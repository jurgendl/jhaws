package org.jhaws.common.math.graph;

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
