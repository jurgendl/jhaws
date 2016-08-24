package org.jhaws.common.g11n;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @see https://en.wikipedia.org/wiki/Conversion_of_units_of_temperature
 */
public class Temperature implements Serializable {
	private static final BigDecimal CTE_ROMER_B = div(40, 21);

	private static final BigDecimal CTE_ROMER_A = div(75, 10);

	private static final BigDecimal CTE_REAUMUR = div(5, 4);

	private static final BigDecimal CTE_RANKINE = div(49167, 100);

	private static final BigDecimal CTE_NEWTON = div(100, 33);

	private static final BigDecimal CTE_FAHRENHEIT = div(5, 9);

	private static final BigDecimal CTE_DELISLE = div(2, 3);

	private static final BigDecimal CTE_KELVIN = div(27315, 100);

	private static final long serialVersionUID = 3786946493426325164L;

	protected static final NumberFormat FORMATTER = NumberFormat.getInstance();

	protected static final int PRECISION = 100;

	public static enum TemperatureUnit {
		Celsius("°C"), Kelvin("K"), Fahrenheit("°F"), Rankine("°R"), Delisle("°De"), Newton("°N"), Réaumur("°Ré"), Rømer("°Rø");

		String unit;

		TemperatureUnit(String unit) {
			this.unit = unit;
		}
	}

	protected TemperatureUnit temperatureUnit;

	protected BigDecimal temperature;

	public Temperature(TemperatureUnit temperatureUnit, long temperature) {
		this(temperatureUnit, bd(temperature));
	}

	public Temperature(TemperatureUnit temperatureUnit, double temperature) {
		this(temperatureUnit, bd(temperature));
	}

	public Temperature(TemperatureUnit temperatureUnit, BigDecimal temperature) {
		this.temperatureUnit = temperatureUnit;
		this.temperature = temperature;
		check();
	}

	public Temperature() {
		super();
	}

	@Override
	public String toString() {
		return FORMATTER.format(temperature) + temperatureUnit.unit;
	}

	public BigDecimal get(TemperatureUnit unit) {
		switch (unit) {
			case Celsius:
				return getCelsius();
			case Delisle:
				return getDelisle();
			case Fahrenheit:
				return getFahrenheit();
			case Kelvin:
				return getKelvin();
			case Newton:
				return getNewton();
			case Rankine:
				return getRankine();
			case Réaumur:
				return getRéaumur();
			case Rømer:
				return getRømer();
		}
		throw new UnsupportedOperationException();
	}

	public void set(TemperatureUnit unit, BigDecimal temp) {
		temperatureUnit = unit;
		temperature = temp;
		check();
	}

	protected void check() throws IllegalArgumentException {
		// if (getKelvin().longValue() < 0)
		// throw new IllegalArgumentException("Absolute zero");
	}

	public static BigDecimal bd(long val) {
		return BigDecimal.valueOf(val);
	}

	public static BigDecimal bd(double val) {
		return BigDecimal.valueOf(val);
	}

	public static BigDecimal multiply(long val, long multiplicand) {
		return multiply(bd(val), multiplicand);
	}

	public static BigDecimal multiply(BigDecimal val, long multiplicand) {
		return multiply(val, bd(multiplicand));
	}

	public static BigDecimal multiply(BigDecimal val, BigDecimal multiplicand) {
		return val.multiply(multiplicand);
	}

	public static BigDecimal div(long val, long divisor) {
		return div(bd(val), divisor);
	}

	public static BigDecimal div(BigDecimal val, long divisor) {
		return div(val, bd(divisor));
	}

	public static BigDecimal div(BigDecimal val, BigDecimal divisor) {
		return val.divide(divisor, PRECISION, RoundingMode.HALF_UP);
	}

	public static BigDecimal add(long val, long augend) {
		return add(bd(val), augend);
	}

	public static BigDecimal add(BigDecimal val, long augend) {
		return add(val, bd(augend));
	}

