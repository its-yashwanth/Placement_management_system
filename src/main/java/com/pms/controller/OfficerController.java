package com.pms.controller;

import com.pms.dto.DriveForm;
import com.pms.service.abstraction.ApplicationServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * OfficeController handles all operations related to placement officers.
 * 
 * Responsibilities:
 * - Managing officer-related requests
 * - Coordinating between students and companies
 * - Handling administrative placement tasks
 */

@Controller
public class OfficerController {

    private final CompanyServicePort companyService;
    private final PlacementDriveServicePort placementDriveService;
    private final ApplicationServicePort applicationService;

    public OfficerController(CompanyServicePort companyService,
                             PlacementDriveServicePort placementDriveService,
                             ApplicationServicePort applicationService) {
        this.companyService = companyService;
        this.placementDriveService = placementDriveService;
        this.applicationService = applicationService;
    }

    @GetMapping("/officer/dashboard")
    public String officerDashboard(Model model) {
        model.addAttribute("pendingCompanies", companyService.getPendingCompanies());
        model.addAttribute("approvedCompanies", companyService.getApprovedCompanies());
        model.addAttribute("drives", placementDriveService.getAllDrives());
        model.addAttribute("selectedApplications", applicationService.getSelectedApplications());
        return "officer/dashboard";
    }

    @PostMapping("/officer/companies/{companyId}/approve")
    public String approveCompany(@PathVariable Long companyId) {
        companyService.approveCompany(companyId);
        return "redirect:/officer/dashboard";
    }

    @PostMapping("/officer/companies/{companyId}/reject")
    public String rejectCompany(@PathVariable Long companyId) {
        companyService.rejectCompany(companyId);
        return "redirect:/officer/dashboard";
    }

    @GetMapping("/officer/drives/{driveId}/edit")
    public String editDrive(@PathVariable Long driveId, Model model) {
        var drive = placementDriveService.getDriveById(driveId);
        DriveForm driveForm = new DriveForm();
        driveForm.setTitle(drive.getTitle());
        driveForm.setDescription(drive.getDescription());
        driveForm.setEligibleBranch(drive.getEligibleBranch());
        driveForm.setMinimumCgpa(drive.getMinimumCgpa());
        driveForm.setLocation(drive.getLocation());
        driveForm.setDriveDate(drive.getDriveDate());
        model.addAttribute("driveId", driveId);
        model.addAttribute("driveForm", driveForm);
        return "officer/edit-drive";
    }

    @PostMapping("/officer/drives/{driveId}/edit")
    public String updateDrive(@PathVariable Long driveId,
                              @Valid DriveForm driveForm,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("driveId", driveId);
            return "officer/edit-drive";
        }
        placementDriveService.updateDrive(driveId, driveForm);
        return "redirect:/officer/dashboard";
    }

    @PostMapping("/officer/drives/{driveId}/delete")
    public String deleteDrive(@PathVariable Long driveId) {
        placementDriveService.deleteDrive(driveId);
        return "redirect:/officer/dashboard";
    }

    @GetMapping("/officer/drives/{driveId}/applications")
    public String officerApplications(@PathVariable Long driveId, Model model) {
        model.addAttribute("drive", placementDriveService.getDriveById(driveId));
        model.addAttribute("applications", applicationService.getApplicationsForDrive(driveId));
        return "officer/applications";
    }

    @PostMapping("/officer/applications/{applicationId}/verify")
    public String verifySelection(@PathVariable Long applicationId) {
        applicationService.verifyFinalSelection(applicationId);
        return "redirect:/officer/dashboard";
    }
}
