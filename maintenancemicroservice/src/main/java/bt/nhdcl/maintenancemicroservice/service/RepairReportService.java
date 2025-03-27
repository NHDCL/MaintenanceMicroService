package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import java.util.List;
import java.util.Optional;

public interface RepairReportService {
    RepairReport createRepairReport(RepairReport repairReport);
    List<RepairReport> getAllRepairReports();
    Optional<RepairReport> getRepairReportById(String repairReportID);
    List<RepairReport> getRepairReportsByRepairID(String repairID);
    RepairReport updateRepairReport(String repairReportID, RepairReport repairReport);
    void deleteRepairReport(String repairReportID);
}
