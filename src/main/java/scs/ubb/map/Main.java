package scs.ubb.map;

import scs.ubb.map.services.config.Config;
import scs.ubb.map.utils.AcademicYear;
import scs.ubb.map.utils.SemesterStructure;

import java.time.LocalDate;
import java.time.Month;

public class Main {
    public static void main(String[] args) {
        SemesterStructure semesterStructure1 = new SemesterStructure(LocalDate.of(2019, Month.SEPTEMBER, 30),
                LocalDate.of(2020, Month.JANUARY, 17),
                LocalDate.of(2019, Month.DECEMBER, 21),
                LocalDate.of(2020, Month.JANUARY, 5));

        SemesterStructure semesterStructure2 = new SemesterStructure(LocalDate.of(2020, Month.FEBRUARY, 24),
                LocalDate.of(2020, Month.MAY, 17),
                LocalDate.of(2020, Month.APRIL, 20),
                LocalDate.of(2020, Month.APRIL, 26));

        AcademicYear academicYear = new AcademicYear(Config.getProperties().getProperty("year-data"));
        System.out.println(academicYear.getSemesterWeek(LocalDate.now()));
//
//        System.out.println(semesterStructure1.getSemesterWeek(LocalDate.of(2020, Month.JANUARY, 1)));
//        System.out.println(semesterStructure2.getSemesterWeek(LocalDate.of(2020, Month.APRIL, 27)));
    }
}
