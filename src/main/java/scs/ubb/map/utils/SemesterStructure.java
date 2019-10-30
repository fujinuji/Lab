package scs.ubb.map.utils;

import java.time.LocalDate;
import java.util.Calendar;

public class SemesterStructure {
    private LocalDate startDate;
    private LocalDate endDate;

    public static int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();

        System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));

        return  1;
    }

    public static void main(String[] args) {
        int a = getCurrentWeek();
    }
}
