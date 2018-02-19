package org.jhaws.common.jaxb.adapters;

public class LongBiArrayAdapter extends BiArrayAdapter<Long> {
    @Override
    Long[] single(int i) {
        return new Long[i];
    }

    @Override
    Long[][] bi(int i) {
        return new Long[i][i];
    }

    @Override
    Long parse(String v) {
        return Long.parseLong(v);
    }
}