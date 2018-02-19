package org.jhaws.common.g11n;

import org.jhaws.common.g11n.Weight.WeightUnit;
import org.junit.Test;

public class WeightTest {
    @Test
    public void test() {
        // 1000oz= 28349.52g
        {
            Weight w = new Weight(WeightUnit.ounce, 1000);
            System.out.println(w.getGram().doubleValue());
        }
        // 1000g= 35.27396oz
        {
            Weight w = new Weight(WeightUnit.gram, 1000);
            System.out.println(w.getOunce().doubleValue());
        }
        // 1kg= 2lb 3.273965oz
        {
            Weight w = new Weight(WeightUnit.gram, 1000);
            System.out.println(w.getPound().doubleValue());
        }
        // 1000lb= 453.5923kg
        {
            Weight w = new Weight(WeightUnit.pound, 1000);
            System.out.println(w.getGram().doubleValue());
        }
        // 1000kg= 157st 6.622560lb
        {
            Weight w = new Weight(WeightUnit.gram, 1000000);
            System.out.println(w.getStone().doubleValue());
        }
        // 1000st= 6350.293kg
        {
            Weight w = new Weight(WeightUnit.stone, 1000);
            System.out.println(w.getGram().doubleValue());
        }
    }
}
