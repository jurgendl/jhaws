package org.jhaws.common.lang;

public class Division<N extends Number> extends Number {
    private static final long serialVersionUID = 5510050339505557228L;

    public static <N1 extends Number, N2 extends Number> Division<? extends Number> div(N1 a, N2 b) {
        return new Division<>(a, b);
    }

    public static <N1 extends Number, N2 extends Number> Division<? extends Number> div(N2 b) {
        return new Division<>(1, b);
    }

    protected N dividend;

    protected N divisor;

    public Division(N dividend, N divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
    }

    public Division() {
        super();
    }

    public N getDividend() {
        return this.dividend;
    }

    public void setDividend(N dividend) {
        this.dividend = dividend;
    }

    public N getDivisor() {
        return this.divisor;
    }

    public void setDivisor(N divisor) {
        this.divisor = divisor;
    }

    @Override
    public String toString() {
        return dividend + "/" + divisor;
    }

    public double quotient() {
        return dividend.doubleValue() / divisor.doubleValue();
    }

    @Override
    public int intValue() {
        return Double.valueOf(quotient()).intValue();
    }

    @Override
    public long longValue() {
        return Double.valueOf(quotient()).longValue();
    }

    @Override
    public float floatValue() {
        return Double.valueOf(quotient()).floatValue();
    }

    @Override
    public double doubleValue() {
        return quotient();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.dividend == null) ? 0 : this.dividend.hashCode());
        result = prime * result + ((this.divisor == null) ? 0 : this.divisor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Division<?> other = (Division<?>) obj;
        if (this.dividend == null) {
            if (other.dividend != null) return false;
        } else if (!this.dividend.equals(other.dividend)) return false;
        if (this.divisor == null) {
            if (other.divisor != null) return false;
        } else if (!this.divisor.equals(other.divisor)) return false;
        return true;
    }
}
