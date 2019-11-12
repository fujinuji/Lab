package scs.ubb.map.handlers;

import scs.ubb.map.domain.Grade;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.utils.AcademicYear;

import java.util.Map;

public class GradeHandler {
    private HomeworkService homeworkService;
    public GradeHandler(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    public Grade getGradeWithConstraints(Grade studentGrade, Map<String, Object> constraints) {
        float finalGrade = studentGrade.getGrade();
        int weekDifference = AcademicYear.getInstance()
                .getSemesterWeek(studentGrade.getDate()) - homeworkService.findOne(studentGrade.getHomeworkId()).getDeadlineWeek();

        if (weekDifference > 0) {
            if (!constraints.containsKey("motivated_absence")) {
                if (weekDifference > 2) {
                    studentGrade.setGrade(1);
                    return studentGrade;
                }
            } else {
                int weeksOfAbsence = (int) constraints.get("motivated_absence");
                studentGrade.setGrade(finalGrade - (weekDifference - weeksOfAbsence));
                return studentGrade;
            }

            studentGrade.setGrade(finalGrade - weekDifference);
            return studentGrade;
        }

        return studentGrade;
    }

   /* public static void main(String[] args) {
        AcademicYear academicYear = new AcademicYear(ApplicationContext.getPROPERTIES().getProperty("year-data"));
        Grade grade = new Grade("", 10, LocalDate.of(2019, 11, 1));
        GradeHandler gradeHandler = new GradeHandler();
        Map<String, Object> map = new HashMap<>();
        map.put("motivated_absence", 0);
        System.out.println(gradeHandler.getGradeWithConstraints(grade, map).getGrade());
    }*/
}
