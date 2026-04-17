package com.pms.service.eligibility;

import com.pms.model.PlacementDrive;
import com.pms.model.Student;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EligibilityEvaluator {

    private final List<EligibilityRule> rules;

    public EligibilityEvaluator(List<EligibilityRule> rules) {
        this.rules = rules;
    }

    public boolean isEligible(Student student, PlacementDrive drive) {
        return rules.stream().allMatch(rule -> rule.isSatisfiedBy(student, drive));
    }
}
