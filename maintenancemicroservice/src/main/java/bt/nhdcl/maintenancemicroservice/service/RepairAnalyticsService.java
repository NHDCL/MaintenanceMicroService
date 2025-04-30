package bt.nhdcl.maintenancemicroservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.Locale;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.*;

@Service
public class RepairAnalyticsService {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairReportRepository repairReportRepository;

    public List<Map<String, Object>> getAverageResponseTimes() {
        List<Repair> repairs = repairRepository.findAll();
        List<RepairReport> reports = repairReportRepository.findAll();

        // Map to group durations by academyId-year-month
        Map<String, List<Long>> groupedDurations = new HashMap<>();

        for (Repair repair : repairs) {
            if (repair.getRepairID() == null || repair.getSubmissionDate() == null) continue;

            // Find matching report
            RepairReport matchingReport = reports.stream()
                    .filter(r -> r.getRepairID() != null && r.getRepairID().equals(repair.getRepairID()))
                    .findFirst()
                    .orElse(null);

            if (matchingReport != null && matchingReport.getFinishedDate() != null && matchingReport.getEndTime() != null) {
                // Combine finished date and time
                LocalDateTime finishedDateTime = LocalDateTime.of(matchingReport.getFinishedDate(), matchingReport.getEndTime());

                // Calculate duration in hours
                Duration duration = Duration.between(repair.getSubmissionDate(), finishedDateTime);
                long hours = duration.toHours();

                // Key: academyId-year-month
                String key = repair.getAcademyId() + "-" + matchingReport.getFinishedDate().getYear() + "-" + matchingReport.getFinishedDate().getMonthValue();
                groupedDurations.computeIfAbsent(key, k -> new ArrayList<>()).add(hours);
            }
        }

        // Convert grouped results to list of maps
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map.Entry<String, List<Long>> entry : groupedDurations.entrySet()) {
            String[] parts = entry.getKey().split("-");
            String academyId = parts[0];
            int year = Integer.parseInt(parts[1]);
            int month = Integer.parseInt(parts[2]);

            double avgHours = entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0);

            Map<String, Object> result = new HashMap<>();
            result.put("academyId", academyId);
            result.put("year", year);
            result.put("month", month);
            result.put("monthName", java.time.Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            result.put("averageResponseTimeHours", avgHours);

            results.add(result);
        }

        // Optionally sort results by academyId, then year and month
        results.sort(Comparator.comparing((Map<String, Object> m) -> (String) m.get("academyId"))
                .thenComparing(m -> (Integer) m.get("year"))
                .thenComparing(m -> (Integer) m.get("month")));

        return results;
    }
}

