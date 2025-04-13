package bt.nhdcl.maintenancemicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "maintenance_report") // MongoDB collection name
public class PreventiveMaintenanceReport {

    @Id
    private String maintenanceReportID; // MongoDB uses String for `_id` field

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate finishedDate;
    private int totalCost;
    private String information;
    private String partsUsed;
    private String technicians;
    private String preventiveMaintenanceID;
    private List<String> images;

    // Default Constructor
    public PreventiveMaintenanceReport() {}

    // Parameterized Constructor
    public PreventiveMaintenanceReport(String maintenanceReportID, LocalTime startTime, LocalTime endTime, 
                             LocalDate finishedDate, int totalCost, String information, 
                             String partsUsed, String technicians, String preventiveMaintenanceID, List<String> images) {
        this.maintenanceReportID = maintenanceReportID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finishedDate = finishedDate;
        this.totalCost = totalCost;
        this.information = information;
        this.partsUsed = partsUsed;
        this.technicians = technicians;
        this.preventiveMaintenanceID = preventiveMaintenanceID;
        this.images = images;
    }

    // Getters and Setters
    public String getMaintenanceReportID() {
        return maintenanceReportID;
    }

    public void setMaintenanceReportID(String maintenanceReportID) {
        this.maintenanceReportID = maintenanceReportID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        this.finishedDate = finishedDate;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPartsUsed() {
        return partsUsed;
    }

    public void setPartsUsed(String partsUsed) {
        this.partsUsed = partsUsed;
    }

    public String getTechnicians() {
        return technicians;
    }

    public void setTechnicians(String technicians) {
        this.technicians = technicians;
    }

    public String getPreventiveMaintenanceID() {
        return preventiveMaintenanceID;
    }

    public void setPreventiveMaintenanceID(String preventiveMaintenanceID) {
        this.preventiveMaintenanceID = preventiveMaintenanceID;
    }
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
}

