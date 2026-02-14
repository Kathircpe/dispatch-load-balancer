package com.balancer.core.service;

import com.balancer.core.dto.PlanResponse;
import com.balancer.core.dto.PostStatus;
import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.serviceUtils.DispatchResponseConverter;
import com.balancer.core.utils.OrderInputValidator;
import com.balancer.core.utils.VehicleInputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DispatchService {
    private final LoadBalancer loadBalancer;

    public DispatchService() {
        this.loadBalancer = new LoadBalancer();
    }

    public ResponseEntity<PostStatus> vehicleService(Map<String, List<Vehicle>> body) {
        if (!VehicleInputValidator.validateInput(body)) {
            return new ResponseEntity<>(
                    new PostStatus("Invalid input", "Failure"), HttpStatus.BAD_REQUEST);
        }
        loadBalancer.addNewVehicles(body.get("vehicles"));
        return new ResponseEntity<>(
                new PostStatus("Vehicle details accepted", "Success"), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<PostStatus> orderService(Map<String, List<Order>> body) {
        if (!OrderInputValidator.validateInput(body)) {
            return new ResponseEntity<>(
                    new PostStatus("Invalid input", "Failure"), HttpStatus.BAD_REQUEST);
        }
        loadBalancer.addNewOrders(body.get("orders"));
        return new ResponseEntity<>(
                new PostStatus("Delivery orders accepted", "Success"), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<PlanResponse> planService() {
        PlanResponse planResponse = DispatchResponseConverter.getPlanResponse(loadBalancer);
        return new ResponseEntity<>(planResponse, HttpStatus.FOUND);
    }
}
