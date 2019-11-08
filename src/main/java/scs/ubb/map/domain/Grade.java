package scs.ubb.map.domain;

import java.util.Date;

public class Grade extends Entity<String> {
    private String teacher;
    private Date date;

    public Grade(String teacher, Date date) {
        this.teacher = teacher;
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public Date getDate() {
        return date;
    }
}
