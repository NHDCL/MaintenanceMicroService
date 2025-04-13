package bt.nhdcl.maintenancemicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "repair_report") // MongoDB collection name
public class RepairReport {

    @Id
    private String repairReportID; // MongoDB uses String for `_id` field

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate finishedDate;
    private int totalCost;
    private String information;
    private String partsUsed;
    private String technicians;
    private String repairID;
    private List<String> images;

    // Default Constructor
    public RepairReport() {}

    // Parameterized Constructor
    public RepairReport(String repairReportID, LocalTime startTime, LocalTime endTime, 
                             LocalDate finishedDate, int totalCost, String information, 
                             String partsUsed, String technicians, String repairID, List<String> images) {
        this.repairReportID = repairReportID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finishedDate = finishedDate;
        this.totalCost = totalCost;
        this.information = information;
        this.partsUsed = partsUsed;
        this.technicians = technicians;
        this.repairID = repairID;
        this.images = images;
    }

    // Getters and Setters
    public String getRepairReportID() {
        return repairReportID;
    }

    public void setRepairReportID(String repairReportID) {
        this.repairReportID = repairReportID;
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

    public String getRepairID() {
        return repairID;
    }

    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
}

