package org.jhaws.common.lambda;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LambdaPathTest {
    @Test
    public void test() {
        LambdaPath<LambdaPathB> a = LambdaPath.p(LambdaPathA::getB);
        System.out.println(a);
        Assertions.assertEquals("b", a.getFullPath());
        try {
            LambdaPath<String> d = LambdaPath.p((LambdaPathA la) -> la.getB().getBs());
            System.out.println(d);
            Assertions.fail("expected exception");
        } catch (IllegalArgumentException ex) {
            //
        }
        LambdaPath<LambdaPathC> b = LambdaPath.p(LambdaPathA::getB).j(LambdaPathB::getC);
        System.out.println(b);
        Assertions.assertEquals("b.c", b.getFullPath());
        LambdaPath<String> c = LambdaPath.p(LambdaPathA::getB).j(LambdaPathB::getC).j(LambdaPathC::getCs);
        System.out.println(c);
        Assertions.assertEquals("b.c.cs", c.getFullPath());
        LambdaPath<String> e = LambdaPath.p(LambdaPathD::getDs);
        System.out.println(e);
        Assertions.assertEquals("ds", e.getFullPath());
    }
}
