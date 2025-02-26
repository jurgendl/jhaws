package org.jhaws.common.net.restclient;

import java.util.Arrays;

@SuppressWarnings("serial")
public class RestException extends RuntimeException {
	public static final int REST_API_SERVER_ERROR_STATUSCODE = 591;

	private Object[] arguments;

	public RestException(String key, Object... arguments) {
		super(key);
		this.arguments = arguments;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + (arguments == null || arguments.length == 0 ? "" : " " + Arrays.asList(arguments));
	}
}
