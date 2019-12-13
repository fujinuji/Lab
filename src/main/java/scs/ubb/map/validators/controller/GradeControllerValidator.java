package scs.ubb.map.validators.controller;

import scs.ubb.map.domain.Grade;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.domain.Student;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeControllerValidator {
    public void studentAlreadyHasGrade(GradeService service, Student student, Homework homework) {
        String id = student.getId() + "_" + homework.getId();
        List<Grade> gradeList = new ArrayList<>();
        service.findAll().forEach(gradeList::add);
        if(!gradeList.stream().filter(grade -> grade.getId().equals(id)).collect(Collectors.toList()).isEmpty()) {
            throw new ValidationException("Studentul deja are nota la aceasta tema");
        }
    }


}
