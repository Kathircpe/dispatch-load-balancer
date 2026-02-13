package com.balancer.core.service.serviceUtils;

import com.balancer.core.dto.DispatchResponse;
import com.balancer.core.dto.OrderResponse;
import com.balancer.core.dto.PlanResponse;
import com.balancer.core.service.LoadBalancer;
import com.balancer.core.service.Nodes.OrderNode;
import com.balancer.core.service.Nodes.VehicleWithOrders;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to convert the stored data to response
 */
public class DispatchResponseConverter {
    public static PlanResponse getPlanResponse(LoadBalancer loadBalancer){
        List<VehicleWithOrders> dispatch=loadBalancer.getDispatch();
        List<DispatchResponse> dispatchResponsePlan=new ArrayList<>();

        for(VehicleWithOrders v:dispatch){
            DispatchResponse temp=new DispatchResponse(v);
            for(OrderNode o:v.assignedOrders){
                temp.assignedOrders.add(new OrderResponse(o));
            }
            dispatchResponsePlan.add(temp);
        }

        return new PlanResponse(dispatchResponsePlan);
    }
}
