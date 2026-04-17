package com.pms.service;

import com.pms.model.Application;
import com.pms.model.PlacementDrive;
import com.pms.model.Student;
import com.pms.model.enums.ApplicationStatus;
import com.pms.model.enums.PlacementStatus;
import com.pms.repository.ApplicationRepository;
import com.pms.service.abstraction.ApplicationServicePort;
import com.pms.service.abstraction.PlacementDriveServicePort;
import com.pms.service.abstraction.StudentServicePort;
import com.pms.service.eligibility.EligibilityEvaluator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService implements NotificationPublisher, ApplicationServicePort {

    private final ApplicationRepository applicationRepository;
    private final StudentServicePort studentService;
    private final PlacementDriveServicePort placementDriveService;
    private final EligibilityEvaluator eligibilityEvaluator;
    private final List<NotificationObserver> observers = new ArrayList<>();

    public ApplicationService(ApplicationRepository applicationRepository,
                              StudentServicePort studentService,
                              PlacementDriveServicePort placementDriveService,
                              EligibilityEvaluator eligibilityEvaluator,
                              ConsoleNotificationObserver consoleNotificationObserver) {
        this.applicationRepository = applicationRepository;
        this.studentService = studentService;
        this.placementDriveService = placementDriveService;
        this.eligibilityEvaluator = eligibilityEvaluator;
        registerObserver(consoleNotificationObserver);
    }

    @Override
    public List<Application> getApplicationsForStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Override
    public List<Application> getApplicationsForDrive(Long driveId) {
        return applicationRepository.findByPlacementDriveId(driveId);
    }

    @Override
    public Application applyForDrive(Long studentId, Long driveId) {
        Student student = studentService.getStudentById(studentId);
        PlacementDrive drive = placementDriveService.getDriveById(driveId);

        if (student.getPlacementStatus() == PlacementStatus.PLACED) {
            throw new IllegalStateException("Placed students cannot apply to new drives.");
        }
        if (applicationRepository.existsByStudentIdAndPlacementDriveId(studentId, driveId)) {
            throw new IllegalStateException("Student has already applied for this drive.");
        }

        Application application = new Application();
        application.setStudent(student);
        application.setPlacementDrive(drive);
        application.setAppliedAt(LocalDateTime.now());
        application.setFinalSelectionVerified(false);
        application.setStatus(eligibilityEvaluator.isEligible(student, drive)
                ? ApplicationStatus.ELIGIBLE
                : ApplicationStatus.REJECTED);

        Application saved = applicationRepository.save(application);
        notifyObservers("Application created for student " + student.getFullName() + " in drive " + drive.getTitle());
        return saved;
    }

    @Override
    public Application shortlist(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setStatus(ApplicationStatus.SHORTLISTED);
        notifyObservers("Student " + application.getStudent().getFullName() + " shortlisted for " + application.getPlacementDrive().getTitle());
        return applicationRepository.save(application);
    }

    @Override
    public Application scheduleInterview(Long applicationId, LocalDateTime interviewAt) {
        Application application = getApplicationById(applicationId);
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        application.setInterviewAt(interviewAt);
        notifyObservers("Interview scheduled for " + application.getStudent().getFullName() + " at " + interviewAt);
        return applicationRepository.save(application);
    }

    @Override
    public Application selectCandidate(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setStatus(ApplicationStatus.SELECTED);
        application.setFinalSelectionVerified(false);
        Student student = application.getStudent();
        student.setPlacementStatus(PlacementStatus.PLACED);
        studentService.save(student);
        notifyObservers("Student " + student.getFullName() + " selected by " + application.getPlacementDrive().getCompany().getCompanyName());
        return applicationRepository.save(application);
    }

    @Override
    public Application rejectApplication(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setStatus(ApplicationStatus.REJECTED);
        return applicationRepository.save(application);
    }

    @Override
    public Application verifyFinalSelection(Long applicationId) {
        Application application = getApplicationById(applicationId);
        application.setFinalSelectionVerified(true);
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getSelectedApplications() {
        return applicationRepository.findByStatus(ApplicationStatus.SELECTED);
    }

    @Override
    public Application getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));
    }

    @Override
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        observers.forEach(observer -> observer.update(message));
    }
}
