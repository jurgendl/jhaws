package org.jhaws.common.g11n;

import java.io.Serializable;

/**
 * @see https://en.wikipedia.org/wiki/Conversion_of_units_of_temperature
 */
public class Temperature implements Serializable {
	private static final long serialVersionUID = 3786946493426325164L;

	public static enum TemperatureUnit {
		Celsius("°C"), Kelvin("K"), Fahrenheit("°F"), Rankine("°R"), Delisle("°De"), Newton("°N"), Réaumur("°Ré"), Rømer("°Rø");

		String unit;

		TemperatureUnit(String unit) {
			this.unit = unit;
		}
	}

	protected TemperatureUnit temperatureUnit;

	protected double temperature;

	public Temperature(TemperatureUnit temperatureUnit, double temperature) {
		this.temperatureUnit = temperatureUnit;
		this.temperature = temperature;
		check();
	}

	public Temperature() {
		super();
	}

	@Override
	public String toString() {
		return temperature + " " + temperatureUnit.unit;
	}

	public double get(TemperatureUnit unit) {
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

	public void set(TemperatureUnit unit, double temp) {
		temperatureUnit = unit;
		temperature = temp;
		check();
	}

	protected void check() {
		if (getKelvin() >= 0.0)
			return;
		throw new UnsupportedOperationException("Absolute zero");
	}

	public double getCelsius() {
		switch (temperatureUnit) {
			case Celsius:
				return temperature;
			case Delisle:
				return 100 - temperature * 2.0 / 3.0;
			case Fahrenheit:
				return (temperature - 32.0) * 5.0 / 9.0;
			case Kelvin:
				return temperature - 273.15;
			case Newton:
				return temperature * 100 / 33;
			case Rankine:
				return (temperature - 491.67) * 5.0 / 9.0;
			case Réaumur:
				return temperature * 5.0 / 4.0;
			case Rømer:
				return (temperature - 7.5) * 40.0 / 21.0;
		}
		throw new UnsupportedOperationException();
	}

	public void setCelsius(double celcius) {
		temperatureUnit = TemperatureUnit.Celsius;
		temperature = celcius;
		check();
	}

	public double getKelvin() {
		return getCelsius() + 273.15;
	}

	public void setKelvin(double kelvin) {
		temperatureUnit = TemperatureUnit.Kelvin;
		temperature = kelvin;
		check();
	}

	public double getFahrenheit() {
		return (getCelsius() * (9.0 / 5.0)) + 32.0;
	}

	public void setFahrenheit(double fahrenheit) {
		temperatureUnit = TemperatureUnit.Fahrenheit;
		temperature = fahrenheit;
		check();
	}

	public double getDelisle() {
		return (100 - getCelsius()) * (3.0 / 2.0);
	}

	public void setDelisle(double delisle) {
		temperatureUnit = TemperatureUnit.Delisle;
		temperature = delisle;
		check();
	}

	public double getNewton() {
		return getCelsius() * (33.0 / 100.0);
	}

	public void setNewton(double newton) {
		temperatureUnit = TemperatureUnit.Newton;
		temperature = newton;
		check();
	}

	public double getRankine() {
		return (getCelsius() + 273.15) * (9.0 / 5.0);
	}

	public void setRankine(double rankine) {
		temperatureUnit = TemperatureUnit.Rankine;
		temperature = rankine;
		check();
	}

	public double getRéaumur() {
		return getCelsius() * (4.0 / 5.0);
	}

	public void setRéaumur(double réaumur) {
		temperatureUnit = TemperatureUnit.Réaumur;
		temperature = réaumur;
		check();
	}

	public double getRømer() {
		return (getCelsius() * (21.0 / 40.0)) + 7.5;
	}

	public void setRømer(double rømer) {
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

	public double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.temperature);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.temperatureUnit == null) ? 0 : this.temperatureUnit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Temperature other = (Temperature) obj;
		if (Double.doubleToLongBits(this.temperature) != Double.doubleToLongBits(other.temperature))
			return false;
		if (this.temperatureUnit != other.temperatureUnit)
			return false;
		return true;
	}
}
