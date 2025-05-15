package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.service.PreventiveMaintenanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/maintenance-reports")
public class PreventiveMaintenanceReportController {

    private final PreventiveMaintenanceReportService service;

    public PreventiveMaintenanceReportController(PreventiveMaintenanceReportService service) {
        this.service = service;
    }

    @PostMapping("/start-time")
    public ResponseEntity<PreventiveMaintenanceReport> submitStartTime(@RequestBody Map<String, String> requestBody) {
        String startTime = requestBody.get("startTime");
        String maintenanceID = requestBody.get("preventiveMaintenanceID");

        PreventiveMaintenanceReport report = new PreventiveMaintenanceReport();
        report.setPreventiveMaintenanceID(maintenanceID);
        report.setStartTime(LocalTime.parse(startTime));

        PreventiveMaintenanceReport saved = service.createReport(report, null);
        return ResponseEntity.ok(saved);
    }

    // 2. Submit end time
    @PutMapping("/{maintenanceReportID}/end-time")
    public ResponseEntity<PreventiveMaintenanceReport> submitEndTime(
            @PathVariable String maintenanceReportID,
            @RequestBody Map<String, String> requestBody) {

        String endTime = requestBody.get("endTime");
        PreventiveMaintenanceReport updated = service.updateEndTime(maintenanceReportID, LocalTime.parse(endTime));

        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 3. Complete maintenance report (details + images, NO email)
    @PutMapping("/complete/{maintenanceReportID}")
    public ResponseEntity<PreventiveMaintenanceReport> completeReport(
            @PathVariable String maintenanceReportID,
            @RequestParam(required = false) String finishedDate,
            @RequestParam int totalCost,
            @RequestParam String information,
            @RequestParam String partsUsed,
            @RequestParam String technicians,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles) {

        PreventiveMaintenanceReport updatedData = new PreventiveMaintenanceReport();
        if (finishedDate != null)
            updatedData.setFinishedDate(LocalDate.parse(finishedDate));

        updatedData.setTotalCost(totalCost);
        updatedData.setInformation(information);
        updatedData.setPartsUsed(partsUsed);
        updatedData.setTechnicians(technicians);

        PreventiveMaintenanceReport updated = service.updateReport(maintenanceReportID, updatedData, imageFiles);
        return ResponseEntity.ok(updated);
    }

    // Create a new Maintenance Report
    @PostMapping
    public ResponseEntity<PreventiveMaintenanceReport> create(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String finishedDate,
            @RequestParam int totalCost,
            @RequestParam String information,
            @RequestParam String partsUsed,
            @RequestParam String technicians,
            @RequestParam String preventiveMaintenanceID,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles) {
        PreventiveMaintenanceReport report = new PreventiveMaintenanceReport();

        if (startTime != null)
            report.setStartTime(LocalTime.parse(startTime));
        if (endTime != null)
            report.setEndTime(LocalTime.parse(endTime));
        if (finishedDate != null)
            report.setFinishedDate(LocalDate.parse(finishedDate));

        report.setTotalCost(totalCost);
        report.setInformation(information);
        report.setPartsUsed(partsUsed);
        report.setTechnicians(technicians);
        report.setPreventiveMaintenanceID(preventiveMaintenanceID);

        PreventiveMaintenanceReport created = service.createReport(report, imageFiles);
        return ResponseEntity.ok(created);
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

    @GetMapping("/by-maintenance-id/{id}")
    public List<PreventiveMaintenanceReport> getReportsByPreventiveMaintenanceID(@PathVariable("id") String preventiveMaintenanceID) {
        return service.getReportsByPreventiveMaintenanceID(preventiveMaintenanceID);
    }
}
