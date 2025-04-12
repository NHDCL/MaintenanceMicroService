package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.repository.RepairRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public RepairServiceImpl(RepairRepository repairRepository, Cloudinary cloudinary) {
        this.repairRepository = repairRepository;
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Repair createRepair(Repair repair, MultipartFile[] imageFiles) {
        List<String> imageUrls = new ArrayList<>();

        if (imageFiles != null && imageFiles.length > 0) {
            for (MultipartFile file : imageFiles) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                                ObjectUtils.emptyMap());
                        String imageUrl = uploadResult.get("url");
                        imageUrls.add(imageUrl); // Add each uploaded image URL to the list
                    } catch (IOException e) {
                        e.printStackTrace(); // Log or handle the exception
                    }
                }
            }
        }

        repair.setImages(imageUrls); // Set the list of image URLs in the Repair entity
        return repairRepository.save(repair);
    }

    @Override
    public List<Repair> getAllRepairs() {
        return repairRepository.findAll();
    }

    @Override
    public Optional<Repair> getRepairById(String repairID) {
        return repairRepository.findById(repairID);
    }

    @Override
    public Repair updateRepair(String repairID, Repair repair) {
        if (repairRepository.existsById(repairID)) {
            repair.setRepairID(repairID); // Ensure the repair ID is set
            return repairRepository.save(repair);
        }
        return null; // Or handle it based on your use case
    }

    @Override
    public void deleteRepair(String repairID) {
        repairRepository.deleteById(repairID);
    }
}
