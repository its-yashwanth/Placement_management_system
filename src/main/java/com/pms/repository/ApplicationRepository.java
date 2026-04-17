package com.pms.repository;

import com.pms.model.Application;
import com.pms.model.enums.ApplicationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByPlacementDriveId(Long driveId);
    boolean existsByStudentIdAndPlacementDriveId(Long studentId, Long driveId);
    long countByStatus(ApplicationStatus status);
    List<Application> findByStatus(ApplicationStatus status);
    void deleteByStudentId(Long studentId);
    void deleteByPlacementDriveId(Long driveId);
}
