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
                    schedule.setStartTime(updatedSchedule.getStartTime());
                    schedule.setReportingDate(updatedSchedule.getReportingDate());
                    schedule.setAddCost(updatedSchedule.getAddCost());
                    schedule.setAddHours(updatedSchedule.getAddHours());
                    schedule.setRemark(updatedSchedule.getRemark());
                    schedule.setTeamMember(updatedSchedule.getTeamMember());
                    schedule.setUserID(updatedSchedule.getUserID());
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
