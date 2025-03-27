package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.service.PreventiveMaintenanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenance-reports")
public class PreventiveMaintenanceReportController {

    private final PreventiveMaintenanceReportService service;

    public PreventiveMaintenanceReportController(PreventiveMaintenanceReportService service) {
        this.service = service;
    }

    // Create a new Maintenance Report
    @PostMapping
    public ResponseEntity<PreventiveMaintenanceReport> createReport(@RequestBody PreventiveMaintenanceReport report) {
        PreventiveMaintenanceReport savedReport = service.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }

    // Get all Maintenance Reports
    @GetMapping
    public ResponseEntity<List<PreventiveMaintenanceReport>> getAllReports() {
        List<PreventiveMaintenanceReport> reports = service.getAllReports();
        return ResponseEntity.ok(reports);
    }

    // Get a single Maintenance Report by ID
    @GetMapping("/{id}")
    public ResponseEntity<PreventiveMaintenanceReport> getReportById(@PathVariable String id) {
        Optional<PreventiveMaintenanceReport> report = service.getReportById(id);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a Maintenance Report
    @PutMapping("/{id}")
    public ResponseEntity<PreventiveMaintenanceReport> updateReport(@PathVariable String id, 
                                                                    @RequestBody PreventiveMaintenanceReport updatedReport) {
        PreventiveMaintenanceReport report = service.updateReport(id, updatedReport);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }

    // Delete a Maintenance Report
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        service.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
