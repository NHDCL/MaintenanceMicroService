package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.RepairReport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface RepairReportService {
    // RepairReport createRepairReport(RepairReport repairReport, List<MultipartFile> imageFiles);
    RepairReport createReport(RepairReport report);
    RepairReport updateEndTime(String reportID, LocalTime endTime, LocalDate finishedDate);
    RepairReport updateReport(String reportID, RepairReport updatedData, List<MultipartFile> images);
    List<RepairReport> getAllRepairReports();
    Optional<RepairReport> getRepairReportById(String repairReportID);
    List<RepairReport> getRepairReportsByRepairID(String repairID);
    RepairReport updateRepairReport(String repairReportID, RepairReport repairReport);
    void deleteRepairReport(String repairReportID);
}
