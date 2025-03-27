package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.RepairReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairReportServiceImpl implements RepairReportService {

    private final RepairReportRepository repairReportRepository;

    public RepairReportServiceImpl(RepairReportRepository repairReportRepository) {
        this.repairReportRepository = repairReportRepository;
    }

    @Override
    public RepairReport createRepairReport(RepairReport repairReport) {
        return repairReportRepository.save(repairReport);  // Save the repair report
    }

    @Override
    public List<RepairReport> getAllRepairReports() {
        return repairReportRepository.findAll();  // Get all repair reports
    }

    @Override
    public Optional<RepairReport> getRepairReportById(String repairReportID) {
        return repairReportRepository.findById(repairReportID);  // Get repair report by ID
    }

    @Override
    public List<RepairReport> getRepairReportsByRepairID(String repairID) {
        return repairReportRepository.findByRepairID(repairID);  // Get repair reports by repairID
    }

    @Override
    public RepairReport updateRepairReport(String repairReportID, RepairReport repairReport) {
        Optional<RepairReport> existingReport = repairReportRepository.findById(repairReportID);
        
        if (existingReport.isPresent()) {
            RepairReport existing = existingReport.get();
            existing.setStartTime(repairReport.getStartTime());
            existing.setEndTime(repairReport.getEndTime());
            existing.setFinishedDate(repairReport.getFinishedDate());
            existing.setTotalCost(repairReport.getTotalCost());
            existing.setInformation(repairReport.getInformation());
            existing.setPartsUsed(repairReport.getPartsUsed());
            existing.setTechnicians(repairReport.getTechnicians());
            existing.setRepairID(repairReport.getRepairID());
            return repairReportRepository.save(existing);  // Save updated report
        } else {
            return null;  // Return null if the report is not found
        }
    }

    @Override
    public void deleteRepairReport(String repairReportID) {
        repairReportRepository.deleteById(repairReportID);  // Delete report by ID
    }
}
