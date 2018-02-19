package org.jhaws.common.g11n;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class Helper {
    protected static final int PRECISION = 100;

    protected static final NumberFormat FORMATTER = NumberFormat.getInstance();

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
}
