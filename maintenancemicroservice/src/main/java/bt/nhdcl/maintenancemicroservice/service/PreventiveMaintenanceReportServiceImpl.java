package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceReportRepository;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;
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
public class PreventiveMaintenanceReportServiceImpl implements PreventiveMaintenanceReportService {

    private final PreventiveMaintenanceReportRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PreventiveMaintenanceRepository maintenanceRepository;

    public PreventiveMaintenanceReportServiceImpl(PreventiveMaintenanceReportRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public PreventiveMaintenanceReport createReport(PreventiveMaintenanceReport report,
            List<MultipartFile> imageFiles) {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imageUrls = uploadImages(imageFiles);
            report.setImages(imageUrls);
        }
        // 2. Save the report
        PreventiveMaintenanceReport savedReport = repository.save(report);

        // 3. Find the related maintenance record
        String maintenanceId = report.getPreventiveMaintenanceID();
        Optional<PreventiveMaintenance> maintenanceOpt = maintenanceRepository.findById(maintenanceId);

        if (maintenanceOpt.isPresent()) {
            PreventiveMaintenance saved = maintenanceOpt.get();

            // Update asset status via ASSETMICROSERVICE
            if (saved.getAssetCode() != null) {
                String assetServiceUrl = "http://ASSETMICROSERVICE/api/assets/update-status";

                Map<String, String> request = new HashMap<>();
                request.put("assetCode", saved.getAssetCode());
                request.put("status", "In Usage"); // or "Completed" based on your logic

                try {
                    ResponseEntity<Void> response = restTemplate.postForEntity(assetServiceUrl, request, Void.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        System.out.println("✅ Asset status updated successfully.");
                    } else {
                        System.err
                                .println("⚠️ Asset status update failed with status code: " + response.getStatusCode());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to update asset status: " + e.getMessage());
                }
            }

        } else {
            throw new RuntimeException(
                    "Preventive Maintenance with ID " + report.getPreventiveMaintenanceID() + " not found.");
        }

        return savedReport;
    }

    @Override
    public PreventiveMaintenanceReport updateEndTime(String reportID, LocalTime endTime, LocalDate finishedDate) {
        Optional<PreventiveMaintenanceReport> optional = repository.findById(reportID);
        if (optional.isEmpty())
            return null;

        PreventiveMaintenanceReport existing = optional.get();

        if (endTime != null) {
            existing.setEndTime(endTime);
        }
        if (finishedDate != null) {
            existing.setFinishedDate(finishedDate);
        }

        return repository.save(existing);
    }

    // @Override
    // public PreventiveMaintenanceReport updateEndTime(String reportID, LocalTime
    // endTime) {
    // Optional<PreventiveMaintenanceReport> optional =
    // repository.findById(reportID);
    // if (optional.isEmpty())
    // return null;

    // PreventiveMaintenanceReport existing = optional.get();
    // existing.setEndTime(endTime);
    // return repository.save(existing);
    // }

    @Override
    public PreventiveMaintenanceReport updateReport(String reportID, PreventiveMaintenanceReport updatedData,
            List<MultipartFile> imageFiles) {
        Optional<PreventiveMaintenanceReport> optional = repository.findById(reportID);
        if (optional.isEmpty())
            return null;

        PreventiveMaintenanceReport existing = optional.get();

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

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> newImageUrls = uploadImages(imageFiles);
            List<String> existingImages = existing.getImages() != null ? existing.getImages() : new ArrayList<>();
            existingImages.addAll(newImageUrls);
            existing.setImages(existingImages);
        }

        return repository.save(existing);
    }

    @SuppressWarnings("unchecked")
    private List<String> uploadImages(List<MultipartFile> imageFiles) {
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
        return imageUrls;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PreventiveMaintenanceReport create(PreventiveMaintenanceReport report, List<MultipartFile> imageFiles) {
        List<String> imageUrls = new ArrayList<>();

        if (imageFiles != null) {
            for (MultipartFile image : imageFiles) {
                try {
                    // Properly parameterizing the Map type
                    Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(),
                            ObjectUtils.emptyMap());
                    imageUrls.add((String) uploadResult.get("secure_url"));
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + e.getMessage());
                }
            }
        }

        report.setImages(imageUrls);
        return repository.save(report);
    }

    @Override
    public List<PreventiveMaintenanceReport> getAllReports() {
        return repository.findAll();
    }

    @Override
    public Optional<PreventiveMaintenanceReport> getReportById(String id) {
        return repository.findById(id);
    }

    public List<PreventiveMaintenanceReport> getReportsByPreventiveMaintenanceID(String preventiveMaintenanceID) {
        return repository.findByPreventiveMaintenanceID(preventiveMaintenanceID);
    }

    @Override
    public PreventiveMaintenanceReport updateReport(String id, PreventiveMaintenanceReport updatedReport) {
        if (repository.existsById(id)) {
            updatedReport.setMaintenanceReportID(id);
            return repository.save(updatedReport);
        }
        return null; // Handle exception in real implementation
    }

    @Override
    public void deleteReport(String id) {
        repository.deleteById(id);
    }
}
