package com.mfrf.dawdletodo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PriorityModifiers {
    public static int SimpleDeadlinePriorityModifier(int initial, LocalDate begin, LocalDate end, LocalDate current){
    if (current.isAfter(end)) {
        return Integer.MAX_VALUE;
    }
    long totalDays = ChronoUnit.DAYS.between(begin, end);
    long remainingDays = ChronoUnit.DAYS.between(current, end);
    double priority = initial * (1.0 - ((double)remainingDays / totalDays));
    return (int)Math.round(priority);
    }
    public static int SimpleCompleteTimeModifier(int initial, int totalTimes, int completedTimes) {
    if (completedTimes >= totalTimes) {
        return initial;
    }

    double priority = initial * (1.0 - ((double)completedTimes / totalTimes));

    return (int)Math.round(priority);
}
}
