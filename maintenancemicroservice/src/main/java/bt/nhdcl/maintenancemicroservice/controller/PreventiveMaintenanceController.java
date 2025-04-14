package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.service.PreventiveMaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maintenance")
public class PreventiveMaintenanceController {

    private final PreventiveMaintenanceService maintenanceService;

    public PreventiveMaintenanceController(PreventiveMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // Create a new maintenance record
    @PostMapping
    public ResponseEntity<PreventiveMaintenance> createMaintenance(@RequestBody PreventiveMaintenance maintenance) {
        PreventiveMaintenance savedMaintenance = maintenanceService.saveMaintenance(maintenance);
        return ResponseEntity.ok(savedMaintenance);
    }

    // Get all maintenance records
    @GetMapping
    public ResponseEntity<List<PreventiveMaintenance>> getAllMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenance());
    }

    // Get a maintenance record by ID
    @GetMapping("/{id}")
    public ResponseEntity<PreventiveMaintenance> getMaintenanceById(@PathVariable String id) {
        Optional<PreventiveMaintenance> maintenance = maintenanceService.getMaintenanceById(id);
        return maintenance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a maintenance record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable String id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    // Get maintenance records by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PreventiveMaintenance>> getMaintenanceByStatus(@PathVariable String status) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByStatus(status));
    }

    // Get maintenance records by start date
    @GetMapping("/date/{startDate}")
    public ResponseEntity<List<PreventiveMaintenance>> getMaintenanceByStartDate(@PathVariable String startDate) {
        LocalDate date = LocalDate.parse(startDate);
        return ResponseEntity.ok(maintenanceService.getMaintenanceByStartDate(date));
    }

    // Get maintenance records by asset code
    @GetMapping("/asset/{assetCode}")
    public ResponseEntity<List<PreventiveMaintenance>> getMaintenanceByAssetCode(@PathVariable String assetCode) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByAssetCode(assetCode));
    }

    @PutMapping("/{id}")
    public PreventiveMaintenance update(@PathVariable String id, @RequestBody PreventiveMaintenance maintenance) {
        return maintenanceService.update(id, maintenance);
    }
}
