package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Schedule;
import bt.nhdcl.maintenancemicroservice.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Optional<Schedule> getScheduleById(String scheduleID) {
        return scheduleRepository.findById(scheduleID);
    }

    @Override
    public List<Schedule> getSchedulesByUserID(String userID) {
        return scheduleRepository.findByUserID(userID);
    }

    @Override
    public List<Schedule> getSchedulesByRepairID(String repairID) {
        return scheduleRepository.findByRepairID(repairID);
    }

    @Override
    public Schedule updateSchedule(String scheduleID, Schedule updatedSchedule) {
        return scheduleRepository.findById(scheduleID)
                .map(schedule -> {
                    if (updatedSchedule.getStartTime() != null)
                        schedule.setStartTime(updatedSchedule.getStartTime());

                    if (updatedSchedule.getReportingDate() != null)
                        schedule.setReportingDate(updatedSchedule.getReportingDate());

                    if (updatedSchedule.getAddCost() != 0)
                        schedule.setAddCost(updatedSchedule.getAddCost());

                    if (updatedSchedule.getAddHours() != 0)
                        schedule.setAddHours(updatedSchedule.getAddHours());

                    if (updatedSchedule.getRemark() != null)
                        schedule.setRemark(updatedSchedule.getRemark());

                    if (updatedSchedule.getTeamMember() != null)
                        schedule.setTeamMember(updatedSchedule.getTeamMember());

                    if (updatedSchedule.getUserID() != null)
                        schedule.setUserID(updatedSchedule.getUserID());

                    if (updatedSchedule.getRepairID() != null)
                        schedule.setRepairID(updatedSchedule.getRepairID());

                    return scheduleRepository.save(schedule);
                })
                .orElseThrow(() -> new RuntimeException("Schedule not found with ID: " + scheduleID));
    }

    @Override
    public void deleteSchedule(String scheduleID) {
        scheduleRepository.deleteById(scheduleID);
    }
}
