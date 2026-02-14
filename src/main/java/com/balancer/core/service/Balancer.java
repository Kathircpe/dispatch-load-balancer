package com.balancer.core.service;

import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.Nodes.VehicleWithOrders;

import java.util.List;

/**
 * Abstraction for core load balancing logic
 */
public interface Balancer {
    void addNewVehicles(List<Vehicle> newVehicles);

    void addNewOrders(List<Order> newOrders);

    void dispatch();

    List<VehicleWithOrders> getDispatch();
}
