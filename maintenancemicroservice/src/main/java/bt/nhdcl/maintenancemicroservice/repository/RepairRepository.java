package bt.nhdcl.maintenancemicroservice.repository;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairRepository extends MongoRepository<Repair, String> {
    // You can add custom query methods here if needed
}
