package com.pms.repository;

import com.pms.model.PlacementDrive;
import com.pms.model.enums.DriveStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    List<PlacementDrive> findByStatus(DriveStatus status);
    List<PlacementDrive> findByCompanyId(Long companyId);
    void deleteByCompanyId(Long companyId);
}
