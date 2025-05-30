package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.RepairReportRepository;
import bt.nhdcl.maintenancemicroservice.repository.RepairRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RepairReportServiceImpl implements RepairReportService {

    private final RepairReportRepository repairReportRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RepairRepository repairRepository;

    public RepairReportServiceImpl(RepairReportRepository repairReportRepository) {
        this.repairReportRepository = repairReportRepository;
    }

    @Override
    public RepairReport createReport(RepairReport report) {
        RepairReport savedReport = repairReportRepository.save(report);

        // Now try to fetch the Repair by its ID from the report
        Optional<Repair> optionalRepair = repairRepository.findById(report.getRepairID());

        if (optionalRepair.isPresent()) {
            Repair saved = optionalRepair.get();

            if (saved.getAssetCode() != null && !saved.getAssetCode().trim().isEmpty()) {
                String assetServiceUrl = "http://ASSETMICROSERVICE/api/assets/update-status";

                Map<String, String> request = new HashMap<>();
                request.put("assetCode", saved.getAssetCode());
                request.put("status", "In Usage"); // Or "Completed" depending on your business logic

                try {
                    ResponseEntity<Void> response = restTemplate.postForEntity(assetServiceUrl, request, Void.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        System.out.println("✅ Asset status updated successfully.");
                    } else {
                        System.err
                                .println("⚠️ Asset status update failed with status code: " + response.getStatusCode());
                    }
                } catch (Exception e) {
                    System.err.println("❌ Failed to update asset status: " + e.getMessage());
                }
            }
        } else {
            System.err.println("❌ Repair not found with ID: " + report.getRepairID());
        }

        return savedReport;

    }

    // @Override
    // public RepairReport updateEndTime(String reportID, LocalTime endTime) {
    // Optional<RepairReport> optional = repairReportRepository.findById(reportID);
    // if (optional.isEmpty())
    // return null;

    // RepairReport existing = optional.get();
    // existing.setEndTime(endTime);
    // return repairReportRepository.save(existing);
    // }
    @Override
    public RepairReport updateEndTime(String reportID, LocalTime endTime, LocalDate finishedDate) {
        Optional<RepairReport> optional = repairReportRepository.findById(reportID);
        if (optional.isEmpty())
            return null;

        RepairReport existing = optional.get();

        if (endTime != null) {
            existing.setEndTime(endTime);
        }
        if (finishedDate != null) {
            existing.setFinishedDate(finishedDate);
        }

        return repairReportRepository.save(existing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RepairReport updateReport(String reportID, RepairReport updatedData, List<MultipartFile> imageFiles) {
        Optional<RepairReport> optional = repairReportRepository.findById(reportID);
        if (optional.isEmpty()) {
            return null;
        }

        RepairReport existing = optional.get();

        // if (updatedData.getFinishedDate() != null) {
        //     existing.setFinishedDate(updatedData.getFinishedDate());
        // }
        if (updatedData.getTotalCost() != 0) {
            existing.setTotalCost(updatedData.getTotalCost());
        }
        if (updatedData.getInformation() != null) {
            existing.setInformation(updatedData.getInformation());
        }
        if (updatedData.getPartsUsed() != null) {
            existing.setPartsUsed(updatedData.getPartsUsed());
        }
        if (updatedData.getTechnicians() != null) {
            existing.setTechnicians(updatedData.getTechnicians());
        }

        if (imageFiles != null) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : imageFiles) {
                try {
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(),
                            ObjectUtils.emptyMap());
                    imageUrls.add((String) uploadResult.get("secure_url"));
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + e.getMessage());
                }
            }
            List<String> existingImages = existing.getImages() != null ? existing.getImages() : new ArrayList<>();
            existingImages.addAll(imageUrls);
            existing.setImages(existingImages);
        }

        return repairReportRepository.save(existing);
    }

    @Override
    public List<RepairReport> getAllRepairReports() {
        return repairReportRepository.findAll(); // Get all repair reports
    }

    @Override
    public Optional<RepairReport> getRepairReportById(String repairReportID) {
        return repairReportRepository.findById(repairReportID); // Get repair report by ID
    }

    @Override
    public List<RepairReport> getRepairReportsByRepairID(String repairID) {
        return repairReportRepository.findByRepairID(repairID); // Get repair reports by repairID
    }

    @Override
    public RepairReport updateRepairReport(String repairReportID, RepairReport repairReport) {
        Optional<RepairReport> existingReport = repairReportRepository.findById(repairReportID);

        if (existingReport.isPresent()) {
            RepairReport existing = existingReport.get();
            existing.setStartTime(repairReport.getStartTime());
            existing.setEndTime(repairReport.getEndTime());
            existing.setFinishedDate(repairReport.getFinishedDate());
            existing.setTotalCost(repairReport.getTotalCost());
            existing.setInformation(repairReport.getInformation());
            existing.setPartsUsed(repairReport.getPartsUsed());
            existing.setTechnicians(repairReport.getTechnicians());
            existing.setRepairID(repairReport.getRepairID());
            return repairReportRepository.save(existing); // Save updated report
        } else {
            return null; // Return null if the report is not found
        }
    }

    @Override
    public void deleteRepairReport(String repairReportID) {
        repairReportRepository.deleteById(repairReportID); // Delete report by ID
    }
}
