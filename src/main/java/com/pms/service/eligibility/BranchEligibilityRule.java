package com.pms.service.eligibility;

import com.pms.model.PlacementDrive;
import com.pms.model.Student;
import org.springframework.stereotype.Component;

@Component
public class BranchEligibilityRule implements EligibilityRule {

    @Override
    public boolean isSatisfiedBy(Student student, PlacementDrive drive) {
        return "ALL".equalsIgnoreCase(drive.getEligibleBranch())
                || drive.getEligibleBranch().equalsIgnoreCase(student.getBranch());
    }

    @Override
    public String getRuleName() {
        return "Branch Eligibility";
    }
}
