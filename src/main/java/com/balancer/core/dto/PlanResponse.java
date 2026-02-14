package com.balancer.core.dto;

import java.util.List;

public class PlanResponse {
    public final List<DispatchResponse> dispatchResponsePlan;

    public PlanResponse(List<DispatchResponse> dispatchResponsePlan) {
        this.dispatchResponsePlan = dispatchResponsePlan;
    }
}