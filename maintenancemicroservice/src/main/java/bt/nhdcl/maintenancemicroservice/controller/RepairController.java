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
public ResponseEntity<Repair> createRepair(@RequestParam("repair") String repairJson,
                                           @RequestParam("image") MultipartFile imageFile) {
    try {
        // Deserialize the repairJson into a Repair object using ObjectMapper
        Repair repair = new ObjectMapper().readValue(repairJson, Repair.class);
        
        // Create repair using the service
        Repair createdRepair = repairService.createRepair(repair, imageFile);
        
        return new ResponseEntity<>(createdRepair, HttpStatus.CREATED);
    } catch (JsonProcessingException e) {
        // Handle the exception (e.g., log it, return a bad request response)
        e.printStackTrace();  // Log the exception
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Return 400 Bad Request if JSON is invalid
    }
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
}
