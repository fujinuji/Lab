package scs.ubb.map.validators.controller;

import scs.ubb.map.controllers.StudentEditController;
import scs.ubb.map.domain.controller.StudentControllerEntity;
import scs.ubb.map.utils.Constants;
import scs.ubb.map.validators.ValidationException;
import scs.ubb.map.validators.Validator;

public class StudentControllerValidator implements Validator<StudentControllerEntity> {

    @Override
    public void validate(StudentControllerEntity student) throws ValidationException {
        String errors = "";

        try {
            Integer.parseInt(student.getId());
        } catch (Exception e) {
            errors += "Id have to be a number";
        }

        if (!student.getFirstName().replaceAll(" ", "").matches(Constants.NAME_REGEX)) {
            errors += "First name should contains only letters";
        }

        if (!student.getLastName().replaceAll(" ", "").matches(Constants.NAME_REGEX)) {
            errors += "Last name should contains only letters";
        }

        try {
            Integer.parseInt(student.getGroup());
        } catch (Exception e) {
            errors += "Group have to be a number";
        }

        if (!student.getEmail().matches(Constants.EMAIL_REGEX)) {
            errors += "Invalid email format";
        }

        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }

    public void validateId(String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            throw new ValidationException("Id have to be a number");
        }
    }

    public void validateFirstName(String data) {
        if(!data.replaceAll(" ", "").matches(Constants.NAME_REGEX)) {
            throw new ValidationException("First name should contains only letters");
        }
    }

    public void validateLastName(String data) {
        if(!data.replaceAll(" ", "").matches(Constants.NAME_REGEX)) {
            throw new ValidationException("Last name should contains only letters");
        }
    }

    public void validateGroup(String data) {
        try {
            Integer.parseInt(data);
        } catch (Exception e) {
            throw new ValidationException("Group have to be a number");
        }
    }

    public void validateEmail(String data) {
        if(!data.matches(Constants.EMAIL_REGEX)) {
            throw new ValidationException("Invalid email format");
        }
    }
}
