package scs.ubb.map.utils;

import java.time.LocalDate;
import java.util.Calendar;

public class SemesterStructure {
    private static LocalDate startDate;
    private static LocalDate endDate;

    private SemesterStructure instance;

    public SemesterStructure(LocalDate startDate, LocalDate endDate) {
        SemesterStructure.startDate = startDate;
        SemesterStructure.endDate = endDate;
    }

    public SemesterStructure getInstance() {
        instance = instance == null ? new SemesterStructure(null, null) : instance;
        return instance;
    }

    public static int getCurrentWeek() {
        Calendar nowCalendar = Calendar.getInstance();
        Calendar beginningCalendar = Calendar.getInstance();

        beginningCalendar.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());

        return nowCalendar.get(Calendar.WEEK_OF_YEAR) - beginningCalendar.get(Calendar.WEEK_OF_YEAR) + 1;
    }
}
