package org.jhaws.common.lambdaj;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import ch.lambdaj.function.argument.ArgumentsFactory;

public class LambdaJUtils {
    static {
        ArgumentsFactory.registerFinalClassArgumentCreator(LocalDate.class, new LocalDateArgumentCreator());
        ArgumentsFactory.registerFinalClassArgumentCreator(LocalDateTime.class, new LocalDateTimeArgumentCreator());
    }
}
