package org.jhaws.common.net.client;

import java.net.URI;

import org.jhaws.common.io.FilePath;

public class PrintRequestListener implements RequestListener {
	long start;
	long time;
	long total;
	URI link;
	boolean log;

	@Override
	public void start(URI uri, long contentLength) {
		link = uri;
		start = System.currentTimeMillis();
		total = contentLength;
		time = start;
	}

	@Override
	public void end() {
		if (log && total > 0) {
			long currentTimeMillis = System.currentTimeMillis();
			long s = (currentTimeMillis - start) / 1000;
			long speed = (long) ((double) total / s);
			System.out.println(""//
					+ String.format("%6s", "100") + "%"//
					+ "   "//
					+ String.format("%11s", "")//
					+ "   "//
					+ String.format("%11s", FilePath.getHumanReadableFileSize(total, 3))//
					+ "   "//
					+ String.format("%3s", "") //
					+ "   "//
					+ String.format("%3s", ((currentTimeMillis - start) / 1000)) + "s"//
					+ "   " //
					+ String.format("%9s", FilePath.getHumanReadableFileSize(speed, 1)) + "/s"//
					+ "   "//
					+ link//
			);
		}
	}

	@Override
	public void bytesWritten(long written, long current) {
		if (total > 0) {
			long currentTimeMillis = System.currentTimeMillis();
			if (log) {
				if ((currentTimeMillis - time) > 1000l) {
					long duration = currentTimeMillis - start;
					double progress = (Math.round(current * 10000.0 / total) / 100.0);
					long expected = ((long) (duration / progress * 100.0)) / 1000;
					long speed = (long) (current / (duration / 1000.0));
					System.out.println(""//
							+ String.format("%6s", progress) + "%"//
							+ "   "//
							+ String.format("%11s", FilePath.getHumanReadableFileSize(current, 3))//
							+ " / "//
							+ String.format("%11s", FilePath.getHumanReadableFileSize(total, 3))//
							+ "   "//
							+ String.format("%3s", ((currentTimeMillis - start) / 1000)) //
							+ " / "//
							+ String.format("%3s", expected) + "s"//
							+ "   " //
							+ String.format("%9s", FilePath.getHumanReadableFileSize(speed, 1)) + "/s"//
							+ "   "//
							+ link//
					);
					time = currentTimeMillis;
				}
			} else {
				long duration = currentTimeMillis - start;
				if (duration > 1000l) {
					double progress = (Math.round(current * 10000.0 / total) / 100.0);
					long expected = ((long) (duration / progress * 100.0)) / 1000;
					long speed = (long) (current / (duration / 1000.0));
					log = true;
					System.out.println(""//
							+ String.format("%6s", progress) + "%"//
							+ "   "//
							+ String.format("%11s", FilePath.getHumanReadableFileSize(current, 3))//
							+ " / "//
							+ String.format("%11s", FilePath.getHumanReadableFileSize(total, 3))//
							+ "   "//
							+ String.format("%3s", ((currentTimeMillis - start) / 1000)) //
							+ " / "//
							+ String.format("%3s", expected) + "s"//
							+ "   " //
							+ String.format("%9s", FilePath.getHumanReadableFileSize(speed, 1)) + "/s"//
							+ "   "//
							+ link//
					);
					time = currentTimeMillis;
				}
			}
		}
	}

	@Override
	public void write(byte[] b, int off, int len) {
		//
	}
}
