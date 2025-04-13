package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenanceReport;
import bt.nhdcl.maintenancemicroservice.entity.RepairReport;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceReportRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PreventiveMaintenanceReportServiceImpl implements PreventiveMaintenanceReportService {

    private final PreventiveMaintenanceReportRepository repository;

    public PreventiveMaintenanceReportServiceImpl(PreventiveMaintenanceReportRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    @Override
    public PreventiveMaintenanceReport createReport(PreventiveMaintenanceReport report, List<MultipartFile> imageFiles) {
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
