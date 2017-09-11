package org.jhaws.common.math;

import java.util.List;
import java.util.function.UnaryOperator;

public interface MathUtils {
    public static int percent(double d) {
        return (int) (100 * d);
    }

    public static double round(int t, int n, int decimals) {
        double exp = Math.pow(10, decimals);
        return Math.round(exp * t / n) / exp;
    }

    public static double round(double d, int decimals) {
        double exp = Math.pow(10, decimals);
        return Math.round(exp * d) / exp;
    }

    public static int floor(int t, int n) {
        return t / n;
    }

    public static int ceiling(int t, int n) {
        return t / n + (t % n > 0 ? 1 : 0);
    }

    // https://stackoverflow.com/questions/2663115/how-to-detect-a-loop-in-a-linked-list
    // http://www.siafoo.net/algorithm/10
    // http://www.siafoo.net/algorithm/11

    static public <T> boolean hasLoopBrent(T item, UnaryOperator<T> next) {
        if (item == null || next == null) {
            return false;
        }
        T slow = item, fast = item;
        int taken = 0, limit = 2;
        while (next.apply(fast) != null) {
            fast = next.apply(fast);
            taken++;
            if (slow == fast) {
                return true;
            }
            if (taken == limit) {
                taken = 0;
                limit <<= 1; // equivalent to limit *= 2;
                slow = fast; // teleporting the turtle (to the hare's position)
            }
        }
        return false;
    }

    static public <T> boolean hasLoopBrent(Node<T> root) {
        return hasLoopBrent(root, Node::next);
    }

    static public <T> boolean hasLoopBrent(TreeNode<T> leaf) {
        return hasLoopBrent(leaf, TreeNode::parent);
    }

    static public <T> boolean hasLoopBrent(List<T> list) {
        return !list.isEmpty() && hasLoopBrent(list.get(0), in -> {
            try {
                return list.get(list.indexOf(in) + 1);
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        });
    }

    static public <T> boolean hasLoopBrent(TreeModel<T> model) {
        return model.nodes().stream().filter(model::isLeaf).anyMatch(MathUtils::hasLoopBrent);
    }

    static public <T> boolean hasLoopFloyd(T top, UnaryOperator<T> next) {
        T tortoise = top;
        T hare = top;
        while (true) {
            if (next.apply(hare) == null) {
                return false;
            }
            hare = next.apply(hare);
            if (next.apply(hare) == null) {
                return false;
            }
            hare = next.apply(hare);
            tortoise = next.apply(tortoise);
            if (hare.equals(tortoise)) {
                return true;
            }
        }
    }

    static public <T> boolean hasLoopFloyd(Node<T> root) {
        return hasLoopFloyd(root, Node::next);
    }

    static public <T> boolean hasLoopFloyd(TreeNode<T> leaf) {
        return hasLoopFloyd(leaf, TreeNode::parent);
    }

    static public <T> boolean hasLoopFloyd(List<T> list) {
        return !list.isEmpty() && hasLoopFloyd(list.get(0), in -> {
            try {
                return list.get(list.indexOf(in) + 1);
            } catch (ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        });
    }

    static public <T> boolean hasLoopFloyd(TreeModel<T> model) {
        return model.nodes().stream().filter(model::isLeaf).anyMatch(MathUtils::hasLoopFloyd);
    }
}
