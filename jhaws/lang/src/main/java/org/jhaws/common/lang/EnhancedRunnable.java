package org.jhaws.common.lang;

@FunctionalInterface
public interface EnhancedRunnable extends Runnable {
	@Override
	default void run() {
		try {
			runEnhanced();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void runEnhanced() throws Exception;
}