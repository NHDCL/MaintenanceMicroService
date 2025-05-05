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

    // Create a new repair report
    @PostMapping
    public ResponseEntity<RepairReport> createRepairReport(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String finishedDate,
            @RequestParam int totalCost,
            @RequestParam String information,
            @RequestParam String partsUsed,
            @RequestParam String technicians,
            @RequestParam String repairID,
            @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles) {
        RepairReport report = new RepairReport();

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
        report.setRepairID(repairID);

        RepairReport created = repairReportService.createRepairReport(report, imageFiles);

        Optional<Repair> optionalRepair = repairService.getRepairById(repairID);
        optionalRepair.ifPresent(repair -> {
            String userEmail = repair.getEmail();
            String asset = repair.getAssetName();
            emailService.sendEmail(
                    userEmail,
                    "Repair Request Completed",
                    "Dear " + repair.getName() + ",\n\n"
                            + "We are pleased to inform you that your repair request for the asset: "
                            + asset + " has been successfully completed.\n\n"
                            + "Thank you,\n\n"
                            + "NHDCL");
        });
        return ResponseEntity.ok(created);
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
