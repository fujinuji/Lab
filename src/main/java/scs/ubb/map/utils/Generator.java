package scs.ubb.map.utils;

import scs.ubb.map.domain.Student;

import java.util.List;

public class Generator {
    public static Long generateStudentId(List<Student> students) {
        return students.get(students.size() - 1).getId() + 1;
    }
}
