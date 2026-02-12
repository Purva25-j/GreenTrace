package com.greentrace.app.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import com.fasterxml.jackson.annotation.JsonIgnore; // Import this to fix the JSON error

@Entity
@Table(name = "waste_items")
public class WasteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private Double weight;
    private String description;
    private String areaName; 

    private Integer credits = 0; 

    // Adding @JsonIgnore fixes the "Could not write JSON" error. 
    // It tells Jackson: "Don't try to serialize this complex Point object."
    @JsonIgnore 
    @Column(name = "location", columnDefinition = "POINT SRID 4326", nullable = true)
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.AVAILABLE;
    @Transient 
    private Double latitude;
    @Transient
    private Double longitude;

    public WasteItem() {}

    // --- Logic for JSON: Mapping Point back to Lat/Lon for the Frontend ---
    // This ensures the recycler.html still gets the numbers even if 'location' is ignored.
    @PostLoad
    protected void fillTransientCoordinates() {
        if (location != null) {
            // Swap these to match real-world Lat/Lon
            this.longitude = location.getX(); // X is Longitude (73.71)
            this.latitude = location.getY();  // Y is Latitude (20.00)
        }
    
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public enum Status {
        AVAILABLE, CLAIMED, COMPLETED
    }
    

    }
