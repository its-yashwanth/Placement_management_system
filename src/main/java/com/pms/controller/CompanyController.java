package com.pms.controller;

import com.pms.dto.ApplicationDecisionForm;
import com.pms.dto.DriveForm;
import com.pms.service.abstraction.ApplicationServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompanyController {

    private final CompanyServicePort companyService;
    private final PlacementDriveServicePort placementDriveService;
    private final ApplicationServicePort applicationService;

    public CompanyController(CompanyServicePort companyService,
                             PlacementDriveServicePort placementDriveService,
                             ApplicationServicePort applicationService) {
        this.companyService = companyService;
        this.placementDriveService = placementDriveService;
        this.applicationService = applicationService;
    }

    @GetMapping("/company/dashboard")
    public String companyDashboard(Authentication authentication, Model model) {
        var company = companyService.getByEmail(authentication.getName());
        model.addAttribute("company", company);
        model.addAttribute("drives", placementDriveService.getAllDrives().stream()
                .filter(drive -> drive.getCompany().getId().equals(company.getId()))
                .toList());
        model.addAttribute("driveForm", new DriveForm());
        model.addAttribute("decisionForm", new ApplicationDecisionForm());
        return "company/dashboard";
    }

    @PostMapping("/company/drives")
    public String createDrive(Authentication authentication,
                              @Valid DriveForm driveForm,
                              BindingResult bindingResult,
                              Model model) {
        Long companyId = companyService.getByEmail(authentication.getName()).getId();
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", companyService.getCompanyById(companyId));
            model.addAttribute("drives", placementDriveService.getAllDrives().stream()
                    .filter(drive -> drive.getCompany().getId().equals(companyId))
                    .toList());
            model.addAttribute("decisionForm", new ApplicationDecisionForm());
            return "company/dashboard";
        }

        placementDriveService.createDrive(companyId, driveForm);
        return "redirect:/company/dashboard";
    }

    @GetMapping("/company/drives/{driveId}/edit")
    public String editDrive(@PathVariable Long driveId, Model model, Authentication authentication) {
        var company = companyService.getByEmail(authentication.getName());
        var drive = placementDriveService.getDriveById(driveId);
        DriveForm form = new DriveForm();
        form.setTitle(drive.getTitle());
        form.setDescription(drive.getDescription());
        form.setEligibleBranch(drive.getEligibleBranch());
        form.setMinimumCgpa(drive.getMinimumCgpa());
        form.setLocation(drive.getLocation());
        form.setDriveDate(drive.getDriveDate());

        model.addAttribute("company", company);
        model.addAttribute("driveId", driveId);
        model.addAttribute("driveForm", form);
        return "company/edit-drive";
    }

    @PostMapping("/company/drives/{driveId}/edit")
    public String updateDrive(@PathVariable Long driveId,
                              @Valid DriveForm driveForm,
                              BindingResult bindingResult,
                              Model model,
                              Authentication authentication) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", companyService.getByEmail(authentication.getName()));
            model.addAttribute("driveId", driveId);
            return "company/edit-drive";
        }
        placementDriveService.updateDrive(driveId, driveForm);
        return "redirect:/company/dashboard";
    }

    @PostMapping("/company/drives/{driveId}/delete")
    public String deleteDrive(@PathVariable Long driveId) {
        placementDriveService.deleteDrive(driveId);
        return "redirect:/company/dashboard";
    }

    @GetMapping("/company/drives/{driveId}/applications")
    public String viewApplications(@PathVariable Long driveId, Model model, Authentication authentication) {
        var company = companyService.getByEmail(authentication.getName());
        model.addAttribute("company", company);
        model.addAttribute("drive", placementDriveService.getDriveById(driveId));
        model.addAttribute("applications", applicationService.getApplicationsForDrive(driveId));
        model.addAttribute("decisionForm", new ApplicationDecisionForm());
        return "company/applications";
    }

    @PostMapping("/company/applications/{applicationId}/shortlist")
    public String shortlist(@PathVariable Long applicationId) {
        Long driveId = applicationService.getApplicationById(applicationId).getPlacementDrive().getId();
        applicationService.shortlist(applicationId);
        return "redirect:/company/drives/" + driveId + "/applications";
    }

    @PostMapping("/company/applications/{applicationId}/schedule")
    public String schedule(@PathVariable Long applicationId,
                           @Valid ApplicationDecisionForm decisionForm,
                           BindingResult bindingResult) {
        Long driveId = applicationService.getApplicationById(applicationId).getPlacementDrive().getId();
        if (!bindingResult.hasErrors()) {
            applicationService.scheduleInterview(applicationId, decisionForm.getInterviewAt());
        }
        return "redirect:/company/drives/" + driveId + "/applications";
    }

    @PostMapping("/company/applications/{applicationId}/select")
    public String select(@PathVariable Long applicationId) {
        Long driveId = applicationService.getApplicationById(applicationId).getPlacementDrive().getId();
        applicationService.selectCandidate(applicationId);
        return "redirect:/company/drives/" + driveId + "/applications";
    }

    @PostMapping("/company/applications/{applicationId}/reject")
    public String rejectApplication(@PathVariable Long applicationId) {
        Long driveId = applicationService.getApplicationById(applicationId).getPlacementDrive().getId();
        applicationService.rejectApplication(applicationId);
        return "redirect:/company/drives/" + driveId + "/applications";
    }
}
