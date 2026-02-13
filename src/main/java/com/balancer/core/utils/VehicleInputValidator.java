package com.balancer.core.utils;

import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class VehicleInputValidator {
    private static final String id="VEH";
    public static boolean validateInput(Map<String, List<Vehicle>> body){
        if(!body.containsKey("vehicles"))return false;
        List<Vehicle> vehicles=body.get("vehicles");
        return helper(vehicles,vehicle -> vehicle.vehicleId.startsWith(id)
                                                    && vehicle.currentLatitude>=-90
                                                    && vehicle.currentLatitude<=90
                                                    && vehicle.currentLongitude>=-180
                                                    && vehicle.currentLongitude<=180
                                                    && vehicle.capacity>=0);
    }
    private static boolean helper(List<Vehicle> vehicles, Predicate<Vehicle> predicate){
        return vehicles.parallelStream().allMatch(predicate);
    }
}
