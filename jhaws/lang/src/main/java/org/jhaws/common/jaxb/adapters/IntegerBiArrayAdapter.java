package org.jhaws.common.jaxb.adapters;

public class IntegerBiArrayAdapter extends BiArrayAdapter<Integer> {
	@Override
	Integer[] single(int i) {
		return new Integer[i];
	}

	@Override
	Integer[][] bi(int i) {
		return new Integer[i][i];
	}

	@Override
	Integer parse(String v) {
		return Integer.parseInt(v);
	}

}