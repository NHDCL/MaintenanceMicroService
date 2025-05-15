package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.service.EmailService;
import bt.nhdcl.maintenancemicroservice.service.RepairReportService;
import bt.nhdcl.maintenancemicroservice.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/repair-reports")
public class RepairReportController {

    private final RepairReportService repairReportService;

    public RepairReportController(RepairReportService repairReportService) {
        this.repairReportService = repairReportService;
    }

    @Autowired
    private EmailService emailService;

    @Autowired
    private RepairService repairService;

    @PostMapping("/start-time")
    public ResponseEntity<RepairReport> submitStartTime(@RequestBody Map<String, String> requestBody) {
        String startTime = requestBody.get("startTime");
        String repairID = requestBody.get("repairID");

        RepairReport report = new RepairReport();
        report.setRepairID(repairID);
        report.setStartTime(LocalTime.parse(startTime));

        RepairReport saved = repairReportService.createReport(report);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{reportID}/end-time")
    public ResponseEntity<RepairReport> submitEndTime(
            @PathVariable String reportID,
            @RequestBody Map<String, String> requestBody) {

        String endTime = requestBody.get("endTime");

        RepairReport updated = repairReportService.updateEndTime(reportID, LocalTime.parse(endTime));

        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // SECOND PART: Upload completion details + images + send email
    @PutMapping("/complete/{reportID}")
    public ResponseEntity<RepairReport> completeRepairReport(
            @PathVariable String reportID,
            @RequestParam(required = false) String finishedDate,
            @RequestParam int totalCost,
            @RequestParam String information,
            @RequestParam String partsUsed,
            @RequestParam String technicians,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles) {

        RepairReport updatedData = new RepairReport();
        if (finishedDate != null)
            updatedData.setFinishedDate(LocalDate.parse(finishedDate));
        updatedData.setTotalCost(totalCost);
        updatedData.setInformation(information);
        updatedData.setPartsUsed(partsUsed);
        updatedData.setTechnicians(technicians);

        RepairReport updated = repairReportService.updateReport(reportID, updatedData, imageFiles);

        // Send email if repairID exists
        Optional<RepairReport> optionalReport = repairReportService.getRepairReportById(reportID);
        optionalReport.ifPresent(report -> {
            Optional<Repair> optionalRepair = repairService.getRepairById(report.getRepairID());
            optionalRepair.ifPresent(repair -> {
                emailService.sendEmail(
                        repair.getEmail(),
                        "Repair Request Completed",
                        "Dear " + repair.getName() + ",\n\n" +
                                "We are pleased to inform you that your repair request for the asset: " +
                                repair.getAssetName() + " has been successfully completed.\n\n" +
                                "Thank you,\n\nNHDCL");
            });
        });

        return ResponseEntity.ok(updated);
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
