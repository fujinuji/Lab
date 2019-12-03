package scs.ubb.map.domain.controller;

public class StudentControllerEntity {
    private String id;
    private String firstName;
    private String lastName;

    public StudentControllerEntity(String id, String firstName, String lastName, String group, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGroup() {
        return group;
    }

    public String getEmail() {
        return email;
    }

    private String group;
    private String email;
}
