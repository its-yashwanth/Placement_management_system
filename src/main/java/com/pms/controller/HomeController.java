package com.pms.controller;

import com.pms.service.abstraction.AdminServicePort;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AdminServicePort adminService;
    private final StudentServicePort studentService;
    private final CompanyServicePort companyService;
    private final PlacementDriveServicePort placementDriveService;

    public HomeController(AdminServicePort adminService,
                          StudentServicePort studentService,
                          CompanyServicePort companyService,
                          PlacementDriveServicePort placementDriveService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.companyService = companyService;
        this.placementDriveService = placementDriveService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
