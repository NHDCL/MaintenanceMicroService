package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PreventiveMaintenanceServiceImpl implements PreventiveMaintenanceService {

    private final PreventiveMaintenanceRepository maintenanceRepository;

    @Autowired
    private EmailService emailService;

    public PreventiveMaintenanceServiceImpl(
            PreventiveMaintenanceRepository maintenanceRepository,
            EmailService emailService) {
        this.maintenanceRepository = maintenanceRepository;
        this.emailService = emailService;
    }

    @Override
    public PreventiveMaintenance saveMaintenance(PreventiveMaintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public List<PreventiveMaintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Optional<PreventiveMaintenance> getMaintenanceById(String maintenanceID) {
        return maintenanceRepository.findById(maintenanceID);
    }

    @Override
    public void deleteMaintenance(String maintenanceID) {
        maintenanceRepository.deleteById(maintenanceID);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByStatus(String status) {
        return maintenanceRepository.findByStatus(status);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByStartDate(LocalDate startDate) {
        return maintenanceRepository.findByStartDate(startDate);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByAssetCode(String assetCode) {
        return maintenanceRepository.findByAssetCode(assetCode);
    }

    @Override
    public PreventiveMaintenance update(String maintenanceID, PreventiveMaintenance updatedMaintenance) {
        PreventiveMaintenance existing = maintenanceRepository.findById(maintenanceID)
                .orElseThrow(() -> new RuntimeException("Maintenance not found with ID: " + maintenanceID));

        boolean technicianEmailUpdated = false;

        // Conditionally update only non-null or valid fields
        if (updatedMaintenance.getTimeStart() != null) {
            existing.setTimeStart(updatedMaintenance.getTimeStart());
        }
        if (updatedMaintenance.getStartDate() != null) {
            existing.setStartDate(updatedMaintenance.getStartDate());
        }
        if (updatedMaintenance.getAddCost() != 0) {
            existing.setAddCost(updatedMaintenance.getAddCost());
        }
        if (updatedMaintenance.getAddHours() != 0) {
            existing.setAddHours(updatedMaintenance.getAddHours());
        }
        if (updatedMaintenance.getRemark() != null) {
            existing.setRemark(updatedMaintenance.getRemark());
        }
        if (updatedMaintenance.getStatus() != null) {
            existing.setStatus(updatedMaintenance.getStatus());
        }
        if (updatedMaintenance.getDescription() != null) {
            existing.setDescription(updatedMaintenance.getDescription());
        }
        if (updatedMaintenance.getTechnicianEmail() != null) {
            String newEmail = updatedMaintenance.getTechnicianEmail();
            String oldEmail = existing.getTechnicianEmail();

            // Check if email was newly set or changed
            if (!newEmail.equals(oldEmail)) {
                existing.setTechnicianEmail(newEmail);
                technicianEmailUpdated = true;
            }
        }
        if (updatedMaintenance.getEndDate() != null) {
            existing.setEndDate(updatedMaintenance.getEndDate());
        }
        if (updatedMaintenance.getRepeat() != null) {
            existing.setRepeat(updatedMaintenance.getRepeat());
        }
        if (updatedMaintenance.getUserID() != null) {
            existing.setUserID(updatedMaintenance.getUserID());
        }
        if (updatedMaintenance.getAssetCode() != null) {
            existing.setAssetCode(updatedMaintenance.getAssetCode());
        }
        if (updatedMaintenance.getAcademyId() != null) {
            existing.setAcademyId(updatedMaintenance.getAcademyId());
        }

        PreventiveMaintenance saved = maintenanceRepository.save(existing);

        // Send email if technician email was updated
        if (technicianEmailUpdated) {
            String subject = "Preventive Maintenance Task Assigned";
            String body = "Dear Technician,\n\n"
                    + "You have been assigned a new preventive maintenance task. "
                    + ".\n\n"
                    + "Please complete it promptly.\n\n"
                    + "Regards,\nNHDCL";

            emailService.sendEmail(existing.getTechnicianEmail(), subject, body);
        }

        return saved;
    }

    public void sendEmail(String to) {
        String subject = "Preventive Maintenance Task Assigned";
        String body = "Dear Supervisor,\n\n"
                + "You have been assigned a new preventive maintenance task.\n\n"
                + "Please complete it promptly.\n\n"
                + "Regards,\nNHDCL";

        emailService.sendEmail(to, subject, body);
    }

    public List<PreventiveMaintenance> getByUserID(String userID) {
        return maintenanceRepository.findByUserID(userID);
    }

    @Override
    public List<PreventiveMaintenance> getByTechnicianEmail(String email) {
        return maintenanceRepository.findByTechnicianEmail(email);
    }

    public void generateRepeatingMaintenance() {
        LocalDate today = LocalDate.now();

        List<PreventiveMaintenance> maintenanceList = maintenanceRepository.findAll();

        for (PreventiveMaintenance maintenance : maintenanceList) {
            // Skip if no repeat
            if (maintenance.getRepeat() == null || maintenance.getRepeat().equalsIgnoreCase("none")) {
                continue;
            }

            // Skip if endDate passed
            if (maintenance.getEndDate() != null && today.isAfter(maintenance.getEndDate())) {
                continue;
            }

            LocalDate nextStartDate = getNextStartDate(maintenance.getStartDate(), maintenance.getRepeat());

            // 🧠 Check if today is 3 days before the nextStartDate
            if (today.isEqual(nextStartDate.minusDays(7))) {
                PreventiveMaintenance newMaintenance = new PreventiveMaintenance();
                newMaintenance.setTimeStart(maintenance.getTimeStart());
                newMaintenance.setStartDate(nextStartDate); // Real start date
                newMaintenance.setAddCost(maintenance.getAddCost());
                newMaintenance.setAddHours(maintenance.getAddHours());
                newMaintenance.setRemark(maintenance.getRemark());
                newMaintenance.setStatus("Pending"); // You can customize
                newMaintenance.setDescription(maintenance.getDescription());
                newMaintenance.setEndDate(maintenance.getEndDate());
                newMaintenance.setRepeat(maintenance.getRepeat());
                newMaintenance.setUserID(maintenance.getUserID());
                newMaintenance.setTechnicianEmail(maintenance.getTechnicianEmail());
                newMaintenance.setAssetCode(maintenance.getAssetCode());
                newMaintenance.setAcademyId(maintenance.getAcademyId());

                maintenanceRepository.save(newMaintenance);

                System.out.println("Generated preventive maintenance for: " + nextStartDate);
            }
        }
    }

    private LocalDate getNextStartDate(LocalDate currentStartDate, String repeatType) {
        switch (repeatType.toLowerCase()) {
            case "daily":
                return currentStartDate.plus(1, ChronoUnit.DAYS);
            case "weekly":
                return currentStartDate.plus(1, ChronoUnit.WEEKS);
            case "monthly":
                return currentStartDate.plus(1, ChronoUnit.MONTHS);
            case "yearly":
                return currentStartDate.plus(1, ChronoUnit.YEARS);
            default:
                return currentStartDate; // no repeat
        }
    }
}
