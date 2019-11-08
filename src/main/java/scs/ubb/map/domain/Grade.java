package scs.ubb.map.domain;

import java.time.LocalDate;

public class Grade extends Entity<String> {
    private String teacher;
    private LocalDate date;
    private float grade;

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Grade(String teacher, float grade, LocalDate date) {
        this.teacher = teacher;
        this.grade = grade;
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public LocalDate getDate() {
        return date;
    }
}
