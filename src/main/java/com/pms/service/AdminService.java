package com.pms.service;

import com.pms.model.Student;
import com.pms.model.enums.ApplicationStatus;
import com.pms.repository.ApplicationRepository;
import com.pms.repository.CompanyRepository;
import com.pms.repository.PlacementDriveRepository;
import com.pms.repository.StudentRepository;
import com.pms.service.abstraction.AdminServicePort;
import com.pms.service.report.ReportExporter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements AdminServicePort {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PlacementDriveRepository placementDriveRepository;
    private final ApplicationRepository applicationRepository;
    private final ReportExporter reportExporter;

    public AdminService(StudentRepository studentRepository,
                        CompanyRepository companyRepository,
                        PlacementDriveRepository placementDriveRepository,
                        ApplicationRepository applicationRepository,
                        ReportExporter reportExporter) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.placementDriveRepository = placementDriveRepository;
        this.applicationRepository = applicationRepository;
        this.reportExporter = reportExporter;
    }

    @Override
    public Map<String, Long> getDashboardCounts() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Students", studentRepository.count());
        counts.put("Companies", companyRepository.count());
        counts.put("Drives", placementDriveRepository.count());
        counts.put("Applications", applicationRepository.count());
        return counts;
    }

    @Override
    public Map<String, Long> getApplicationStatusCounts() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Eligible", applicationRepository.countByStatus(ApplicationStatus.ELIGIBLE));
        counts.put("Shortlisted", applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED));
        counts.put("Interview Scheduled", applicationRepository.countByStatus(ApplicationStatus.INTERVIEW_SCHEDULED));
        counts.put("Selected", applicationRepository.countByStatus(ApplicationStatus.SELECTED));
        counts.put("Rejected", applicationRepository.countByStatus(ApplicationStatus.REJECTED));
        return counts;
    }

    @Override
    public List<com.pms.model.Application> getPlacedStudentApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.SELECTED);
    }

    @Override
    public String generateCsvReport() {
        return reportExporter.export(getDashboardCounts(), getApplicationStatusCounts());
    }
}
