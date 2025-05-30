package bt.nhdcl.maintenancemicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.RepairRepository;
import bt.nhdcl.maintenancemicroservice.repository.RepairReportRepository;

@Service
public class RepairAnalyticsService {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private RepairReportRepository repairReportRepository;

    public List<Map<String, Object>> getAverageResponseTimes() {
        List<Repair> repairs = repairRepository.findAll();
        List<RepairReport> reports = repairReportRepository.findAll();

        // Count how many reports exist per repairID
        Map<String, Long> reportCounts = reports.stream()
                .filter(r -> r.getRepairID() != null)
                .collect(Collectors.groupingBy(RepairReport::getRepairID, Collectors.counting()));

        // Group durations by academyId-year-month
        Map<String, List<Long>> groupedDurations = new HashMap<>();

        for (Repair repair : repairs) {
            String repairId = repair.getRepairID();

            // Exclude missing IDs or submissionDate
            if (repairId == null || repair.getSubmissionDate() == null) continue;

            // Exclude if multiple reports exist for a single repair
            if (reportCounts.getOrDefault(repairId, 0L) != 1L) continue;

            // Find the single matching report
            RepairReport matchingReport = reports.stream()
                    .filter(r -> repairId.equals(r.getRepairID()))
                    .findFirst()
                    .orElse(null);

            // Exclude if report is missing or has incomplete date/time
            if (matchingReport == null ||
                matchingReport.getFinishedDate() == null ||
                matchingReport.getEndTime() == null) {
                continue;
            }

            // Combine date and time into LocalDateTime
            LocalDateTime finishedDateTime = LocalDateTime.of(matchingReport.getFinishedDate(), matchingReport.getEndTime());

            // Calculate duration in hours
            Duration duration = Duration.between(repair.getSubmissionDate(), finishedDateTime);
            long hours = duration.toHours();

            // Build key
            String key = repair.getAcademyId() + "-" +
                         matchingReport.getFinishedDate().getYear() + "-" +
                         matchingReport.getFinishedDate().getMonthValue();

            groupedDurations.computeIfAbsent(key, k -> new ArrayList<>()).add(hours);
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

        // Sort results
        results.sort(Comparator
                .comparing((Map<String, Object> m) -> (String) m.get("academyId"))
                .thenComparing(m -> (Integer) m.get("year"))
                .thenComparing(m -> (Integer) m.get("month")));

        return results;
    }
}
