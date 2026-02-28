package com.greentrace.app.controller;

import com.greentrace.app.model.WasteItem;
import com.greentrace.app.service.WasteService;
import com.greentrace.app.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/waste")
public class WasteController {

    @Autowired
    private WasteService wasteService;
    @Autowired
    private WasteRepository wasteRepository;

    @PostMapping("/add")
    public String addWaste(@RequestBody WasteItem item) {
        wasteService.createWaste(item);
        return "Waste Posted Successfully!";
    }

    @GetMapping("/nearby")
    public List<WasteItem> getNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double radius) {
        return wasteService.getNearbyWaste(lat, lon, radius);
    }

    @GetMapping("/leaderboard")
    public List<Object[]> getLeaderboard() {
        return wasteService.getAreaLeaderboard();
    }

    @PostMapping("/claim/{id}")
    public ResponseEntity<?> claimWaste(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String recyclerPhone = requestBody.get("recyclerPhone");
        wasteService.claimWaste(id, recyclerPhone);
        return ResponseEntity.ok("Waste successfully claimed!");
    }

    @GetMapping("/my-latest-request")
    public ResponseEntity<WasteItem> getLatestRequest() {
        WasteItem latest = wasteRepository.findTopByOrderByIdDesc();
        return ResponseEntity.ok(latest);
    }

    @PostMapping("/sold/{id}")
    public String markAsSold(@PathVariable Long id) {
        wasteService.markAsSold(id);
        return "Waste marked as sold!";
    }

    @GetMapping("/total-impact")
    public ResponseEntity<Double> getTotalImpact() {
        Double totalWeight = wasteService.getTotalImpact();
        return ResponseEntity.ok(totalWeight);
    }
}