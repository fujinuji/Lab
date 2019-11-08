package scs.ubb.map.handlers;

import scs.ubb.map.domain.Grade;
import scs.ubb.map.services.config.ApplicationContext;
import scs.ubb.map.utils.AcademicYear;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class GradeHandler {
    public Grade getGradeWithConstraints(Grade studentGrade, Map<String, Object> constraints) {
        float finalGrade = studentGrade.getGrade();
        int weekDifference = AcademicYear.getInstance().getCurrentWeek() - AcademicYear.getInstance()
                .getSemesterWeek(studentGrade.getDate());

        if(weekDifference > 0) {
            if(!constraints.containsKey("motivated_absence")) {
                if( weekDifference > 2) {
                    studentGrade.setGrade(1);
                    return studentGrade;
                }
            } else {
                int weeksOfAbsence = (int) constraints.get("motivated_absence");
                studentGrade.setGrade(finalGrade - (weekDifference - weeksOfAbsence));
                return studentGrade;
            }

            if(!constraints.containsKey("professorFault")) {

            }

            studentGrade.setGrade(finalGrade - weekDifference);
            return studentGrade;
        }

        return studentGrade;
    }

    public static void main(String[] args){
        AcademicYear academicYear = new AcademicYear(ApplicationContext.getPROPERTIES().getProperty("year-data"));
        Grade grade = new Grade("", 10, LocalDate.of(2019, 11, 1));
        GradeHandler gradeHandler = new GradeHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("motivated_absence", 0);
        System.out.println(gradeHandler.getGradeWithConstraints(grade, map).getGrade());
    }
}
