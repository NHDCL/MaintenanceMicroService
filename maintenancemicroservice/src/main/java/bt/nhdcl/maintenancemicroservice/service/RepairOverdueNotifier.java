package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Schedule;
import bt.nhdcl.maintenancemicroservice.repository.ScheduleRepository;
import bt.nhdcl.maintenancemicroservice.repository.RepairReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RepairOverdueNotifier {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RepairReportRepository repairReportRepository;

    @Autowired
    private EmailService emailService;

    // Runs every day at 9:15 AM
    @Scheduled(cron = "0 15 9 * * ?")
    public void notifyOverdueRepairs() {
        List<Schedule> allSchedules = scheduleRepository.findAll();

        for (Schedule schedule : allSchedules) {
            if (schedule.getReportingDate() == null || schedule.getRepairID() == null) continue;

            boolean reportExists = repairReportRepository.existsByRepairID(schedule.getRepairID());

            if (!reportExists && LocalDate.now().isAfter(schedule.getReportingDate().plusDays(7))) {
                String technicianEmail = schedule.getTechnicianEmail();
                if (technicianEmail != null && !technicianEmail.isBlank()) {
                    emailService.sendEmail(
                        technicianEmail,
                        "Overdue Repair Alert",
                        "The repair with ID " + schedule.getRepairID() +
                        " is overdue. It was scheduled for " + schedule.getReportingDate() +
                        ". Please complete and submit your repair report as soon as possible."
                    );
                }
            }
        }
    }
}

