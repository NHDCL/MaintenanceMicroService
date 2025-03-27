package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;

import java.util.List;
import java.util.Optional;

public interface PreventiveMaintenanceReportService {
    PreventiveMaintenanceReport saveReport(PreventiveMaintenanceReport report);
    List<PreventiveMaintenanceReport> getAllReports();
    Optional<PreventiveMaintenanceReport> getReportById(String id);
    PreventiveMaintenanceReport updateReport(String id, PreventiveMaintenanceReport updatedReport);
    void deleteReport(String id);
}
