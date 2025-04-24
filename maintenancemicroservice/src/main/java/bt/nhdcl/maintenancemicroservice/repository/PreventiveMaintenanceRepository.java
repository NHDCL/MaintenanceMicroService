package bt.nhdcl.maintenancemicroservice.repository;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PreventiveMaintenanceRepository extends MongoRepository<PreventiveMaintenance, String> {
    // Custom query methods

    // Find all maintenance records by status
    List<PreventiveMaintenance> findByStatus(String status);

    // Find maintenance records by start date
    List<PreventiveMaintenance> findByStartDate(LocalDate startDate);

    // Find all maintenance records for a specific asset
    List<PreventiveMaintenance> findByAssetCode(String assetCode);

    List<PreventiveMaintenance> findByStartDateAndStatus(LocalDate startDate, String status);

    List<PreventiveMaintenance> findByUserID(String userID);

    List<PreventiveMaintenance> findByTechnicianEmail(String technicianEmail);
}

