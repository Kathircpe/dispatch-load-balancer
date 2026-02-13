package com.balancer.core.service.Nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * to store the vehicles and their orders
 */
public class VehicleWithOrders {
   public final String vehicleId;
    public final Integer capacity;
    public Integer totalLoad;
    public String totalDistance;
    public Double currentLatitude;
    public Double currentLongitude;
    public String address;
    public List<OrderNode> assignedOrders;
    public VehicleWithOrders(String vehicleId, Integer capacity){
        this.assignedOrders=new ArrayList<>();
        this.vehicleId=vehicleId;
        this.capacity=capacity;
        this.totalLoad=0;
        this.totalDistance="0 km";
    }
}
