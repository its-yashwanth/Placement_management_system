package com.pms.service.report;

import java.util.Map;

public interface ReportExporter {
    String export(Map<String, Long> dashboardCounts, Map<String, Long> statusCounts);
    String getFormat();
}
