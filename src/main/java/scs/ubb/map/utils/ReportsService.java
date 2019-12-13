package scs.ubb.map.utils;

import scs.ubb.map.domain.*;
import scs.ubb.map.services.service.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReportsService {
    private Service<Long, Student> studentService;
    private Service<Integer, Homework> homeworkService;
    private Service<String, Grade> gradeService;

    public ReportsService(Service<Long, Student> studentService,
                          Service<Integer, Homework> homeworkService,
                          Service<String, Grade> gradeService) {
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        this.gradeService = gradeService;
    }

    public List<Student> getStudentFromGroup(int targetGroup) {
        return StreamSupport.stream(studentService.findAll().spliterator(), false)
                .filter(student -> student.getGroup() == targetGroup)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsWithHomework(int homeworkId) {
        return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .filter(grade -> grade.getHomeworkId() == homeworkId)
                .map(grade -> studentService.findOne(grade.getStudentId()))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsWithHomeworkToATeacher(int homeworkId, String teacherName) {
        return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .filter(grade -> grade.getHomeworkId() == homeworkId && grade.getTeacher().equals(teacherName))
                .map(grade -> studentService.findOne(grade.getStudentId()))
                .collect(Collectors.toList());
    }

    public List<GradeFilterDTO> getGradesFromAHomeworkInAWeek(int homeworkId, int week) {
        return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .filter(grade -> grade.getHomeworkId() == homeworkId &&
                        AcademicYear.getInstance().getSemesterWeek(grade.getDate()) == week)
                .map(grade -> {
                    Student student = studentService.findOne(grade.getStudentId());
                    return new GradeFilterDTO(student.getFirstName(),
                            student.getLastName(),
                            grade.getGrade(),
                            grade.getDate(),
                            grade.getHomeworkId(),
                            AcademicYear.getInstance().getSemesterWeek(grade.getDate()));
                })
                .collect(Collectors.toList());
    }

    public List<StudentLabDTO> getLabGrades() {
        return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Grade::getStudentId,
                        Collectors.reducing(0F, Grade::getGrade, Float::sum)))
                .entrySet()
                .stream()
                .map(id -> new StudentLabDTO(studentService.findOne(id.getKey()), id.getValue() / 10))
                .collect(Collectors.toList());
    }

    public Homework getHardestHomework() {
         return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Grade::getHomeworkId,
                        Collectors.averagingDouble(Grade::getGrade)
                )).entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .map(x -> homeworkService.findOne(x.getKey()))
                .findFirst().get();
    }

    public List<Student> getStudentsWhichCanTakeTheExam() {
        return  StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Grade::getStudentId,
                        Collectors.reducing(0F, Grade::getGrade, Float::sum)))
                .entrySet()
                .stream()
                .filter(x -> x.getValue() / 10 >= 5 )
                .map(x -> studentService.findOne(x.getKey()))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsWithMaxGrade() {
        return StreamSupport.stream(gradeService.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Grade::getStudentId,
                        Collectors.reducing(true, x -> x.getGrade() == 10, Boolean::logicalAnd)))
                .entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(x -> studentService.findOne(x.getKey()))
                .collect(Collectors.toList());
    }
}
