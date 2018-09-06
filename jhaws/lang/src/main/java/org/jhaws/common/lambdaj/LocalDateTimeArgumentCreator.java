package org.jhaws.common.lambdaj;

import org.joda.time.LocalDateTime;

import ch.lambdaj.function.argument.FinalClassArgumentCreator;

public class LocalDateTimeArgumentCreator implements FinalClassArgumentCreator<LocalDateTime> {
    @Override
    public LocalDateTime createArgumentPlaceHolder(int seed) {
        return new LocalDateTime(seed);
    }
}