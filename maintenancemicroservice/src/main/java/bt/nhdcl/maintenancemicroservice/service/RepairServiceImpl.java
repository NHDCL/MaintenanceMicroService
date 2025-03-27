package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.Repair;
import bt.nhdcl.maintenancemicroservice.repository.RepairRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;

import java.io.IOException;
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

    @Override
    public Repair createRepair(Repair repair, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload image to Cloudinary
            try {
                Map<String, String> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url"); // Get the URL of the uploaded image
                repair.setImage(imageUrl); // Set the image URL in the Repair entity
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception appropriately (log, rethrow, etc.)
            }
        }
        return repairRepository.save(repair); // Save the repair with the image URL
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
            repair.setRepairID(repairID);  // Ensure the repair ID is set
            return repairRepository.save(repair);
        }
        return null;  // Or handle it based on your use case
    }

    @Override
    public void deleteRepair(String repairID) {
        repairRepository.deleteById(repairID);
    }
}
