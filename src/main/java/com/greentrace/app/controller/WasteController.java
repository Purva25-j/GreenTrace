package com.greentrace.app.controller;

import com.greentrace.app.model.WasteItem;
import com.greentrace.app.service.WasteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waste")
public class WasteController {

    @Autowired
    private WasteService wasteService;

    // This method receives data from your HTML form
    @PostMapping("/add")
    public String addWaste(@ModelAttribute WasteItem item) {
        wasteService.createWaste(item);
        return "Waste Posted Successfully! Check your MySQL Database.";
    }
//    @GetMapping("/nearby")
//    public List<WasteItem> getNearby(@RequestParam double lat, @RequestParam double lon) {
//        // Increase 5.0 to 15.0 for a wider search area
//        return wasteService.getNearbyWaste(lat, lon, 5); 
//    }
    @GetMapping("/nearby")
    public List<WasteItem> getNearby(@RequestParam double lat, @RequestParam double lon) {
        // Increase to 50km for testing to ensure Nashik points are caught
        double testRadius = 50.0; 
        System.out.println("Scanning for waste near Lat: " + lat + ", Lon: " + lon + " within " + testRadius + "km");
        return wasteService.getNearbyWaste(lat, lon, testRadius);
    }
}