package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Schedule;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    Schedule createSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    Optional<Schedule> getScheduleById(String scheduleID);
    List<Schedule> getSchedulesByUserID(String userID);
    List<Schedule> getSchedulesByRepairID(String repairID);
    Schedule updateSchedule(String scheduleID, Schedule schedule);
    void deleteSchedule(String scheduleID);
    List<Schedule> getSchedulesByTechnicianEmail(String email);
}
