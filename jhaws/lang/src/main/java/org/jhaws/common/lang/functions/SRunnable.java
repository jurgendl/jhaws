package org.jhaws.common.lang.functions;

import java.io.Serializable;

@FunctionalInterface
public interface SRunnable extends Runnable, Serializable {
	public static Runnable enhance(SRunnable runnable) {
		return runnable::run;
	}
}
