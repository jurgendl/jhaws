package org.jhaws.common.math;

public interface MathUtils {
	public static int percent(double d) {
		return (int) (100 * d);
	}

	public static double round(int t, int n, int decimals) {
		double exp = Math.pow(10, decimals);
		return Math.round(exp * t / n) / exp;
	}

	public static double round(double d, int decimals) {
		double exp = Math.pow(10, decimals);
		return Math.round(exp * d) / exp;
	}

	public static int floor(int t, int n) {
		return t / n;
	}

	public static int ceiling(int t, int n) {
		return t / n + (t % n > 0 ? 1 : 0);
	}
}
