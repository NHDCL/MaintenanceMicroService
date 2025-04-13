package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.service.RepairService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final RepairService repairService;

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
            @RequestParam String assetCode,
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
        repair.setAssetCode(assetCode);

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
    @PutMapping("/{repairID}")
    public ResponseEntity<Repair> updateRepair(@PathVariable String repairID, @RequestBody Repair repair) {
        Repair updatedRepair = repairService.updateRepair(repairID, repair);
        return updatedRepair != null
                ? new ResponseEntity<>(updatedRepair, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete a repair by ID
    @DeleteMapping("/{repairID}")
    public ResponseEntity<Void> deleteRepair(@PathVariable String repairID) {
        repairService.deleteRepair(repairID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptRepair(@PathVariable String id) {
        boolean updated = repairService.acceptRepairById(id);

        if (updated) {
            return ResponseEntity.ok("Repair accepted successfully.");
        } else {
            return ResponseEntity.status(404).body("Repair not found.");
        }
    }
    @PutMapping("/{id}/schedule")
    public ResponseEntity<String> acceptSchedule(@PathVariable String id) {
        boolean updated = repairService.scheduleRepairById(id);

        if (updated) {
            return ResponseEntity.ok("Repair schedule successfully.");
        } else {
            return ResponseEntity.status(404).body("Repair not found.");
        }
    }
}
