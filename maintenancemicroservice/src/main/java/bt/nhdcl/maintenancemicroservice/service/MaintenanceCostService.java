package bt.nhdcl.maintenancemicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import bt.nhdcl.maintenancemicroservice.entity.CombinedMaintenanceCostResult;
import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceReportRepository;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;
import bt.nhdcl.maintenancemicroservice.repository.RepairReportRepository;

@Service
public class MaintenanceCostService {

    @Autowired
    private PreventiveMaintenanceRepository preventiveRepo;

    @Autowired
    private RepairReportRepository repairRepo;

    @Autowired
    private PreventiveMaintenanceReportRepository preventiveReportRepo;

    public List<CombinedMaintenanceCostResult> getAllMaintenanceCosts() {
        List<PreventiveMaintenance> preventives = preventiveRepo.findAll();
        List<RepairReport> repairs = repairRepo.findAll();
    
        Map<String, CombinedMaintenanceCostResult> costMap = new HashMap<>();
    
        // Preventive maintenance + reports
        for (PreventiveMaintenance p : preventives) {
            // Corrected method name here
            List<PreventiveMaintenanceReport> reports = preventiveReportRepo.findByPreventiveMaintenanceID(p.getMaintenanceID()); // Use the correct method
    
            for (PreventiveMaintenanceReport r : reports) {
                String key = p.getMaintenanceID() + "-" + r.getFinishedDate().getYear() + "-" + r.getFinishedDate().getMonthValue();
    
                double combined = r.getTotalCost() + (p.getAddCost() != 0 ? p.getAddCost() : 0);
                costMap.merge(key,
                        new CombinedMaintenanceCostResult(p.getAcademyId(), r.getFinishedDate().getYear(),
                                r.getFinishedDate().getMonthValue(), combined),
                        (oldVal, newVal) -> {
                            oldVal.setTotalCost(oldVal.getTotalCost() + newVal.getTotalCost());
                            return oldVal;
                        });
            }
        }
    
        // Repair reports
        for (RepairReport r : repairs) {
            double addCost = (r.getTotalCost() != 0) ? r.getTotalCost() : 0; // Correcting the addCost reference
    
            String key = r.getRepairReportID() + "-" + r.getFinishedDate().getYear() + "-" + r.getFinishedDate().getMonthValue(); // Fix here
            double combined = r.getTotalCost() + addCost;
    
            costMap.merge(key,
                    new CombinedMaintenanceCostResult(r.getRepairReportID(), r.getFinishedDate().getYear(),
                            r.getFinishedDate().getMonthValue(), combined),
                    (oldVal, newVal) -> {
                        oldVal.setTotalCost(oldVal.getTotalCost() + newVal.getTotalCost());
                        return oldVal;
                    });
        }
    
        return new ArrayList<>(costMap.values());
    }
    
}
