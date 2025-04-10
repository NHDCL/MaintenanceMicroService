package bt.nhdcl.maintenancemicroservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "maintenance")
public class Repair {

    @Id
    private String repairID;
    private String name;
    private String phoneNumber;
    private String email;
    private String priority;
    private String area;
    private List<String> images;
    private String description;
    private String assetName;
    private LocalDate submissionDate;  // Automatically generated
    private boolean scheduled;
    private String assetCode;

    // Constructors
    public Repair() {
        // Automatically generate the submission date
        this.submissionDate = LocalDate.now();
    }

    public Repair(String repairID, String name, String phoneNumber, String email, String priority, String area,
    List<String> images, String description, String assetName, boolean scheduled, String assetCode) {
        this.repairID = repairID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.priority = priority;
        this.area = area;
        this.images = images;
        this.description = description;
        this.assetName = assetName;
        this.submissionDate = LocalDate.now();  // Automatically generate the submission date
        this.scheduled = scheduled;
        this.assetCode = assetCode;
    }

    // Getters and Setters
    public String getRepairID() {
        return repairID;
    }

    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
}
