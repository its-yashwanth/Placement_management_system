package com.pms.service.abstraction;

import com.pms.dto.CompanyRegistrationForm;
import com.pms.dto.StudentRegistrationForm;
import com.pms.model.Company;
import com.pms.model.Student;
import com.pms.model.UserAccount;
import com.pms.model.enums.Role;

public interface AuthServicePort {
    Student registerStudent(StudentRegistrationForm form);
    Company registerCompany(CompanyRegistrationForm form);
    UserAccount registerStaff(String email, String rawPassword, Role role);
}
