package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Repair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface RepairService {
    Repair createRepair(Repair repair, MultipartFile[] imageFiles);
    List<Repair> getAllRepairs();
    Optional<Repair> getRepairById(String repairID);
    Map<String, Object> updateRepair(String repairID, Map<String, Object> updateFields);
    void deleteRepair(String repairID);
    Boolean acceptRepairById(String repairId, Boolean accept);
    boolean setScheduleTrue(String repairId);
}
