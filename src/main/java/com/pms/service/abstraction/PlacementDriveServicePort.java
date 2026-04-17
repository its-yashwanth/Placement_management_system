package com.pms.service.abstraction;

import com.pms.dto.DriveForm;
import com.pms.model.PlacementDrive;
import java.util.List;

public interface PlacementDriveServicePort {
    List<PlacementDrive> getOpenDrives();
    List<PlacementDrive> getAllDrives();
    PlacementDrive getDriveById(Long id);
    PlacementDrive createDrive(Long companyId, DriveForm form);
    PlacementDrive updateDrive(Long driveId, DriveForm form);
    void deleteDrive(Long driveId);
}
