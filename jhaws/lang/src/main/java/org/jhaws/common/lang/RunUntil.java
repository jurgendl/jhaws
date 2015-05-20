package org.jhaws.common.lang;

import java.util.concurrent.atomic.AtomicReference;

@FunctionalInterface
public interface RunUntil extends Runnable {
	public abstract long infinite(AtomicReference<Boolean> running);

	@Override
	default public void run() {
		AtomicReference<Boolean> running = new AtomicReference<>(Boolean.TRUE);
		do {
			long wait;
			if ((wait = infinite(running)) > 0l) {
				try {
					Thread.sleep(wait);
				} catch (InterruptedException ex) {
					//
				}
			}
		} while (running.get());
	}
}