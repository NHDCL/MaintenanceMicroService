package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Repair;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface RepairService {
    Repair createRepair(Repair repair, MultipartFile[] imageFiles);
    List<Repair> getAllRepairs();
    Optional<Repair> getRepairById(String repairID);
    Repair updateRepair(String repairID, Repair repair);
    void deleteRepair(String repairID);
}
