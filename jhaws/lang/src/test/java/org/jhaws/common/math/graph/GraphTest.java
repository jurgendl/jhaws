package org.jhaws.common.math.graph;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GraphTest {
    @Test
    public void testGraph() {
        Graph<String> g = new Graph<>();
        for (int i = 1; i <= 7; i++) {
            g.add(new Node<>("" + (i * 10)));
        }
        g.addEdge(1 - 1, 3 - 1);
        g.addEdge(2 - 1, 1 - 1);
        g.addEdge(2 - 1, 3 - 1);
        g.addEdge(2 - 1, 6 - 1);
        g.addEdge(2 - 1, 5 - 1);
        g.addEdge(3 - 1, 6 - 1);
        g.addEdge(4 - 1, 1 - 1);
        g.addEdge(4 - 1, 2 - 1);
        g.addEdge(5 - 1, 7 - 1);
        g.addEdge(6 - 1, 7 - 1);
        Assert.assertEquals(Arrays.asList(g.node(4 - 1), g.node(1 - 1), g.node(2 - 1), g.node(3 - 1), g.node(6 - 1), g.node(5 - 1), g.node(7 - 1)),
                g.breathFirst(g.node(4 - 1)));
        Assert.assertEquals(Arrays.asList(g.node(4 - 1), g.node(2 - 1), g.node(5 - 1), g.node(7 - 1), g.node(6 - 1), g.node(3 - 1), g.node(1 - 1)),
                g.depthFirst(g.node(4 - 1)));
    }
}
