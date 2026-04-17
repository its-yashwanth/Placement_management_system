package com.pms.controller;

import com.pms.service.abstraction.AdminServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final AdminServicePort adminService;
    private final StudentServicePort studentService;
    private final CompanyServicePort companyService;
    private final PlacementDriveServicePort placementDriveService;

    public AdminController(AdminServicePort adminService,
                           StudentServicePort studentService,
                           CompanyServicePort companyService,
                           PlacementDriveServicePort placementDriveService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.companyService = companyService;
        this.placementDriveService = placementDriveService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("counts", adminService.getDashboardCounts());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("pendingCompanies", companyService.getPendingCompanies());
        return "admin/dashboard";
    }

    @GetMapping("/admin/reports")
    public String adminReports(Model model) {
        model.addAttribute("counts", adminService.getDashboardCounts());
        model.addAttribute("statusCounts", adminService.getApplicationStatusCounts());
        model.addAttribute("placedStudents", adminService.getPlacedStudentApplications());
        return "admin/reports";
    }

    @GetMapping("/admin/drives")
    public String adminDrives(Model model) {
        model.addAttribute("drives", placementDriveService.getAllDrives());
        return "admin/drives";
    }

    @PostMapping("/admin/students/{studentId}/delete")
    public String deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/companies/{companyId}/delete")
    public String deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/companies/{companyId}/approve")
    public String approveCompany(@PathVariable Long companyId) {
        companyService.approveCompany(companyId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/companies/{companyId}/reject")
    public String rejectCompany(@PathVariable Long companyId) {
        companyService.rejectCompany(companyId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/drives/{driveId}/delete")
    public String deleteDrive(@PathVariable Long driveId) {
        placementDriveService.deleteDrive(driveId);
        return "redirect:/admin/drives";
    }

    @GetMapping("/admin/reports/export")
    public ResponseEntity<byte[]> exportReports() {
        byte[] content = adminService.generateCsvReport().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pms-report.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }
}
