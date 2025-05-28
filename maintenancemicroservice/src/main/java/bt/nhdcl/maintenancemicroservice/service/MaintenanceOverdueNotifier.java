package bt.nhdcl.maintenancemicroservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceReportRepository;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;

import java.time.LocalDate;
import java.util.List;


@Component
public class MaintenanceOverdueNotifier {

    @Autowired
    private PreventiveMaintenanceRepository preventiveRepo;

    @Autowired
    private PreventiveMaintenanceReportRepository reportRepo;

    @Autowired
    private EmailService emailService;

    // @Scheduled(cron = "0 30 8 * * ?") // Runs daily at 8:30 AM
    @Scheduled(cron = "0 26 23 * * *")
    public void notifyOverdueTechnicians() {
        List<PreventiveMaintenance> allMaintenances = preventiveRepo.findAll();

        for (PreventiveMaintenance pm : allMaintenances) {
            if (pm.getStartDate() == null || pm.getMaintenanceID() == null) continue;

            boolean reportExists = reportRepo.existsByPreventiveMaintenanceID(pm.getMaintenanceID());

            if (!reportExists && LocalDate.now().isAfter(pm.getStartDate().plusDays(7))) {
                String technicianEmail = pm.getTechnicianEmail();
                if (technicianEmail != null && !technicianEmail.isBlank()) {
                    emailService.sendEmail(
                        technicianEmail,
                        "Overdue Maintenance Alert",
                        "The maintenance task with ID " + pm.getMaintenanceID() +
                        " is overdue. It was scheduled to start on " + pm.getStartDate() +
                        ". Please complete and submit your maintenance report as soon as possible."
                    );
                }
            }
        }
    }
}

