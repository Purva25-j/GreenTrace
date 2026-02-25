package com.greentrace.app.service;

import com.greentrace.app.model.WasteItem;
import com.greentrace.app.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WasteService {

    @Autowired
    private WasteRepository wasteRepository;

    // 1. Create new waste (Status MUST be AVAILABLE for Radar)
    // 1. Create new waste (Status MUST be AVAILABLE for Radar)
    public void createWaste(WasteItem item) {
        // Use the Enum instead of a String
        item.setStatus(WasteItem.Status.AVAILABLE);

        // FIX: Cast the math to (int) to match your Integer credits field
        if (item.getCredits() == null) {
            item.setCredits((int) (item.getWeight() * 10));
        }

        wasteRepository.save(item);
    }
    // 2. Claim Waste (This makes it show up on LEADERBOARD)
    public void claimWaste(Long id) {
        WasteItem item = wasteRepository.findById(id).orElse(null);
        if (item != null) {
            // FIX: Use WasteItem.Status.CLAIMED instead of "CLAIMED"
            item.setStatus(WasteItem.Status.CLAIMED);
            wasteRepository.save(item);
        }
    }

    public List<WasteItem> getNearbyWaste(double lat, double lon, double radiusKm) {
        // 1. Convert to WKT format: POINT(Longitude Latitude)
        String pointWkt = "POINT(" + lon + " " + lat + ")";

        // 2. IMPORTANT: Convert KM to Meters for MySQL
        double radiusMeters = radiusKm * 1000;

        return wasteRepository.findNearbyWaste(pointWkt, radiusMeters);
    }
    public void completeTransaction(Long id) {
        WasteItem item = wasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setStatus(WasteItem.Status.SOLD);
        wasteRepository.save(item);
    }

    public List<Object[]> getAreaLeaderboard() {
        return wasteRepository.getAreaLeaderboard();
    }

    public WasteItem getById(Long id) {
        return wasteRepository.findById(id).orElse(null);
    }

    public void save(WasteItem item) {
        wasteRepository.save(item);
    }

    public Double getTotalImpact() {
        Double total = wasteRepository.getTotalRecycledWeight();
        return (total != null) ? total : 0.0;
    }
}