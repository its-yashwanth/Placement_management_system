package com.pms.service;

import com.pms.model.Company;
import com.pms.model.Student;
import com.pms.model.UserAccount;
import com.pms.model.enums.PlacementStatus;
import com.pms.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public Student createStudent(String email, String password, String fullName, String rollNo,
                                 String branch, Double cgpa, String skills, String resumeUrl) {
        UserAccount account = new UserAccount();
        account.setEmail(email);
        account.setPassword(password);
        account.setRole(Role.STUDENT);

        Student student = new Student();
        student.setFullName(fullName);
        student.setUniversityRollNo(rollNo);
        student.setBranch(branch);
        student.setCgpa(cgpa);
        student.setSkills(skills);
        student.setResumeUrl(resumeUrl);
        student.setPlacementStatus(PlacementStatus.NOT_PLACED);
        student.setUserAccount(account);
        return student;
    }

    public Company createCompany(String email, String password, String companyName, String website) {
        UserAccount account = new UserAccount();
        account.setEmail(email);
        account.setPassword(password);
        account.setRole(Role.COMPANY);

        Company company = new Company();
        company.setEmail(email);
        company.setCompanyName(companyName);
        company.setWebsite(website);
        company.setApproved(false);
        company.setUserAccount(account);
        return company;
    }

    public UserAccount createStaffAccount(String email, String password, Role role) {
        UserAccount account = new UserAccount();
        account.setEmail(email);
        account.setPassword(password);
        account.setRole(role);
        return account;
    }
}
