package bt.nhdcl.maintenancemicroservice.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "maintenance")
public class PreventiveMaintenance {

    @Id
    private String maintenanceID;
    private LocalTime timeStart;
    private LocalDate startDate;
    private int addCost;
    private int addHours;
    private String remark;
    private String status;
    private boolean scheduled;
    private String description;
    private String assignedSupervisors;
    private LocalDate endDate;
    private String repeat;
    private int userID;
    private String assetCode;

    // Constructors
    public PreventiveMaintenance() {}

    public PreventiveMaintenance(String maintenanceID, LocalTime timeStart, LocalDate startDate, int addCost, int addHours,
                       String remark, String status, boolean scheduled, String description, String assignedSupervisors,
                       LocalDate endDate, String repeat, int userID, String assetCode) {
        this.maintenanceID = maintenanceID;
        this.timeStart = timeStart;
        this.startDate = startDate;
        this.addCost = addCost;
        this.addHours = addHours;
        this.remark = remark;
        this.status = status;
        this.scheduled = scheduled;
        this.description = description;
        this.assignedSupervisors = assignedSupervisors;
        this.endDate = endDate;
        this.repeat = repeat;
        this.userID = userID;
        this.assetCode = assetCode;
    }

    // Getters and Setters
    public String getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(String maintenanceID) {
        this.maintenanceID = maintenanceID;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedSupervisors() {
        return assignedSupervisors;
    }

    public void setAssignedSupervisors(String assignedSupervisors) {
        this.assignedSupervisors = assignedSupervisors;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
}

