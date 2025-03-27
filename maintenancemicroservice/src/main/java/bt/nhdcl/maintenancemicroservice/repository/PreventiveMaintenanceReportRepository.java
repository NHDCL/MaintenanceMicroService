package bt.nhdcl.maintenancemicroservice.repository;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreventiveMaintenanceReportRepository extends MongoRepository<PreventiveMaintenanceReport, String> {
    // You can add custom query methods here if needed
}
