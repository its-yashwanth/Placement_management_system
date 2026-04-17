package com.pms.controller;

import com.pms.dto.CompanyRegistrationForm;
import com.pms.dto.StudentRegistrationForm;
import com.pms.model.enums.Role;
import com.pms.service.abstraction.AuthServicePort;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthServicePort authService;

    public AuthController(AuthServicePort authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register/student")
    public String studentRegistration(Model model) {
        model.addAttribute("studentForm", new StudentRegistrationForm());
        return "auth/register-student";
    }

    @PostMapping("/register/student")
    public String registerStudent(@Valid StudentRegistrationForm studentForm,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "auth/register-student";
        }

        try {
            authService.registerStudent(studentForm);
            return "redirect:/login?registered";
        } catch (IllegalStateException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register-student";
        }
    }

    @GetMapping("/register/company")
    public String companyRegistration(Model model) {
        model.addAttribute("companyForm", new CompanyRegistrationForm());
        return "auth/register-company";
    }

    @PostMapping("/register/company")
    public String registerCompany(@Valid CompanyRegistrationForm companyForm,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "auth/register-company";
        }

        try {
            authService.registerCompany(companyForm);
            return "redirect:/login?registered";
        } catch (IllegalStateException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register-company";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.STUDENT.name()));
        boolean isCompany = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.COMPANY.name()));
        boolean isOfficer = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.PLACEMENT_OFFICER.name()));

        if (isStudent) {
            return "redirect:/student/dashboard";
        }
        if (isCompany) {
            return "redirect:/company/dashboard";
        }
        if (isOfficer) {
            return "redirect:/officer/dashboard";
        }
        return "redirect:/admin/dashboard";
    }
}
