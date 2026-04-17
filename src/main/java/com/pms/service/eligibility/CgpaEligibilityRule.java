package com.pms.service.eligibility;

import com.pms.model.PlacementDrive;
import com.pms.model.Student;
import org.springframework.stereotype.Component;

@Component
public class CgpaEligibilityRule implements EligibilityRule {

    @Override
    public boolean isSatisfiedBy(Student student, PlacementDrive drive) {
        return student.getCgpa() >= drive.getMinimumCgpa();
    }

    @Override
    public String getRuleName() {
        return "CGPA Eligibility";
    }
}
