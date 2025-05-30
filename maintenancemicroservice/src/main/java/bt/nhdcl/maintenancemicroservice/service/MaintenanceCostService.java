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

        // Preventive maintenance
        for (PreventiveMaintenance p : preventives) {
            List<PreventiveMaintenanceReport> reports = preventiveReportRepo.findByPreventiveMaintenanceID(p.getMaintenanceID());

            if (reports == null || reports.size() != 1) continue;

            PreventiveMaintenanceReport r = reports.get(0);

            if (r.getFinishedDate() == null || p.getAcademyId() == null) continue;

            String key = p.getMaintenanceID() + "-" + r.getFinishedDate().getYear() + "-" + r.getFinishedDate().getMonthValue();

            double combined = r.getTotalCost() + p.getAddCost();

            costMap.merge(key,
                    new CombinedMaintenanceCostResult(
                            p.getAcademyId(),
                            r.getFinishedDate().getYear(),
                            r.getFinishedDate().getMonthValue(),
                            combined),
                    (oldVal, newVal) -> {
                        oldVal.setTotalCost(oldVal.getTotalCost() + newVal.getTotalCost());
                        return oldVal;
                    });
        }

        // Repair reports
        for (RepairReport r : repairs) {
            if (r.getFinishedDate() == null || r.getRepairReportID() == null) continue;

            String key = r.getRepairReportID() + "-" + r.getFinishedDate().getYear() + "-" + r.getFinishedDate().getMonthValue();

            // Simulate one-report-per-ID logic (assuming unique ID = one report)
            if (costMap.containsKey(key)) continue;

            double totalCost = r.getTotalCost();

            costMap.put(key,
                    new CombinedMaintenanceCostResult(
                            r.getRepairReportID(),
                            r.getFinishedDate().getYear(),
                            r.getFinishedDate().getMonthValue(),
                            totalCost));
        }

        return new ArrayList<>(costMap.values());
    }
}
