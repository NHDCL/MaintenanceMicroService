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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final Cloudinary cloudinary;

    @Autowired
    private EmailService emailService;

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

    public Map<String, Object> updateRepair(String repairID, Map<String, Object> updateFields) {
        Optional<Repair> optionalRepair = repairRepository.findById(repairID);

        Map<String, Object> response = new HashMap<>();

        if (!optionalRepair.isPresent()) {
            response.put("status", "error");
            response.put("message", "Repair with ID " + repairID + " not found.");
            return response;
        }

        Repair repair = optionalRepair.get();

        // Update the fields based on the incoming updateFields map
        if (updateFields.containsKey("name")) {
            repair.setName((String) updateFields.get("name"));
        }
        if (updateFields.containsKey("phoneNumber")) {
            repair.setPhoneNumber((String) updateFields.get("phoneNumber"));
        }
        if (updateFields.containsKey("email")) {
            repair.setEmail((String) updateFields.get("email"));
        }
        if (updateFields.containsKey("priority")) {
            repair.setPriority((String) updateFields.get("priority"));
        }
        if (updateFields.containsKey("status")) {
            repair.setStatus((String) updateFields.get("status"));
        }
        if (updateFields.containsKey("area")) {
            repair.setArea((String) updateFields.get("area"));
        }
        if (updateFields.containsKey("description")) {
            repair.setDescription((String) updateFields.get("description"));
        }
        if (updateFields.containsKey("assetName")) {
            repair.setAssetName((String) updateFields.get("assetName"));
        }
        if (updateFields.containsKey("scheduled")) {
            repair.setScheduled((boolean) updateFields.get("scheduled"));
        }
        if (updateFields.containsKey("accept")) {
            repair.setAccept((Boolean) updateFields.get("accept"));
        }
        if (updateFields.containsKey("academyId")) {
            repair.setAcademyId((String) updateFields.get("academyId"));
        }
        if (updateFields.containsKey("assetCode")) {
            repair.setAssetCode((String) updateFields.get("assetCode"));
        }

        // Save the updated repair
        repairRepository.save(repair);

        // Prepare the response with the updated repair data
        response.put("status", "success");
        response.put("message", "Repair updated successfully.");
        response.put("repair", repair);

        return response;
    }

    @Override
    public void deleteRepair(String repairID) {
        repairRepository.deleteById(repairID);
    }

    @Override
    public Boolean acceptRepairById(String repairId, Boolean accept) {
        Optional<Repair> optionalRepair = repairRepository.findById(repairId);

        if (optionalRepair.isPresent()) {
            Repair repair = optionalRepair.get();
            repair.setAccept(accept);
            repairRepository.save(repair);

            String email = repair.getEmail();
            String subject = accept ? "Repair Request Accepted" : "Repair Request Rejected";
            String body = accept
                    ? "Dear " + repair.getName() + ",\n\n"
                            + "We are pleased to inform you that your repair request for the asset: "
                            + repair.getAssetName() + " has been accepted and is now in process.\n\n"
                            + "Thank you,\n\n"
                            + "NHDCL"
                    : "Dear " + repair.getName() + ",\n\n"
                            + "We regret to inform you that your repair request for the asset: "
                            + repair.getAssetName() + " has been rejected.\n\n"
                            + "Thank you for your understanding.\n\n"
                            + "NHDCL";

            emailService.sendEmail(email, subject, body);

            return true;
        }

        return false;
    }

    @Override
    public boolean setScheduleTrue(String repairId) {
        // Find the repair record by reportId
        Optional<Repair> optionalRepair = repairRepository.findById(repairId);

        if (optionalRepair.isPresent()) {
            Repair repair = optionalRepair.get();
            // Set schedule flag to true
            repair.setScheduled(true);
            repairRepository.save(repair);
            return true;
        }

        return false;
    }

}
