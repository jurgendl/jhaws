package org.jhaws.common.net.client;

import java.io.OutputStream;

public class RequestOutputStreamWrapper extends org.apache.commons.compress.utils.CountingOutputStream {
	protected RequestListener requestListener;

	public RequestOutputStreamWrapper(OutputStream out, RequestListener requestListener) {
		super(out);
		this.requestListener = requestListener;
	}

	@Override
	protected void count(final long written) {
		super.count(written);
		if (written != -1) {
			requestListener.bytesWritten(written, getBytesWritten());
		}
	}
}
