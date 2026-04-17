package com.pms.config;

import com.pms.dto.DriveForm;
import com.pms.dto.StudentRegistrationForm;
import com.pms.model.Company;
import com.pms.model.enums.Role;
import com.pms.service.abstraction.AuthServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(StudentServicePort studentService,
                               CompanyServicePort companyService,
                               PlacementDriveServicePort placementDriveService,
                               AuthServicePort authService) {
        return args -> {
            authService.registerStaff("admin@pms.edu", "admin123", Role.ADMIN);
            authService.registerStaff("officer@pms.edu", "officer123", Role.PLACEMENT_OFFICER);

            if (studentService.getAllStudents().isEmpty()) {
                StudentRegistrationForm studentForm = new StudentRegistrationForm();
                studentForm.setEmail("ananya@student.edu");
                studentForm.setPassword("student123");
                studentForm.setFullName("Ananya Sharma");
                studentForm.setUniversityRollNo("2022CS101");
                studentForm.setBranch("CSE");
                studentForm.setCgpa(8.7);
                studentForm.setSkills("Java, SQL, Spring Boot");
                studentForm.setResumeUrl("resume-ananya.pdf");
                authService.registerStudent(studentForm);
            }

            if (companyService.getAllCompanies().isEmpty()) {
                var companyForm = new com.pms.dto.CompanyRegistrationForm();
                companyForm.setEmail("hr@techverse.com");
                companyForm.setPassword("company123");
                companyForm.setCompanyName("TechVerse");
                companyForm.setWebsite("https://techverse.example");
                Company savedCompany = authService.registerCompany(companyForm);
                savedCompany.setApproved(true);
                companyService.save(savedCompany);

                DriveForm form = new DriveForm();
                form.setTitle("Software Engineer Trainee");
                form.setDescription("Campus hiring drive for final year students.");
                form.setEligibleBranch("CSE");
                form.setMinimumCgpa(7.0);
                form.setLocation("Bengaluru");
                form.setDriveDate(LocalDate.now().plusDays(7));
                placementDriveService.createDrive(savedCompany.getId(), form);
            }
        };
    }
}
