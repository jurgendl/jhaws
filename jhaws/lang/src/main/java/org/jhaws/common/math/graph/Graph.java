package org.jhaws.common.math.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jhaws.common.lang.Pair;

// https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)
// http://www.geeksforgeeks.org/category/data-structures/graph/
// http://algs4.cs.princeton.edu/41graph/
public class Graph<T, N extends Number> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected List<Node<T>> nodes = new ArrayList<>();

    protected List<Edge<T, N>> edges = new ArrayList<>();

    public int addNode(Node<T> node) {
        nodes.add(node);
        return nodes.size() - 1;
    }

    public int addNodes(List<Node<T>> newNodes) {
        nodes.addAll(newNodes);
        return nodes.size() - 1;
    }

    public int addNodes(Node<T>[] newNodes) {
        for (int i = 0; i < newNodes.length; i++) {
            nodes.add(newNodes[i]);
        }
        return nodes.size() - 1;
    }

    public Edge<T, N> addEdge(int from, int to) {
        Edge<T, N> edge = new Edge<>(node(from), node(to));
        addEdge(edge);
        return edge;
    }

    public int addEdge(Edge<T, N> edge) {
        edges.add(edge);
        return edges.size() - 1;
    }

    public Edge<T, N> addEdge(Node<T> from, Node<T> to) {
        Edge<T, N> edge = new Edge<>(from, to);
        edges.add(edge);
        return edge;
    }

    public int addEdges(Edge<T, N>[] newEdges) {
        for (int i = 0; i < newEdges.length; i++) {
            edges.add(newEdges[i]);
        }
        return edges.size() - 1;
    }

    public int addEdges(List<Edge<T, N>> newEdges) {
        edges.addAll(newEdges);
        return edges.size() - 1;
    }

    public Edge<T, N> edge(int index) {
        return edges.get(index);
    }

    public Node<T> node(int index) {
        return nodes.get(index);
    }

    public int index(Node<T> node) {
        return nodes.indexOf(node);
    }

    public List<Node<T>> neighbours(Node<T> node) {
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
            List<Node<T>> neighbours = neighbours(element);
            for (int i = 0; i < neighbours.size(); i++) {
                Node<T> n = neighbours.get(i);
                if (n != null && !visited.contains(n)) {
                    queue.add(n);
                    visited.add(n);
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
            List<Node<T>> neighbours = neighbours(element);
            for (int i = 0; i < neighbours.size(); i++) {
                Node<T> n = neighbours.get(i);
                if (n != null && !visited.contains(n)) {
                    stack.add(n);
                    visited.add(n);
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

    public int v() {
        return nodes.size();
    }

    public N weight(Node<T> from, Node<T> to) {
        return edges.stream().filter(e -> e.from.equals(from) && e.to.equals(to)).map(Edge::getWeight).findFirst().orElse(null);
    }

    protected boolean lt(N w1, N w2) {
        if (w1 == null && w2 == null) {
            return false;
        }
        if (w1 == null) {
            return false;
        }
        if (w2 == null) {
            return true;
        }
        if (w1 instanceof Double && w2 instanceof Double) {
            return ((double) w1 < (double) w2);
        }
        if (w1 instanceof Float && w2 instanceof Float) {
            return ((float) w1 < (float) w2);
        }
        if (w1 instanceof Long && w2 instanceof Long) {
            return ((long) w1 < (long) w2);
        }
        if (w1 instanceof Integer && w2 instanceof Integer) {
            return ((int) w1 < (int) w2);
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    protected N add(N w1, N w2) {
        if (w1 == null || w2 == null) {
            return null;
        }
        if (w1 instanceof Double && w2 instanceof Double) {
            return (N) (Double) ((Double) w1 + (Double) w2);
        }
        if (w1 instanceof Float && w2 instanceof Float) {
            return (N) (Float) ((Float) w1 + (Float) w2);
        }
        if (w1 instanceof Long && w2 instanceof Long) {
            return (N) (Long) ((Long) w1 + (Long) w2);
        }
        if (w1 instanceof Integer && w2 instanceof Integer) {
            return (N) (Integer) ((Integer) w1 + (Integer) w2);
        }
        throw new UnsupportedOperationException();
    }

    // http://www.geeksforgeeks.org/dynamic-programming-set-16-floyd-warshall-algorithm/
    public Map<Pair<Node<T>>, N> wfi() {
        int v = v();
        SortedMap<Pair<Node<T>>, N> dist = new TreeMap<>();
        int i, j, k;
        for (i = 0; i < v; i++) {
            Node<T> ni = node(i);
            for (j = 0; j < v; j++) {
                Node<T> nj = node(j);
                N w = weight(ni, nj);
                if (w != null) {
                    dist.put(new Pair<>(ni, nj), w);
                }
            }
        }
        for (k = 0; k < v; k++) {
            Node<T> nk = node(k);
            for (i = 0; i < v; i++) {
                Node<T> ni = node(i);
                for (j = 0; j < v; j++) {
                    Node<T> nj = node(j);
                    Pair<Node<T>> ik = new Pair<>(ni, nk);
                    Pair<Node<T>> kj = new Pair<>(nk, nj);
                    Pair<Node<T>> ij = new Pair<>(ni, nj);
                    N dik = dist.get(ik);
                    N dkj = dist.get(kj);
                    N dij = dist.get(ij);
                    if (lt(add(dik, dkj), dij)) {
                        dist.put(ij, add(dik, dkj));
                    }
                }
            }
        }
        return dist;
    }
}
