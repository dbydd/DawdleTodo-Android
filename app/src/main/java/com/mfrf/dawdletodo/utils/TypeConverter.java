package com.mfrf.dawdletodo.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TypeConverter {
    public static Date localeDate2Date(LocalDate date) {
        //copy from stackoverflow
        return java.util.Date.from(                     // Convert from modern java.time class to troublesome old legacy class.  DO NOT DO THIS unless you must, to inter operate with old code not yet updated for java.time.
                date                          // `LocalDate` class represents a date-only, without time-of-day and without time zone nor offset-from-UTC.
                        .atStartOfDay(                       // Let java.time determine the first moment of the day on that date in that zone. Never assume the day starts at 00:00:00.
                                ZoneId.of("Asia/Shanghai")  // Specify time zone using proper name in `continent/region` format, never 3-4 letter pseudo-zones such as “PST”, “CST”, “IST”.
                        )                                    // Produce a `ZonedDateTime` object.
                        .toInstant()                         // Extract an `Instant` object, a moment always in UTC.
        );
    }

    public static LocalDate date2LocaleDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.of("Asia/Shanghai")); //same ZoneId ensure same behavior
    }
}
