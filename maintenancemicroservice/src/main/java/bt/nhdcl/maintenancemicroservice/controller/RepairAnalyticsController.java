package bt.nhdcl.maintenancemicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bt.nhdcl.maintenancemicroservice.service.RepairAnalyticsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class RepairAnalyticsController {

    @Autowired
    private RepairAnalyticsService repairAnalyticsService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAverageResponseTimes() {
        List<Map<String, Object>> results = repairAnalyticsService.getAverageResponseTimes();
        return ResponseEntity.ok(results);
    }
}

