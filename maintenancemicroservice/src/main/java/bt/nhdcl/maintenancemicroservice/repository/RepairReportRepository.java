package bt.nhdcl.maintenancemicroservice.repository;

import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairReportRepository extends MongoRepository<RepairReport, String> {
    // Custom query methods can be defined here if needed

    // Example: Find all reports for a specific repairID
    List<RepairReport> findByRepairID(String repairID);

    // Example: Find a report by maintenanceReportID
    Optional<RepairReport> findByRepairReportID(String repairReportID);
    
    boolean existsByRepairID(String repairID);
}
