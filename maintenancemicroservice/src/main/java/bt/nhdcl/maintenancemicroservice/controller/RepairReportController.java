package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.service.RepairReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/repair-reports")
public class RepairReportController {

    private final RepairReportService repairReportService;

    public RepairReportController(RepairReportService repairReportService) {
        this.repairReportService = repairReportService;
    }

    // Create a new repair report
    @PostMapping
    public ResponseEntity<RepairReport> createRepairReport(@RequestBody RepairReport repairReport) {
        RepairReport createdRepairReport = repairReportService.createRepairReport(repairReport);
        return new ResponseEntity<>(createdRepairReport, HttpStatus.CREATED);
    }

    // Get all repair reports
    @GetMapping
    public ResponseEntity<List<RepairReport>> getAllRepairReports() {
        List<RepairReport> repairReports = repairReportService.getAllRepairReports();
        return new ResponseEntity<>(repairReports, HttpStatus.OK);
    }

    // Get a specific repair report by ID
    @GetMapping("/{repairReportID}")
    public ResponseEntity<RepairReport> getRepairReportById(@PathVariable String repairReportID) {
        Optional<RepairReport> repairReport = repairReportService.getRepairReportById(repairReportID);
        return repairReport.map(report -> new ResponseEntity<>(report, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get repair reports by repairID
    @GetMapping("/repair/{repairID}")
    public ResponseEntity<List<RepairReport>> getRepairReportsByRepairID(@PathVariable String repairID) {
        List<RepairReport> repairReports = repairReportService.getRepairReportsByRepairID(repairID);
        return new ResponseEntity<>(repairReports, HttpStatus.OK);
    }

    // Update a repair report by ID
    @PutMapping("/{repairReportID}")
    public ResponseEntity<RepairReport> updateRepairReport(@PathVariable String repairReportID, 
                                                           @RequestBody RepairReport repairReport) {
        RepairReport updatedRepairReport = repairReportService.updateRepairReport(repairReportID, repairReport);
        return updatedRepairReport != null
                ? new ResponseEntity<>(updatedRepairReport, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a repair report by ID
    @DeleteMapping("/{repairReportID}")
    public ResponseEntity<Void> deleteRepairReport(@PathVariable String repairReportID) {
        repairReportService.deleteRepairReport(repairReportID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
