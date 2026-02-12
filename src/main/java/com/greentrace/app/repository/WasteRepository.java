package com.greentrace.app.repository;

import com.greentrace.app.model.WasteItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteRepository extends JpaRepository<WasteItem, Long> {
    // This allows you to do: repository.save(item), repository.findAll(), etc.
	@Query(value = "SELECT * FROM waste_items w WHERE " +
		       "ST_Distance_Sphere(w.location, ST_GeomFromText(?, 4326)) <= ? " +
		       "AND w.status = 'AVAILABLE' " + // Only show unclaimed items
		       "ORDER BY w.id DESC", // Put the newest "bids" at the top
		       nativeQuery = true)
		List<WasteItem> findNearbyWaste(String pointWkt, double radiusMeters);
}