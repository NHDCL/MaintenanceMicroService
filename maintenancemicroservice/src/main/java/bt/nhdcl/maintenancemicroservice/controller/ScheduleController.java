package bt.nhdcl.maintenancemicroservice.controller;

import bt.nhdcl.maintenancemicroservice.entity.Schedule;
import bt.nhdcl.maintenancemicroservice.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")

public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // Create a new schedule
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }

    // Get all schedules
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    // Get schedule by ID
    @GetMapping("/{scheduleID}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable String scheduleID) {
        Optional<Schedule> schedule = scheduleService.getScheduleById(scheduleID);
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get schedules by User ID
    @GetMapping("/user/{userID}")
    public ResponseEntity<List<Schedule>> getSchedulesByUserID(@PathVariable String userID) {
        return ResponseEntity.ok(scheduleService.getSchedulesByUserID(userID));
    }

    // Get schedules by Repair ID
    @GetMapping("/repair/{repairID}")
    public ResponseEntity<List<Schedule>> getSchedulesByRepairID(@PathVariable String repairID) {
        return ResponseEntity.ok(scheduleService.getSchedulesByRepairID(repairID));
    }

    // Update a schedule
    @PutMapping("/{scheduleID}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable String scheduleID,
            @RequestBody Schedule updatedSchedule) {
        try {
            Schedule schedule = scheduleService.updateSchedule(scheduleID, updatedSchedule);
            return ResponseEntity.ok(schedule);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a schedule
    @DeleteMapping("/{scheduleID}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleID) {
        scheduleService.deleteSchedule(scheduleID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/technician/{email}")
    public List<Schedule> getSchedulesByTechnicianEmail(@PathVariable String email) {
        return scheduleService.getSchedulesByTechnicianEmail(email);
    }
}
