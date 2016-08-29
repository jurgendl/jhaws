package org.jhaws.common.g11n;

import java.math.BigDecimal;

/** mass */
// http://www.metric-conversions.org/weight-conversion.htm
public class Weight extends Helper {
	protected static final BigDecimal CTE_STONE = div(15747, 100);

	protected static final BigDecimal CTE_POUND = div(22046, 10);

	protected static final BigDecimal CTE_OUNCE = div(35274, 1000000);

	public static enum WeightUnit {
		gram("g"), ounce("oz"), pound("lb"), stone("st");

		String unit;

		WeightUnit(String unit) {
			this.unit = unit;
		}
	}

	protected WeightUnit weightUnit;

	protected BigDecimal weight;

	public Weight(WeightUnit weightUnit, BigDecimal weight) {
		this.weightUnit = weightUnit;
		this.weight = weight;
	}

	public Weight(WeightUnit weightUnit, long weight) {
		this(weightUnit, bd(weight));
	}

	public Weight(WeightUnit weightUnit, double weight) {
		this(weightUnit, bd(weight));
	}

	public Weight() {
		super();
	}

	public void set(WeightUnit unit, BigDecimal w) {
		this.weightUnit = unit;
		this.weight = w;
	}

	public BigDecimal get(WeightUnit unit) {
		switch (unit) {
			case gram:
				return getGram();
			case ounce:
				return getOunce();
			case pound:
				return getPound();
			case stone:
				return getStone();
		}
		throw new UnsupportedOperationException();
	}

	public BigDecimal getGram() {
		switch (weightUnit) {
			case gram:
				return weight;
			case ounce:
				return div(weight, CTE_OUNCE);
			case pound:
				return div(weight, CTE_POUND);
			case stone:
				return multiply(weight, CTE_STONE);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return FORMATTER.format(weight) + weightUnit.unit;
	}

	public WeightUnit getWeightUnit() {
		return this.weightUnit;
	}

	public void setWeightUnit(WeightUnit weightUnit) {
		this.weightUnit = weightUnit;
	}

	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public void setGram(BigDecimal gram) {
		this.weightUnit = WeightUnit.gram;
		this.weight = gram;
	}

	public BigDecimal getOunce() {
		return multiply(getGram(), CTE_OUNCE);
	}

	public void setOunce(BigDecimal ounce) {
		this.weightUnit = WeightUnit.ounce;
		this.weight = ounce;
	}

	public BigDecimal getPound() {
		return multiply(getGram(), CTE_POUND);
	}

	public void setPound(BigDecimal pound) {
		this.weightUnit = WeightUnit.pound;
		this.weight = pound;
	}

	public BigDecimal getStone() {
		return div(getGram(), CTE_STONE);
	}

	public void setStone(BigDecimal stone) {
		this.weightUnit = WeightUnit.stone;
		this.weight = stone;
	}
}
