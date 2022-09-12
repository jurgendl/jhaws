package org.jhaws.common.net.reastclient;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RestExceptionInfo {
    protected boolean exception = true;
    protected String[] stacktrace;
    protected String key;
    protected String[] arguments;

    public RestExceptionInfo() {
        super();
    }

    public RestExceptionInfo(RestException rex) {
        this(rex.getMessage(), rex.getArguments() == null || rex.getArguments().length == 0 ? null : Arrays.stream(rex.getArguments()).map(String::valueOf).toArray(l -> new String[l]));
    }

    public RestExceptionInfo(String key, String... arguments) {
        this();
        this.key = key;
        this.arguments = arguments;
    }

    public RestExceptionInfo(String[] stacktrace) {
        this();
        this.stacktrace = stacktrace;
    }

    public RestException toRestException() {
        return Optional.ofNullable(getKey()).map(keyNotNull -> new RestException(keyNotNull, getArguments() == null || getArguments().length == 0 ? null : Arrays.stream(getArguments()).map(Object.class::cast).toArray(l -> new Object[l]))).orElseGet(() -> {
            if (getStacktrace() == null || getStacktrace().length == 0) {
                return new RestException("unknown error");
            }
            return new RestException(getStacktrace()[0]);
        });
    }

    public boolean getException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public String[] getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String[] stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }
}
