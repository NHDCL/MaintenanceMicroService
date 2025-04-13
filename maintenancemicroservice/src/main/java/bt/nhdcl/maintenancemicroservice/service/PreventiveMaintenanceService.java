package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PreventiveMaintenanceService {
    
    // Save a maintenance record
    PreventiveMaintenance saveMaintenance(PreventiveMaintenance maintenance);

    // Get all maintenance records
    List<PreventiveMaintenance> getAllMaintenance();

    // Get a maintenance record by ID
    Optional<PreventiveMaintenance> getMaintenanceById(String maintenanceID);

    // Delete a maintenance record by ID
    void deleteMaintenance(String maintenanceID);

    // Get maintenance records by status
    List<PreventiveMaintenance> getMaintenanceByStatus(String status);
    
    // Get maintenance records by start date
    List<PreventiveMaintenance> getMaintenanceByStartDate(LocalDate startDate);

    // Get maintenance records by asset code
    List<PreventiveMaintenance> getMaintenanceByAssetCode(String assetCode);
}
