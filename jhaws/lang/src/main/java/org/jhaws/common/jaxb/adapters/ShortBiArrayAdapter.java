package org.jhaws.common.jaxb.adapters;

public class ShortBiArrayAdapter extends BiArrayAdapter<Short> {
    @Override
    Short[] single(int i) {
        return new Short[i];
    }

    @Override
    Short[][] bi(int i) {
        return new Short[i][i];
    }

    @Override
    Short parse(String v) {
        return Short.parseShort(v);
    }
}