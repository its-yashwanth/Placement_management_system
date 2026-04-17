package com.pms.service;

import com.pms.dto.DriveForm;
import com.pms.model.Company;
import com.pms.model.PlacementDrive;
import com.pms.model.enums.DriveStatus;
import com.pms.repository.ApplicationRepository;
import com.pms.repository.PlacementDriveRepository;
import com.pms.service.abstraction.CompanyServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlacementDriveService implements PlacementDriveServicePort {

    private final PlacementDriveRepository placementDriveRepository;
    private final CompanyServicePort companyService;
    private final ApplicationRepository applicationRepository;

    public PlacementDriveService(PlacementDriveRepository placementDriveRepository,
                                 CompanyServicePort companyService,
                                 ApplicationRepository applicationRepository) {
        this.placementDriveRepository = placementDriveRepository;
        this.companyService = companyService;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<PlacementDrive> getOpenDrives() {
        return placementDriveRepository.findByStatus(DriveStatus.OPEN);
    }

    @Override
    public List<PlacementDrive> getAllDrives() {
        return placementDriveRepository.findAll();
    }

    @Override
    public PlacementDrive getDriveById(Long id) {
        return placementDriveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Placement drive not found: " + id));
    }

    @Override
    public PlacementDrive createDrive(Long companyId, DriveForm form) {
        Company company = companyService.getCompanyById(companyId);
        PlacementDrive drive = new PlacementDrive();
        drive.setCompany(company);
        populateDrive(drive, form);
        drive.setStatus(DriveStatus.OPEN);
        return placementDriveRepository.save(drive);
    }

    @Override
    public PlacementDrive updateDrive(Long driveId, DriveForm form) {
        PlacementDrive drive = getDriveById(driveId);
        populateDrive(drive, form);
        return placementDriveRepository.save(drive);
    }

    @Override
    @Transactional
    public void deleteDrive(Long driveId) {
        applicationRepository.deleteByPlacementDriveId(driveId);
        placementDriveRepository.deleteById(driveId);
    }

    private void populateDrive(PlacementDrive drive, DriveForm form) {
        drive.setTitle(form.getTitle());
        drive.setDescription(form.getDescription());
        drive.setEligibleBranch(form.getEligibleBranch());
        drive.setMinimumCgpa(form.getMinimumCgpa());
        drive.setLocation(form.getLocation());
        drive.setDriveDate(form.getDriveDate());
    }
}
