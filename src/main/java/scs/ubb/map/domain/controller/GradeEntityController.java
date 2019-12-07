package scs.ubb.map.domain.controller;


public class GradeEntityController {
    private String gradeId;
    private String fullName;
    private String grade;
    private String homework;
    private String date;
    private String deadline;
    private String teacher;

    public GradeEntityController(String gradeId, String fullName, String grade, String homework, String date, String deadline, String teacher) {
        this.gradeId = gradeId;
        this.fullName = fullName;
        this.grade = grade;
        this.homework = homework;
        this.date = date;
        this.deadline = deadline;
        this.teacher = teacher;
    }

    public String getGradeId() {
        return gradeId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGrade() {
        return grade;
    }

    public String getHomework() {
        return homework;
    }

    public String getDate() {
        return date;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getTeacher() {
        return teacher;
    }
}
