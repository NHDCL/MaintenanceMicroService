package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreventiveMaintenanceReportServiceImpl implements PreventiveMaintenanceReportService {

    private final PreventiveMaintenanceReportRepository repository;

    public PreventiveMaintenanceReportServiceImpl(PreventiveMaintenanceReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public PreventiveMaintenanceReport saveReport(PreventiveMaintenanceReport report) {
        return repository.save(report);
    }

    @Override
    public List<PreventiveMaintenanceReport> getAllReports() {
        return repository.findAll();
    }

    @Override
    public Optional<PreventiveMaintenanceReport> getReportById(String id) {
        return repository.findById(id);
    }

    @Override
    public PreventiveMaintenanceReport updateReport(String id, PreventiveMaintenanceReport updatedReport) {
        if (repository.existsById(id)) {
            updatedReport.setMaintenanceReportID(id);
            return repository.save(updatedReport);
        }
        return null; // Handle exception in real implementation
    }

    @Override
    public void deleteReport(String id) {
        repository.deleteById(id);
    }
}
