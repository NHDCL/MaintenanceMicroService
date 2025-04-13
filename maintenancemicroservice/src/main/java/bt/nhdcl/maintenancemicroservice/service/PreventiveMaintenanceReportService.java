package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface PreventiveMaintenanceReportService {
    PreventiveMaintenanceReport createReport(PreventiveMaintenanceReport report, List<MultipartFile> imageFiles);
    List<PreventiveMaintenanceReport> getAllReports();
    Optional<PreventiveMaintenanceReport> getReportById(String id);
    PreventiveMaintenanceReport updateReport(String id, PreventiveMaintenanceReport updatedReport);
    void deleteReport(String id);
}
