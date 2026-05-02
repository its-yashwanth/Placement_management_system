package com.pms.controller;

// Importing required service interfaces (abstractions)
import com.pms.service.abstraction.AdminServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;

// Importing required Java and Spring libraries
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// Marks this class as a Spring MVC Controller
@Controller
public class AdminController {

    // Declaring service layer dependencies (using abstraction)
    private final AdminServicePort adminService;
    private final StudentServicePort studentService;
    private final CompanyServicePort companyService;
    private final PlacementDriveServicePort placementDriveService;

    // Constructor Injection for dependency injection
    public AdminController(AdminServicePort adminService,
                           StudentServicePort studentService,
                           CompanyServicePort companyService,
                           PlacementDriveServicePort placementDriveService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.companyService = companyService;
        this.placementDriveService = placementDriveService;
    }

    // Handles GET request for Admin Dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        // Add dashboard statistics (counts)
        model.addAttribute("counts", adminService.getDashboardCounts());

        // Add list of all students
        model.addAttribute("students", studentService.getAllStudents());

        // Add list of all companies
        model.addAttribute("companies", companyService.getAllCompanies());

        // Add list of companies pending approval
        model.addAttribute("pendingCompanies", companyService.getPendingCompanies());

        // Return dashboard view
        return "admin/dashboard";
    }

    // Handles GET request for Reports page
    @GetMapping("/admin/reports")
    public String adminReports(Model model) {
        // Add dashboard counts
        model.addAttribute("counts", adminService.getDashboardCounts());

        // Add application status counts (placed, pending, etc.)
        model.addAttribute("statusCounts", adminService.getApplicationStatusCounts());

        // Add list of placed student applications
        model.addAttribute("placedStudents", adminService.getPlacedStudentApplications());

        // Return reports view
        return "admin/reports";
    }

    // Handles GET request for Placement Drives page
    @GetMapping("/admin/drives")
    public String adminDrives(Model model) {
        // Add all placement drives
        model.addAttribute("drives", placementDriveService.getAllDrives());

        // Return drives view
        return "admin/drives";
    }

    // Handles POST request to delete a student by ID
    @PostMapping("/admin/students/{studentId}/delete")
    public String deleteStudent(@PathVariable Long studentId) {
        // Call service to delete student
        studentService.deleteStudent(studentId);

        // Redirect back to dashboard
        return "redirect:/admin/dashboard";
    }

    // Handles POST request to delete a company by ID
    @PostMapping("/admin/companies/{companyId}/delete")
    public String deleteCompany(@PathVariable Long companyId) {
        // Call service to delete company
        companyService.deleteCompany(companyId);

        // Redirect back to dashboard
        return "redirect:/admin/dashboard";
    }

    // Handles POST request to approve a company
    @PostMapping("/admin/companies/{companyId}/approve")
    public String approveCompany(@PathVariable Long companyId) {
        // Approve the company
        companyService.approveCompany(companyId);

        // Redirect back to dashboard
        return "redirect:/admin/dashboard";
    }

    // Handles POST request to reject a company
    @PostMapping("/admin/companies/{companyId}/reject")
    public String rejectCompany(@PathVariable Long companyId) {
        // Reject the company
        companyService.rejectCompany(companyId);

        // Redirect back to dashboard
        return "redirect:/admin/dashboard";
    }

    // Handles POST request to delete a placement drive
    @PostMapping("/admin/drives/{driveId}/delete")
    public String deleteDrive(@PathVariable Long driveId) {
        // Delete the drive
        placementDriveService.deleteDrive(driveId);

        // Redirect to drives page
        return "redirect:/admin/drives";
    }

    // Handles GET request to export reports as CSV file
    @GetMapping("/admin/reports/export")
    public ResponseEntity<byte[]> exportReports() {

        // Generate CSV content from service and convert to bytes
        byte[] content = adminService.generateCsvReport().getBytes(StandardCharsets.UTF_8);

        // Return response with file download headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pms-report.csv") // file name
                .contentType(MediaType.TEXT_PLAIN) // content type
                .body(content); // file data
    }
}
