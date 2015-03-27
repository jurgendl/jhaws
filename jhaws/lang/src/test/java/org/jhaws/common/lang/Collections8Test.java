package org.jhaws.common.lang;


import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class Collections8Test {
    @Test
    public void test1() {
        Collection<String> c = Arrays.asList("v1","v2","v3");
        Collections8.array(c);
        Collections8.array(c.parallelStream());
    }
}
