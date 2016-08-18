package org.jhaws.common.lang;

@FunctionalInterface
public interface RunIndefinitely extends Runnable {
	public abstract long infinite();

	@Override
	default public void run() {
		long wait;
		do {
			if ((wait = infinite()) > 0l) {
				try {
					Thread.sleep(wait);
				} catch (InterruptedException ex) {
					//
				}
			}
		} while (true && wait >= 0);
	}
}