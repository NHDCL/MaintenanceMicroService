package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.service.EmailService;
import bt.nhdcl.maintenancemicroservice.service.RepairService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/repairs")

public class RepairController {

    private final RepairService repairService;

    @Autowired
    private EmailService emailService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    // Create a new repair
    @PostMapping
    public ResponseEntity<Repair> createRepair(
            @RequestParam String name,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String priority,
            @RequestParam String status,
            @RequestParam String area,
            @RequestParam String description,
            @RequestParam String assetName,
            @RequestParam boolean scheduled,
            @RequestParam String academyId,
            @RequestParam("images") MultipartFile[] imageFiles) {

        Repair repair = new Repair();
        repair.setName(name);
        repair.setPhoneNumber(phoneNumber);
        repair.setEmail(email);
        repair.setPriority(priority);
        repair.setStatus(status);
        repair.setArea(area);
        repair.setDescription(description);
        repair.setAssetName(assetName);
        repair.setScheduled(scheduled);
        repair.setAcademyId(academyId);

        Repair createdRepair = repairService.createRepair(repair, imageFiles);
        return new ResponseEntity<>(createdRepair, HttpStatus.CREATED);
    }

    // Get all repairs
    @GetMapping
    public ResponseEntity<List<Repair>> getAllRepairs() {
        List<Repair> repairs = repairService.getAllRepairs();
        return new ResponseEntity<>(repairs, HttpStatus.OK);
    }

    // Get a specific repair by ID
    @GetMapping("/{repairID}")
    public ResponseEntity<Repair> getRepairById(@PathVariable String repairID) {
        Optional<Repair> repair = repairService.getRepairById(repairID);
        return repair.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a repair by ID
    @PutMapping("/update/{repairID}")
    public ResponseEntity<Map<String, Object>> updateRepair(@PathVariable String repairID, @RequestBody Map<String, Object> updateFields) {
        Map<String, Object> result = repairService.updateRepair(repairID, updateFields);
        return ResponseEntity.ok(result); // This will automatically convert the Map to JSON
    }

    // Delete a repair by ID
    @DeleteMapping("/{repairID}")
    public ResponseEntity<Void> deleteRepair(@PathVariable String repairID) {
        repairService.deleteRepair(repairID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{repairId}/accept")
    public ResponseEntity<String> acceptOrRejectRepair(
            @PathVariable String repairId,
            @RequestBody Map<String, Boolean> requestBody) {

        // Get the 'accept' value directly from the request body
        Boolean accept = requestBody.get("accept");

        if (accept == null) {
            return ResponseEntity
                    .status(400)
                    .body("'accept' field is missing or invalid in the request body.");
        }

        // Proceed with the logic to accept or reject based on the 'accept' value
        Boolean result = repairService.acceptRepairById(repairId, accept);

        if (result) {
            String message = accept
                    ? "Repair request accepted and email sent."
                    : "Repair request rejected and email sent.";
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity
                    .status(404)
                    .body("Repair request with ID " + repairId + " not found.");
        }
    }

    @PutMapping("/schedule/{repairId}")
    public ResponseEntity<String> assignRepair(
            @PathVariable String repairId,
            @RequestBody Map<String, String> request) {

        String email = request.get("email");

        boolean scheduled = repairService.setScheduleTrue(repairId);

        if (scheduled) {
            // Compose structured email
            String subject = "Repair Task Assigned";
            String body = "Dear Supervisor,\n\n"
                    + "We would like to inform you that the repair task with Report ID: " + repairId
                    + " has been assigned to you.\n\n"
                    + "Please ensure the task is completed as early as possible. Your effort and timely response are highly appreciated.\n\n"
                    + "Best regards,\n"
                    + "NHDCL";

            emailService.sendEmail(email, subject, body);

            return ResponseEntity.ok("Repair task assigned and email sent to " + email);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Repair with ID " + repairId + " not found.");
        }
    }

}
