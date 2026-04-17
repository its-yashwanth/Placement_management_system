package com.pms.service.report;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CsvReportExporter implements ReportExporter {

    @Override
    public String export(Map<String, Long> dashboardCounts, Map<String, Long> statusCounts) {
        StringBuilder builder = new StringBuilder();
        builder.append("Metric,Value\n");
        dashboardCounts.forEach((key, value) -> builder.append(key).append(',').append(value).append('\n'));
        statusCounts.forEach((key, value) -> builder.append(key).append(',').append(value).append('\n'));
        return builder.toString();
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}
