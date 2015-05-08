package org.jhaws.common.lang;

import java.util.function.Function;

public class Pair<P> extends KeyValue<P, P> {
	private static final long serialVersionUID = 2133551453748910250L;

	public Pair() {
		super();
	}

	public Pair(P key, P value) {
		super(key, value);
	}

	public <I> boolean isEquals(Function<P, I> pf) {
		return isEquals(pf, pf);
	}

	public <I> boolean notEquals(Function<P, I> pf) {
		return !isEquals(pf);
	}
}