package bt.nhdcl.maintenancemicroservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import bt.nhdcl.maintenancemicroservice.service.MaintenanceCostService;
import bt.nhdcl.maintenancemicroservice.entity.CombinedMaintenanceCostResult;

@RestController
@RequestMapping("/api/cost")
public class MaintenanceCostController {

    @Autowired
    private MaintenanceCostService maintenanceCostService;

    @GetMapping
    public List<CombinedMaintenanceCostResult> getAllCombinedMaintenanceCost() {
        return maintenanceCostService.getAllMaintenanceCosts();
    }
}

