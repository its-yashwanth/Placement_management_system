package com.pms.service;

import com.pms.dto.CompanyRegistrationForm;
import com.pms.dto.StudentRegistrationForm;
import com.pms.model.Company;
import com.pms.model.Student;
import com.pms.model.UserAccount;
import com.pms.model.enums.Role;
import com.pms.repository.UserAccountRepository;
import com.pms.service.abstraction.AuthServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.StudentServicePort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServicePort {

    private final UserAccountRepository userAccountRepository;
    private final StudentServicePort studentService;
    private final CompanyServicePort companyService;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userAccountRepository,
                       StudentServicePort studentService,
                       CompanyServicePort companyService,
                       UserFactory userFactory,
                       PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.studentService = studentService;
        this.companyService = companyService;
        this.userFactory = userFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student registerStudent(StudentRegistrationForm form) {
        validateUniqueEmail(form.getEmail());
        Student student = userFactory.createStudent(
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                form.getFullName(),
                form.getUniversityRollNo(),
                form.getBranch(),
                form.getCgpa(),
                form.getSkills(),
                form.getResumeUrl()
        );
        return studentService.save(student);
    }

    @Override
    public Company registerCompany(CompanyRegistrationForm form) {
        validateUniqueEmail(form.getEmail());
        Company company = userFactory.createCompany(
                form.getEmail(),
                passwordEncoder.encode(form.getPassword()),
                form.getCompanyName(),
                form.getWebsite()
        );
        return companyService.save(company);
    }

    @Override
    public UserAccount registerStaff(String email, String rawPassword, Role role) {
        if (userAccountRepository.existsByEmail(email)) {
            return userAccountRepository.findByEmail(email).orElseThrow();
        }
        UserAccount account = userFactory.createStaffAccount(email, passwordEncoder.encode(rawPassword), role);
        return userAccountRepository.save(account);
    }

    private void validateUniqueEmail(String email) {
        if (userAccountRepository.existsByEmail(email)) {
            throw new IllegalStateException("An account with this email already exists.");
        }
    }
}
