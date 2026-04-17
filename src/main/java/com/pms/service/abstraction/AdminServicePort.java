package com.pms.service.abstraction;

import com.pms.model.Student;
import java.util.List;
import java.util.Map;

public interface AdminServicePort {
    Map<String, Long> getDashboardCounts();
    Map<String, Long> getApplicationStatusCounts();
    List<com.pms.model.Application> getPlacedStudentApplications();
    String generateCsvReport();
}
