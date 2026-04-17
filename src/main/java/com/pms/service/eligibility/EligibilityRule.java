package com.pms.service.eligibility;

import com.pms.model.PlacementDrive;
import com.pms.model.Student;

public interface EligibilityRule {
    boolean isSatisfiedBy(Student student, PlacementDrive drive);
    String getRuleName();
}
