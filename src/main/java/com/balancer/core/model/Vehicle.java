package com.balancer.core.model;

public class Vehicle {
    public final String vehicleId;
    public final Integer capacity;
    public Double currentLatitude;
    public Double currentLongitude;
    public String address;
    public Vehicle(String vehicleId, Integer capacity){
        this.vehicleId=vehicleId;
        this.capacity=capacity;
    }
}
