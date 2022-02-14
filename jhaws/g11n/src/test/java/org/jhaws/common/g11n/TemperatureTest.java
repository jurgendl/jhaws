package org.jhaws.common.g11n;

import java.math.BigDecimal;

import org.jhaws.common.g11n.Temperature.TemperatureUnit;
import org.junit.Assert;
import org.junit.Test;

public class TemperatureTest {
    TemperatureUnit[] units = { TemperatureUnit.Celsius, TemperatureUnit.Fahrenheit, TemperatureUnit.Kelvin, TemperatureUnit.Rankine, TemperatureUnit.Delisle, TemperatureUnit.Newton, TemperatureUnit.Réaumur, TemperatureUnit.Rømer };

    boolean[] doTest = { true, true, true, true, true, true, true, true };

    @Test
    public void test1() {
        System.out.println("--------------------------------------");
        try {
            double[] var = { 300.00, 572.00, 573.15, 1031.67, -300.00, 99.00, 240.00, 165.00 };
            Temperature t = new Temperature(TemperatureUnit.Celsius, var[0]);
            for (int i = 0; i < units.length; i++) {
                if (!doTest[i]) continue;
                TemperatureUnit u = units[i];
                double check = var[i];
                BigDecimal vv = t.get(u);
                Temperature tt = new Temperature(u, vv);
                System.out.println(tt);
                System.out.println(check + "=" + vv.doubleValue());
                System.out.println(var[0] + "=" + tt.getCelsius().doubleValue());
                System.out.println();
                Assert.assertEquals(check, vv.doubleValue(), 10);
                Assert.assertEquals(var[0], tt.getCelsius().doubleValue(), 10);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("");
        }
        System.out.println("--------------------------------------");
    }

    @Test
    public void test2() {
        System.out.println("--------------------------------------");
        try {
            double[] var = { -273.15, -459.67, 0.00, 0.00, 559.725, -90.1395, -218.52, -135.90375 };
            Temperature t = new Temperature(TemperatureUnit.Celsius, var[0]);
            for (int i = 0; i < units.length; i++) {
                if (!doTest[i]) continue;
                TemperatureUnit u = units[i];
                double check = var[i];
                BigDecimal vv = t.get(u);
                Temperature tt = new Temperature(u, vv);
                System.out.println(tt);
                System.out.println(check + "=" + vv.doubleValue());
                System.out.println(var[0] + "=" + tt.getCelsius().doubleValue());
                System.out.println();
                Assert.assertEquals(check, vv.doubleValue(), 10);
                Assert.assertEquals(var[0], tt.getCelsius().doubleValue(), 10);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("");
        }
        System.out.println("--------------------------------------");
    }

    // @Test
    // public void testZero() {
    // new Temperature(TemperatureUnit.Kelvin, 0);
    // new Temperature(TemperatureUnit.Kelvin, 0.0000000000000000000000001);
    // try {
    // new Temperature(TemperatureUnit.Kelvin, -0.1);
    // Assert.fail("");
    // } catch (IllegalArgumentException ex) {
    // //
    // }
    // }
}
