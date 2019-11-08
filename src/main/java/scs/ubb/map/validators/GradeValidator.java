package scs.ubb.map.validators;

import scs.ubb.map.domain.Grade;
import scs.ubb.map.repository.CrudRepository;

public class GradeValidator implements Validator<Grade> {
    private CrudRepository gradeRepo;

    public GradeValidator(CrudRepository gradeRepo) {
        this.gradeRepo = gradeRepo;
    }
    @Override
    public void validate(Grade entity) throws ValidationException {
        String errors = "";
        if(entity.getGrade() > 10 || entity.getGrade() < 1) {
            errors += "Grade should be between 1 and 10\n";
        }

        if(gradeRepo.findOne(entity.getId()) != null) {
            errors += "This student already has a grade for this homework";
        }

        if(!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}
