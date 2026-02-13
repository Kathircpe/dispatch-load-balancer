package com.balancer.core.dto;

import com.balancer.core.service.Nodes.VehicleWithOrders;

import java.util.ArrayList;
import java.util.List;

public class DispatchResponse {
   public String vehicleId;
    public Integer totalLoad;
    public String totalDistance;
    public List<OrderResponse> assignedOrders;

    public DispatchResponse(VehicleWithOrders o){
        this.vehicleId=o.vehicleId;
        this.totalLoad=o.totalLoad;
        this.totalDistance=o.totalDistance;
        assignedOrders=new ArrayList<>();
    }

}
