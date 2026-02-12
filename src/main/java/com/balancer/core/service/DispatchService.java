package com.balancer.core.service;

import com.balancer.core.dto.PlanResponse;
import com.balancer.core.dto.PostStatus;
import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DispatchService {
    public ResponseEntity<PostStatus> vehicleService(Map<String, List<Vehicle>> body) {
    }

    public ResponseEntity<PostStatus> orderService(Map<String, List<Order>> body) {
    }

    public ResponseEntity<PlanResponse> planService() {
        return null;
    }
}
