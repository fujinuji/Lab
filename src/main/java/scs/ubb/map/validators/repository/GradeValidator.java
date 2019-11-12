package scs.ubb.map.validators.repository;

import scs.ubb.map.domain.Grade;
import scs.ubb.map.validators.ValidationException;
import scs.ubb.map.validators.Validator;

public class GradeValidator implements Validator<Grade> {
    public GradeValidator() {
    }

    @Override
    public void validate(Grade entity) throws ValidationException {
        String errors = "";
        if (entity.getGrade() > 10 || entity.getGrade() < 1) {
            errors += "Grade should be between 1 and 10\n";
        }

        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}
