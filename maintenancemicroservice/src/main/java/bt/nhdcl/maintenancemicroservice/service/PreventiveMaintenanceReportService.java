package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface PreventiveMaintenanceReportService {
    PreventiveMaintenanceReport create(PreventiveMaintenanceReport report, List<MultipartFile> imageFiles);
    List<PreventiveMaintenanceReport> getAllReports();
    Optional<PreventiveMaintenanceReport> getReportById(String id);
    PreventiveMaintenanceReport updateReport(String id, PreventiveMaintenanceReport updatedReport);
    void deleteReport(String id);
    List<PreventiveMaintenanceReport> getReportsByPreventiveMaintenanceID(String preventiveMaintenanceID);
    PreventiveMaintenanceReport createReport(PreventiveMaintenanceReport report, List<MultipartFile> imageFiles);
    PreventiveMaintenanceReport updateEndTime(String reportID, LocalTime endTime, LocalDate finishedDate);
    PreventiveMaintenanceReport updateReport(String reportID, PreventiveMaintenanceReport updatedData, List<MultipartFile> imageFiles);
}
