package com.greentrace.app.repository;

import com.greentrace.app.model.WasteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WasteRepository extends JpaRepository<WasteItem, Long> {

	WasteItem findTopByOrderByIdDesc();

	@Query(value = "SELECT * FROM waste_items WHERE status = 'AVAILABLE'", nativeQuery = true)
	List<WasteItem> findNearbyWaste(@Param("pointWkt") String pointWkt, @Param("radius") double radius);

	@Query("SELECT w.areaName, SUM(w.credits) FROM WasteItem w " +
			"WHERE w.status = com.greentrace.app.model.WasteItem.Status.SOLD " +
			"GROUP BY w.areaName ORDER BY SUM(w.credits) DESC")
	List<Object[]> getAreaLeaderboard();

	@Query("SELECT SUM(w.weight) FROM WasteItem w WHERE w.status = com.greentrace.app.model.WasteItem.Status.SOLD")
	Double getTotalRecycledWeight();
}