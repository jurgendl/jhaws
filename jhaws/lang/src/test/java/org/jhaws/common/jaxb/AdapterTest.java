package org.jhaws.common.jaxb;

import org.jhaws.common.jaxb.adapters.DoubleBiArrayAdapter;
import org.jhaws.common.jaxb.adapters.FloatBiArrayAdapter;
import org.jhaws.common.jaxb.adapters.IntegerBiArrayAdapter;
import org.jhaws.common.jaxb.adapters.LongBiArrayAdapter;
import org.jhaws.common.jaxb.adapters.ShortBiArrayAdapter;
import org.junit.Assert;
import org.junit.Test;

public class AdapterTest {
    @Test
    public void testShort() {
        System.out.println("------testShort------");
        Short[][] v = { { Short.MIN_VALUE, (short) 1 }, { (short) 2, Short.MAX_VALUE } };
        ShortBiArrayAdapter a = new ShortBiArrayAdapter();
        String s = a.marshal(v);
        System.out.println(s);
        Short[][] vv = a.unmarshal(s);
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[i].length; j++) {
                Assert.assertEquals(v[i][j], vv[i][j]);
            }
        }
        for (int i = 0; i < vv.length; i++) {
            for (int j = 0; j < vv[i].length; j++) {
                Assert.assertEquals(vv[i][j], v[i][j]);
            }
        }
    }

    @Test
    public void testInteger() {
        System.out.println("------testInteger------");
        Integer[][] v = { { Integer.MIN_VALUE, 1 }, { 2, Integer.MAX_VALUE } };
        IntegerBiArrayAdapter a = new IntegerBiArrayAdapter();
        String s = a.marshal(v);
        System.out.println(s);
        Integer[][] vv = a.unmarshal(s);
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[i].length; j++) {
                Assert.assertEquals(v[i][j], vv[i][j]);
            }
        }
        for (int i = 0; i < vv.length; i++) {
            for (int j = 0; j < vv[i].length; j++) {
                Assert.assertEquals(vv[i][j], v[i][j]);
            }
        }
    }

    @Test
    public void testLong() {
        System.out.println("------testLong------");
        Long[][] v = { { Long.MIN_VALUE, 1l }, { 2l, Long.MAX_VALUE } };
        LongBiArrayAdapter a = new LongBiArrayAdapter();
        String s = a.marshal(v);
        System.out.println(s);
        Long[][] vv = a.unmarshal(s);
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[i].length; j++) {
                Assert.assertEquals(v[i][j], vv[i][j]);
            }
        }
        for (int i = 0; i < vv.length; i++) {
            for (int j = 0; j < vv[i].length; j++) {
                Assert.assertEquals(vv[i][j], v[i][j]);
            }
        }
    }

    @Test
    public void testFloat() {
        System.out.println("------testFloat------");
        Float[][] v = { { Float.MIN_VALUE, 1f }, { 2f, Float.MAX_VALUE } };
        FloatBiArrayAdapter a = new FloatBiArrayAdapter();
        String s = a.marshal(v);
        System.out.println(s);
        Float[][] vv = a.unmarshal(s);
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[i].length; j++) {
                Assert.assertEquals(v[i][j], vv[i][j]);
            }
        }
        for (int i = 0; i < vv.length; i++) {
            for (int j = 0; j < vv[i].length; j++) {
                Assert.assertEquals(vv[i][j], v[i][j]);
            }
        }
    }

    @Test
    public void testDouble() {
        System.out.println("------testDouble------");
        Double[][] v = { { Double.MIN_VALUE, 1.0 }, { 2.0, Double.MAX_VALUE } };
        DoubleBiArrayAdapter a = new DoubleBiArrayAdapter();
        String s = a.marshal(v);
        System.out.println(s);
        Double[][] vv = a.unmarshal(s);
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[i].length; j++) {
                Assert.assertEquals(v[i][j], vv[i][j]);
            }
        }
        for (int i = 0; i < vv.length; i++) {
            for (int j = 0; j < vv[i].length; j++) {
                Assert.assertEquals(vv[i][j], v[i][j]);
            }
        }
    }
}
