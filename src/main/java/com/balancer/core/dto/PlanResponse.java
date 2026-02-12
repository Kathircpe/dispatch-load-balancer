package com.balancer.core.dto;

public class PlanResponse {
    List<Dispatch> assignedOrders;
    public PlanResponse(List<DispatchPlan> assignedOrders){
        this.assignedOrders=assignedOrders;
    }
}
git config --global user.email "kathircp007@gmail.com"
git config --global user.name "kathir"