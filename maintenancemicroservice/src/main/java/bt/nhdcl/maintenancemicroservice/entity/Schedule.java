package bt.nhdcl.maintenancemicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "Schedule") // MongoDB collection name
public class Schedule {

    @Id
    private String scheduleID; // MongoDB uses String for `_id` field

    private LocalTime startTime;
    private LocalDate reportingDate;
    private int addCost;
    private int addHours;
    private String remark;
    private String userID;
    private String repairID;
    private String technicianEmail;

    // Default Constructor
    public Schedule() {}

    // Parameterized Constructor
    public Schedule(String scheduleID, LocalTime startTime, LocalDate reportingDate, 
                    int addCost, int addHours, String remark,
                    String userID, String repairID, String technicianEmail) {
        this.scheduleID = scheduleID;
        this.startTime = startTime;
        this.reportingDate = reportingDate;
        this.addCost = addCost;
        this.addHours = addHours;
        this.remark = remark;
        this.userID = userID;
        this.repairID = repairID;
        this.technicianEmail = technicianEmail;
    }

    // Getters and Setters
    public String getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(String scheduleID) {
        this.scheduleID = scheduleID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public int getAddCost() {
        return addCost;
    }

    public void setAddCost(int addCost) {
        this.addCost = addCost;
    }

    public int getAddHours() {
        return addHours;
    }

    public void setAddHours(int addHours) {
        this.addHours = addHours;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRepairID() {
        return repairID;
    }

    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public String getTechnicianEmail() {
        return technicianEmail;
    }

    public void setTechnicianEmail(String technicianEmail) {
        this.technicianEmail = technicianEmail;
    }
}
