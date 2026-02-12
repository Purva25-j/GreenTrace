package com.greentrace.app.service;

import com.greentrace.app.model.WasteItem;
import com.greentrace.app.repository.WasteRepository;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WasteService {

    @Autowired
    private WasteRepository wasteRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    public WasteItem createWaste(WasteItem item) {
        // Convert Lat/Lon from the HTML form into a Geometry Point for MySQL
        Point location = geometryFactory.createPoint(new Coordinate(item.getLongitude(), item.getLatitude()));
        location.setSRID(4326); // Set the standard GPS coordinate system
        
        item.setLocation(location);
        return wasteRepository.save(item);
    }
    public List<WasteItem> getNearbyWaste(double lat, double lon, double radiusKm) {
        // MySQL expects Longitude (X) then Latitude (Y)
        String pointWkt = String.format("POINT(%f %f)", lon, lat);
        
        // ST_Distance_Sphere uses meters
        double radiusMeters = radiusKm * 1000;
        
        return wasteRepository.findNearbyWaste(pointWkt, radiusMeters);
    }
    @Transactional
    public void claimWasteItem(Long id) {
        WasteItem item = wasteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Waste not found"));

        // Log the current status to your Eclipse console to see what's happening
        System.out.println("Attempting to claim ID: " + id + " | Current Status: " + item.getStatus());

        // Use .name() to compare if the Enum mapping is being stubborn
        if (item.getStatus() != null && item.getStatus().name().equals("AVAILABLE")) {
            item.setStatus(WasteItem.Status.CLAIMED);
            
            // Ensure credits are calculated
            int earnedPoints = (int) (item.getWeight() * 10);
            item.setCredits(earnedPoints);
            
            wasteRepository.save(item);
            System.out.println("Successfully claimed! Credits added: " + earnedPoints);
        } else {
            throw new RuntimeException("Item is not available for claiming.");
        }
    
    }
}