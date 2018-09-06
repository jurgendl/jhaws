package org.jhaws.common.lambdaj;

import org.joda.time.LocalDate;

import ch.lambdaj.function.argument.FinalClassArgumentCreator;

public class LocalDateArgumentCreator implements FinalClassArgumentCreator<LocalDate> {
    private final long MSECS_IN_DAY = 1000L * 60L * 60L * 24L;

    public LocalDateArgumentCreator() {
        return;
    }

    @Override
    public LocalDate createArgumentPlaceHolder(int seed) {
        return new LocalDate(seed * MSECS_IN_DAY);
    }
}