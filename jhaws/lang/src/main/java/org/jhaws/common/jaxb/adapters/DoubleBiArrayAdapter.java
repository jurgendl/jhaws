package org.jhaws.common.jaxb.adapters;

public class DoubleBiArrayAdapter extends BiArrayAdapter<Double> {
	@Override
	Double[] single(int i) {
		return new Double[i];
	}

	@Override
	Double[][] bi(int i) {
		return new Double[i][i];
	}

	@Override
	Double parse(String v) {
		return Double.parseDouble(v);
	}
}