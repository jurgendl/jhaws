package org.jhaws.common.math;

import java.util.Arrays;

import org.jhaws.common.math.Graph.Vertex;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testIsLoop1() {
        Node<Integer> _0 = null;
        Node<Integer> it = null;
        Node<Integer> _2 = null;
        for (int i = 0; i <= 6; i++) {
            it = new Node<>(it, i);
            if (i == 2) _2 = it;
            if (i == 0) _0 = it;
        }
        it.next(_2);
        Assert.assertTrue(MathUtils.hasLoopBrent(_0));
    }

    @Test
    public void testIsLoop2() {
        Assert.assertTrue(MathUtils.hasLoopBrent(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 2)));
    }

    @Test
    public void testIsLoop3() {
        Node<Integer> _0 = null;
        Node<Integer> it = null;
        for (int i = 0; i <= 7; i++) {
            it = new Node<>(it, i);
            if (i == 0) _0 = it;
        }
        Assert.assertFalse(MathUtils.hasLoopBrent(_0));
    }

    @Test
    public void testIsLoop4() {
        Assert.assertFalse(MathUtils.hasLoopBrent(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)));
    }

    @Test
    public void testIsLoop5() {
        Node<Integer> _0 = null;
        Node<Integer> it = null;
        Node<Integer> _2 = null;
        for (int i = 0; i <= 6; i++) {
            it = new Node<>(it, i);
            if (i == 2) _2 = it;
            if (i == 0) _0 = it;
        }
        it.next(_2);
        Assert.assertTrue(MathUtils.hasLoopFloyd(_0));
    }

    @Test
    public void testIsLoop6() {
        Assert.assertTrue(MathUtils.hasLoopFloyd(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 2)));
    }

    @Test
    public void testIsLoop7() {
        Node<Integer> _0 = null;
        Node<Integer> it = null;
        for (int i = 0; i <= 7; i++) {
            it = new Node<>(it, i);
            if (i == 0) _0 = it;
        }
        Assert.assertFalse(MathUtils.hasLoopFloyd(_0));
    }

    @Test
    public void testIsLoop8() {
        Assert.assertFalse(MathUtils.hasLoopFloyd(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)));
    }

    @Test
    public void testGraph() {
        Graph<String> g = new Graph<>();
        for (int i = 1; i <= 7; i++) {
            g.add(new Vertex<>("" + (i * 10)));
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
        Assert.assertEquals(
                Arrays.asList(g.vertex(4 - 1), g.vertex(1 - 1), g.vertex(2 - 1), g.vertex(3 - 1), g.vertex(6 - 1), g.vertex(5 - 1), g.vertex(7 - 1)),
                g.breathFirst(g.vertex(4 - 1)));
        Assert.assertEquals(
                Arrays.asList(g.vertex(4 - 1), g.vertex(2 - 1), g.vertex(5 - 1), g.vertex(7 - 1), g.vertex(6 - 1), g.vertex(3 - 1), g.vertex(1 - 1)),
                g.depthFirst(g.vertex(4 - 1)));
    }
}