	public static BigDecimal add(BigDecimal val, BigDecimal augend) {
		return val.add(augend);
	}

	public BigDecimal getCelsius() {
		switch (temperatureUnit) {
			case Celsius:
				return temperature;
			case Delisle:
				// 100 − [°De] × 2⁄3
				return add(multiply(temperature, CTE_DELISLE).negate(), 100);
			case Fahrenheit:
				// ([°F] − 32) × 5⁄9
				return multiply(add(temperature, bd(32).negate()), CTE_FAHRENHEIT);
			case Kelvin:
				// [K] − 273.15
				return add(temperature, CTE_KELVIN.negate());
			case Newton:
				// [°N] × 100⁄33
				return multiply(temperature, CTE_NEWTON);
			case Rankine:
				// ([°R] − 491.67) × 5⁄9
				return multiply(add(temperature, CTE_RANKINE.negate()), CTE_FAHRENHEIT);
			case Réaumur:
				// [°Ré] × 5⁄4
				return multiply(temperature, CTE_REAUMUR);
			case Rømer:
				// ([°Rø] − 7.5) × 40⁄21
				return multiply(add(temperature, CTE_ROMER_A.negate()), CTE_ROMER_B);
		}
		throw new UnsupportedOperationException();
	}

	public void setCelsius(BigDecimal celcius) {
		temperatureUnit = TemperatureUnit.Celsius;
		temperature = celcius;
		check();
	}

	public BigDecimal getKelvin() {
		// [K] = [°C] + 273.15
		return add(getCelsius(), CTE_KELVIN);
	}

	public void setKelvin(BigDecimal kelvin) {
		temperatureUnit = TemperatureUnit.Kelvin;
		temperature = kelvin;
		check();
	}

	public BigDecimal getFahrenheit() {
		// [°F] = [°C] × 9⁄5 + 32
		return add(multiply(getCelsius(), div(9, 5)), 32);
	}

	public void setFahrenheit(BigDecimal fahrenheit) {
		temperatureUnit = TemperatureUnit.Fahrenheit;
		temperature = fahrenheit;
		check();
	}

	public BigDecimal getDelisle() {
		// [°De] = (100 − [°C]) × 3⁄2
		return multiply(add(bd(100), getCelsius().negate()), div(3, 2));
	}

	public void setDelisle(BigDecimal delisle) {
		temperatureUnit = TemperatureUnit.Delisle;
		temperature = delisle;
		check();
	}

	public BigDecimal getNewton() {
		// [°N] = [°C] × 33⁄100
		return multiply(getCelsius(), div(33, 100));
	}

	public void setNewton(BigDecimal newton) {
		temperatureUnit = TemperatureUnit.Newton;
		temperature = newton;
		check();
	}

	public BigDecimal getRankine() {
		// [°R] = ([°C] + 273.15) × 9⁄5
		return multiply(add(getCelsius(), CTE_KELVIN), div(9, 5));
	}

	public void setRankine(BigDecimal rankine) {
		temperatureUnit = TemperatureUnit.Rankine;
		temperature = rankine;
		check();
	}

	public BigDecimal getRéaumur() {
		// [°Ré] = [°C] × 4⁄5
		return multiply(getCelsius(), div(4, 5));
	}

	public void setRéaumur(BigDecimal réaumur) {
		temperatureUnit = TemperatureUnit.Réaumur;
		temperature = réaumur;
		check();
	}

	public BigDecimal getRømer() {
		// [°Rø] = [°C] × 21⁄40 + 7.5
		return multiply(getCelsius(), div(21, 40)).add(div(75, 10));
	}

	public void setRømer(BigDecimal rømer) {
		temperatureUnit = TemperatureUnit.Rømer;
		temperature = rømer;
		check();
	}

	public TemperatureUnit getTemperatureUnit() {
		return this.temperatureUnit;
	}

	public void setTemperatureUnit(TemperatureUnit temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public BigDecimal getTemperature() {
		return this.temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}
}
