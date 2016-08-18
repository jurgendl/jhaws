package org.tools.hqlbuilder.webservice.wicket.converter;

import java.util.Date;

import org.joda.time.LocalDate;

public class LocalDateToDateConverter implements Converter<LocalDate, Date> {
    private static final long serialVersionUID = -6637566257324415974L;

    @Override
    public Date toType(LocalDate object) {
        return object.toDate();
    }

    @Override
    public LocalDate fromType(Date object) {
        return LocalDate.fromDateFields(object);
    }
}
