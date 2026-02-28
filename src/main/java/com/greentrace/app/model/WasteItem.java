package com.greentrace.app.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String phoneNumber;
    private String recyclerPhone;
    private Integer credits = 0;

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

    @PostLoad
    protected void fillTransientCoordinates() {
        if (location != null) {
            this.longitude = location.getX();
            this.latitude = location.getY();
        }
    }

    // CRITICAL: Ensure this method exists
    @PrePersist
    protected void preparePoint() {
        if (this.latitude != null && this.longitude != null) {
            this.location = new GeometryFactory().createPoint(new Coordinate(this.longitude, this.latitude));
        }
    }

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
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRecyclerPhone() { return recyclerPhone; }
    public void setRecyclerPhone(String recyclerPhone) { this.recyclerPhone = recyclerPhone; }
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
        AVAILABLE, CLAIMED, SOLD
    }
}