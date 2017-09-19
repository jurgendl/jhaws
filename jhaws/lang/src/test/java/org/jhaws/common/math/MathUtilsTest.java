package org.jhaws.common.math;

import java.util.Arrays;

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
}
