package org.jhaws.common.jaxb.adapters;

public class FloatBiArrayAdapter extends BiArrayAdapter<Float> {
	@Override
	Float[] single(int i) {
		return new Float[i];
	}

	@Override
	Float[][] bi(int i) {
		return new Float[i][i];
	}

	@Override
	Float parse(String v) {
		return Float.parseFloat(v);
	}
}