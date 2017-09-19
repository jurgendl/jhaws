package org.jhaws.common.math.graph;

import static org.jhaws.common.lang.Pair.pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jhaws.common.lang.Pair;
import org.junit.Assert;
import org.junit.Test;

public class GraphTest {
    @Test
    public void breathFirst() {
        Graph<Integer, Integer> g = new Graph<>();
        for (int i = 1; i <= 7; i++) {
            g.addNode(new Node<>(i));
        }
        g.addEdge(0, 2);
        g.addEdge(1, 0);
        g.addEdge(1, 2);
        g.addEdge(1, 5);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 0);
        g.addEdge(3, 1);
        g.addEdge(4, 6);
        g.addEdge(5, 6);
        Assert.assertEquals(Arrays.asList(g.node(3), g.node(0), g.node(1), g.node(2), g.node(5), g.node(4), g.node(6)), g.breathFirst(g.node(3)));
    }

    @Test
    public void depthFirst() {
        Graph<Integer, Integer> g = new Graph<>();
        for (int i = 1; i <= 7; i++) {
            g.addNode(new Node<>(i));
        }
        g.addEdge(0, 2);
        g.addEdge(1, 0);
        g.addEdge(1, 2);
        g.addEdge(1, 5);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 0);
        g.addEdge(3, 1);
        g.addEdge(4, 6);
        g.addEdge(5, 6);
        Assert.assertEquals(Arrays.asList(g.node(3), g.node(1), g.node(4), g.node(6), g.node(5), g.node(2), g.node(0)), g.depthFirst(g.node(3)));
    }

    @Test
    public void fwi() {
        Graph<Integer, Integer> g = new Graph<>();
        g.addNodes(Arrays.asList(new Node<>(0), new Node<>(1), new Node<>(2), new Node<>(3)));
        g.addEdge(0, 1).weight(5);
        g.addEdge(1, 2).weight(3);
        g.addEdge(2, 3).weight(1);
        g.addEdge(0, 3).weight(1);
        Map<Pair<Node<Integer>>, Integer> w = g.wfi();
        Map<Pair<Node<Integer>>, Integer> c = new HashMap<>();
        c.put(pair(g.node(0), g.node(1)), 5);
        c.put(pair(g.node(0), g.node(2)), 8);
        c.put(pair(g.node(0), g.node(3)), 1);
        c.put(pair(g.node(1), g.node(2)), 3);
        c.put(pair(g.node(1), g.node(3)), 4);
        c.put(pair(g.node(2), g.node(3)), 1);
        Assert.assertEquals(c, w);
    }

    @Test
    public void bf() {
        // Bellman-Ford
        // http://www.geeksforgeeks.org/dynamic-programming-set-23-bellman-ford-algorithm/
    }
}
