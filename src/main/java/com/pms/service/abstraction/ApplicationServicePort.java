package com.pms.service.abstraction;

import com.pms.model.Application;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationServicePort {
    List<Application> getApplicationsForStudent(Long studentId);
    List<Application> getApplicationsForDrive(Long driveId);
    Application applyForDrive(Long studentId, Long driveId);
    Application shortlist(Long applicationId);
    Application scheduleInterview(Long applicationId, LocalDateTime interviewAt);
    Application selectCandidate(Long applicationId);
    Application rejectApplication(Long applicationId);
    Application verifyFinalSelection(Long applicationId);
    List<Application> getSelectedApplications();
    Application getApplicationById(Long applicationId);
}
