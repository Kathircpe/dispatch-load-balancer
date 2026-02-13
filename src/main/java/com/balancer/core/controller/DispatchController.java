package com.balancer.core.controller;

import com.balancer.core.dto.PlanResponse;
import com.balancer.core.dto.PostStatus;
import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/dispatch")
public class DispatchController {
    @Autowired
    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService){
        this.dispatchService=dispatchService;
    }

    @PostMapping("/orders")
    public ResponseEntity<PostStatus> orderController(@RequestBody Map<String, List<Order>> body){
        return dispatchService.orderService(body);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<PostStatus> vehicleController(@RequestBody Map<String, List<Vehicle>> body){
        return dispatchService.vehicleService(body);
    }

    @GetMapping("/plan")
    public ResponseEntity<PlanResponse> planController(){
        return dispatchService.planService();
    }

}
