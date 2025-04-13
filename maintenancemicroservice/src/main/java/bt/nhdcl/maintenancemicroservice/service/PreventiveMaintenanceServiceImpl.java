package bt.nhdcl.maintenancemicroservice.service;

import bt.nhdcl.maintenancemicroservice.entity.PreventiveMaintenance;
import bt.nhdcl.maintenancemicroservice.repository.PreventiveMaintenanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PreventiveMaintenanceServiceImpl implements PreventiveMaintenanceService {

    private final PreventiveMaintenanceRepository maintenanceRepository;

    public PreventiveMaintenanceServiceImpl(PreventiveMaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public PreventiveMaintenance saveMaintenance(PreventiveMaintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public List<PreventiveMaintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Optional<PreventiveMaintenance> getMaintenanceById(String maintenanceID) {
        return maintenanceRepository.findById(maintenanceID);
    }

    @Override
    public void deleteMaintenance(String maintenanceID) {
        maintenanceRepository.deleteById(maintenanceID);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByStatus(String status) {
        return maintenanceRepository.findByStatus(status);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByStartDate(LocalDate startDate) {
        return maintenanceRepository.findByStartDate(startDate);
    }

    @Override
    public List<PreventiveMaintenance> getMaintenanceByAssetCode(String assetCode) {
        return maintenanceRepository.findByAssetCode(assetCode);
    }
}
