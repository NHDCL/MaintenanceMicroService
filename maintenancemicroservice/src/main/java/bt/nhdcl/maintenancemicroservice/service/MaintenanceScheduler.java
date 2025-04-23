package bt.nhdcl.maintenancemicroservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceScheduler {

    private final PreventiveMaintenanceRepository repository;
    private final EmailService mailService;

    public MaintenanceScheduler(PreventiveMaintenanceRepository repository, EmailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyTechnicians() {
        LocalDate today = LocalDate.now();
        LocalDate notifyDate = today.plusDays(7);

        List<PreventiveMaintenance> upcoming = repository.findByStartDateAndStatus(notifyDate, "Pending");

        for (PreventiveMaintenance maintenance : upcoming) {
            String email = maintenance.getTechnicianEmail();
            String subject = "Upcoming Preventive Maintenance Alert";
            String message = String.format(
                "Dear Technician,\n\nThis is a reminder that maintenance for an asset is scheduled on %s at %s.\n\nDescription: %s\n\nPlease ensure the task is completed as early as possible. Your effort and timely response are highly appreciated.\n\nRegards,\nNHDCL",
                maintenance.getStartDate(),
                maintenance.getTimeStart(),
                maintenance.getDescription()
            );
            mailService.sendEmail(email, subject, message);
        }
    }
}

