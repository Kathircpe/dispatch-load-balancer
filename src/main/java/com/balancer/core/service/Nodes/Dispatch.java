package com.balancer.core.service.Nodes;

import com.balancer.core.dto.OrderResponse;
import com.balancer.core.model.Order;

import java.util.List;

public class Dispatch {
    String vehicleId;
    Integer totalLoad;
    String totalDistance;
    List<OrderNode> assignedOrders;

}
