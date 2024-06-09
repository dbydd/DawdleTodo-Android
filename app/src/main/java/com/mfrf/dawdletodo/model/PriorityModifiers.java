package com.mfrf.dawdletodo.model;

import com.mfrf.dawdletodo.utils.TypeConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PriorityModifiers {
    public static int SimpleDeadlinePriorityModifier(int initial, Date begin_before, Date end_before, LocalDate current) {
        LocalDate end = TypeConverter.date2LocaleDate(end_before);
        LocalDate begin = TypeConverter.date2LocaleDate(begin_before);

        if (current.isAfter(end)) {
            return Integer.MAX_VALUE;
        }
        long totalDays = ChronoUnit.DAYS.between(begin, end);
        long remainingDays = ChronoUnit.DAYS.between(current, end);
        double priority = initial * (1.0 - ((double) remainingDays / totalDays));
        return (int) Math.round(priority);
    }

    public static int SimpleCompleteTimeModifier(int initial, int totalTimes, int completedTimes) {
        if (completedTimes >= totalTimes) {
            return initial;
        }

        double priority = initial * (1.0 - ((double) completedTimes / totalTimes));

        return (int) Math.round(priority);
    }
}
