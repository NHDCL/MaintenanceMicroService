package bt.nhdcl.maintenancemicroservice.repository;

import bt.nhdcl.maintenancemicroservice.entity.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByUserID(String userID);
    List<Schedule> findByRepairID(String repairID);
    List<Schedule> findByTechnicianEmail(String technicianEmail);
}
