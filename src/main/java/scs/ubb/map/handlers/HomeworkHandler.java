package scs.ubb.map.handlers;

import scs.ubb.map.domain.Homework;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.utils.AcademicYear;

import java.util.ArrayList;
import java.util.List;

public class HomeworkHandler {
    public static Homework getCurrentHomework(HomeworkService homeworkService) {
        int currentWeek = AcademicYear.getInstance().getCurrentWeek();
        List<Homework> homework = new ArrayList<>();
        homeworkService.findAll().forEach(homework::add);
        return homework.stream().filter(x->x.getDeadlineWeek() == currentWeek).findFirst().get();
    }
}
