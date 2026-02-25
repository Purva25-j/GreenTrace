package com.greentrace.app.controller;

import com.greentrace.app.model.WasteItem;
import com.greentrace.app.service.WasteService;
import com.greentrace.app.repository.WasteRepository; // Added this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/waste")
public class WasteController {

    @Autowired
    private WasteService wasteService;

    @Autowired
    private WasteRepository wasteRepository; // Added this to fix your error

    @PostMapping("/add")
    public String addWaste(@RequestBody WasteItem item) {
        wasteService.createWaste(item);
        return "Waste Posted Successfully!";
    }
    //this is for all card show not gps base
//    @GetMapping("/nearby")
//    public List<WasteItem> getNearby(@RequestParam double lat, @RequestParam double lon) {
//        // DEMO MODE: Bypass GPS math and show ALL available items
//        // This ensures "Chunchale" shows up regardless of your location
//        return wasteRepository.findAll().stream()
//                .filter(item -> item.getStatus() == WasteItem.Status.AVAILABLE)
//                .collect(Collectors.toList());
//    }

    @GetMapping("/nearby")
    public List<WasteItem> getNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double radius) { // Accept the radius from the slider

        return wasteService.getNearbyWaste(lat, lon, radius);
    }




    @GetMapping("/leaderboard")
    public List<Object[]> getLeaderboard() {
        return wasteService.getAreaLeaderboard();
    }

    @PostMapping("/claim/{id}")
    public String claimWaste(@PathVariable Long id) {
        wasteService.claimWaste(id);
        return "Waste successfully claimed!";
    }

    @GetMapping("/total-impact")
    public ResponseEntity<Double> getTotalImpact() {
        // Calling the service method we discussed earlier
        Double totalWeight = wasteService.getTotalImpact();
        return ResponseEntity.ok(totalWeight);
    }
}