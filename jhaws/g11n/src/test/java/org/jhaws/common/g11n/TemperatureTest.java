package org.jhaws.common.g11n;

import org.jhaws.common.g11n.Temperature.TemperatureUnit;
import org.junit.Assert;
import org.junit.Test;

public class TemperatureTest {
	@Test
	public void test() {
		double[] var = { 300.00, 572.00, 573.15, 1031.67, -300.00, 99.00, 240.00, 165.00 };
		Temperature t = new Temperature(TemperatureUnit.Celsius, 300.0);
		TemperatureUnit[] units = { TemperatureUnit.Celsius, TemperatureUnit.Fahrenheit, TemperatureUnit.Kelvin, TemperatureUnit.Rankine, TemperatureUnit.Delisle,
				TemperatureUnit.Newton, TemperatureUnit.Réaumur, TemperatureUnit.Rømer };
		// double accuracy = Math.pow(10, -1000);
		// System.out.println(accuracy);
		for (int i = 0; i < units.length; i++) {
			TemperatureUnit u = units[i];
			double check = var[i];
			double vv = t.get(u);
			Temperature tt = new Temperature(u, vv);
			System.out.println(tt);
			Assert.assertEquals(check, vv, 0.0);
			Assert.assertEquals(300.0, tt.getCelsius(), 0.0);
		}
	}

	@Test
	public void testZero() {
		try {
			new Temperature(TemperatureUnit.Kelvin, -0.0000000000000000000000001);
			Assert.fail("");
		} catch (IllegalArgumentException ex) {
			//
		}
	}
}
