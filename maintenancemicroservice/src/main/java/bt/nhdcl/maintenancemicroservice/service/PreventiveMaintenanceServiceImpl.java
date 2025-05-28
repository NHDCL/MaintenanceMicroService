package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PreventiveMaintenanceServiceImpl implements PreventiveMaintenanceService {

    private final PreventiveMaintenanceRepository maintenanceRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        PreventiveMaintenance saved = maintenanceRepository.save(maintenance);
        if (saved.getAssetCode() != null) {
            // Correct endpoint and method
            String assetServiceUrl = "http://ASSETMICROSERVICE/api/assets/update-status";

            // Create the request body as a Map (since you're not using DTO)
            Map<String, String> request = new HashMap<>();
            request.put("assetCode", saved.getAssetCode());
            request.put("status", "In Maintenance");

            try {
                restTemplate.postForEntity(assetServiceUrl, request, Void.class);
            } catch (Exception e) {
                System.err.println("Failed to update asset status: " + e.getMessage());
            }
        }

        return saved;
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

    @Scheduled(cron = "0 0 8 * * *")
    public void generateRepeatingMaintenance() {
        LocalDate today = LocalDate.now();
        List<PreventiveMaintenance> maintenanceList = maintenanceRepository.findAll();

        for (PreventiveMaintenance maintenance : maintenanceList) {
            String repeatType = maintenance.getRepeat();

            // Skip if no repeat or invalid
            if (repeatType == null || repeatType.equalsIgnoreCase("none")) {
                continue;
            }

            // Skip if endDate passed
            if (maintenance.getEndDate() != null && today.isAfter(maintenance.getEndDate())) {
                continue;
            }

            // Get the next scheduled start date
            LocalDate nextStartDate = getNextStartDate(maintenance.getStartDate(), repeatType);

            // Get the lead time (how many days before nextStartDate we should generate the
            // task)
            int leadTime = getLeadTimeDays(repeatType);

            // Check if today is the right day to generate the new maintenance
            if (today.isEqual(nextStartDate.minusDays(leadTime))) {
                boolean exists = maintenanceRepository.existsByStartDateAndAssetCode(nextStartDate,
                        maintenance.getAssetCode());
                if (exists) {
                    continue; // Skip generation to avoid duplication
                }
                PreventiveMaintenance newMaintenance = new PreventiveMaintenance();

                newMaintenance.setTimeStart(maintenance.getTimeStart());
                newMaintenance.setStartDate(nextStartDate);
                newMaintenance.setAddCost(maintenance.getAddCost());
                newMaintenance.setAddHours(maintenance.getAddHours());
                newMaintenance.setRemark(maintenance.getRemark());
                newMaintenance.setStatus("Pending");
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

    private LocalDate getNextStartDate(LocalDate startDate, String repeatType) {
        LocalDate nextDate = startDate;
        LocalDate today = LocalDate.now();

        while (nextDate.isBefore(today)) {
            switch (repeatType.toLowerCase()) {
                case "daily":
                    nextDate = nextDate.plusDays(1);
                    break;
                case "weekly":
                    nextDate = nextDate.plusWeeks(1);
                    break;
                case "monthly":
                    nextDate = nextDate.plusMonths(1);
                    break;
                case "yearly":
                    nextDate = nextDate.plusYears(1);
                    break;
                default:
                    return startDate;
            }
        }
        return nextDate;
    }

    private int getLeadTimeDays(String repeatType) {
        switch (repeatType.toLowerCase()) {
            case "daily":
                return 0; // generate on the same day
            case "weekly":
                return 3;
            case "monthly":
                return 3;
            case "yearly":
                return 3;
            default:
                return 0;
        }
    }

}
