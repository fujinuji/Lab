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

    public Grade getGradeWithTeacherFault(Grade studentGrade) {
        float finalGrade = studentGrade.getGrade();

        int weekDifference = AcademicYear.getInstance().getCurrentWeek() - AcademicYear.getInstance().getSemesterWeek(studentGrade.getDate());
        if (finalGrade + weekDifference > 10)
            studentGrade.setGrade(10L);
        else
            studentGrade.setGrade(finalGrade + weekDifference);

        return studentGrade;
    }

    public Grade getGradeWithConstraints(Grade studentGrade, Map<String, Object> constraints) {
        float finalGrade = studentGrade.getGrade();
        int weekDifference = AcademicYear.getInstance()
                .getSemesterWeek(studentGrade.getDate()) - homeworkService.findOne(studentGrade.getHomeworkId()).getDeadlineWeek();

        if (constraints.containsKey("teacherFault")) {
            int teacherDifference = AcademicYear.getInstance().getCurrentWeek() -
                    AcademicYear.getInstance().getSemesterWeek(studentGrade.getDate());
            finalGrade += teacherDifference;
            if (weekDifference - teacherDifference < 3)
                studentGrade.setGrade(finalGrade);
        }

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
}
