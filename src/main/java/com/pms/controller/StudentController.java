/**
 * Handles all student operations:
 * - Dashboard viewing
 * - Applying to placement drives
 * - Updating student profile
 */

package com.pms.controller;

import com.pms.dto.StudentProfileForm;
import com.pms.service.abstraction.ApplicationServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentServicePort studentService;
    private final PlacementDriveServicePort placementDriveService;
    private final ApplicationServicePort applicationService;

    public StudentController(StudentServicePort studentService,
                             PlacementDriveServicePort placementDriveService,
                             ApplicationServicePort applicationService) {
        this.studentService = studentService;
        this.placementDriveService = placementDriveService;
        this.applicationService = applicationService;
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(Authentication authentication, Model model) {
        var student = studentService.getByEmail(authentication.getName());
        StudentProfileForm profileForm = new StudentProfileForm();
        profileForm.setFullName(student.getFullName());
        profileForm.setBranch(student.getBranch());
        profileForm.setCgpa(student.getCgpa());
        profileForm.setSkills(student.getSkills());
        profileForm.setResumeUrl(student.getResumeUrl());

        model.addAttribute("student", student);
        model.addAttribute("drives", placementDriveService.getOpenDrives().stream()
                .filter(drive -> drive.getCompany().isApproved())
                .toList());
        model.addAttribute("applications", applicationService.getApplicationsForStudent(student.getId()));
        model.addAttribute("profileForm", profileForm);
        return "student/dashboard";
    }

    @PostMapping("/student/apply/{driveId}")
    public String apply(Authentication authentication,
                        @PathVariable Long driveId,
                        RedirectAttributes redirectAttributes) {
        Long studentId = studentService.getByEmail(authentication.getName()).getId();
        try {
            applicationService.applyForDrive(studentId, driveId);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/student/dashboard";
    }

    @PostMapping("/student/profile")
    public String updateProfile(Authentication authentication,
                                @Valid StudentProfileForm profileForm,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the profile form values.");
            return "redirect:/student/dashboard";
        }

        Long studentId = studentService.getByEmail(authentication.getName()).getId();
        studentService.updateProfile(studentId, profileForm);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        return "redirect:/student/dashboard";
    }
}
