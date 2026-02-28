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

    public void createWaste(WasteItem item) {
        item.setStatus(WasteItem.Status.AVAILABLE);

        if (item.getCredits() == null) {
            item.setCredits((int) (item.getWeight() * 10));
        }

        wasteRepository.save(item);
    }

    public void claimWaste(Long id, String recyclerPhone) {
        WasteItem item = wasteRepository.findById(id).orElse(null);
        if (item != null) {
            item.setStatus(WasteItem.Status.CLAIMED);
            item.setRecyclerPhone(recyclerPhone);
            wasteRepository.save(item);
        }
    }

    public List<WasteItem> getNearbyWaste(double lat, double lon, double radiusKm) {
        String pointWkt = "POINT(" + lon + " " + lat + ")";
        double radiusMeters = radiusKm * 1000;
        return wasteRepository.findNearbyWaste(pointWkt, radiusMeters);
    }

    public void markAsSold(Long id) {
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