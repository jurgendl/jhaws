package org.jhaws.common.net.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiRequestListener implements RequestListener {
	protected List<RequestListener> listeners;

	public MultiRequestListener() {
		this.listeners = new ArrayList<>();
	}

	public MultiRequestListener(RequestListener... listeners) {
		this.listeners = Arrays.asList(listeners);
	}

	@Override
	public void bytesWritten(long written, long total) {
		listeners.forEach(listener -> listener.bytesWritten(written, total));
	}

	@Override
	public void start(URI uri, long contentLength) {
		listeners.forEach(listener -> listener.start(uri, contentLength));
	}

	@Override
	public void end() {
		listeners.forEach(listener -> listener.end());
	}

	public List<RequestListener> getListeners() {
		return this.listeners;
	}

	public void setListeners(List<RequestListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void write(byte[] b, int off, int len) {
		listeners.forEach(listener -> listener.write(b, off, len));
	}
}
