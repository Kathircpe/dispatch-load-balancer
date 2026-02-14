package com.balancer.core.service.serviceUtils;

import com.balancer.core.dto.DispatchResponse;
import com.balancer.core.dto.OrderResponse;
import com.balancer.core.dto.PlanResponse;
import com.balancer.core.service.LoadBalancer;
import com.balancer.core.service.Nodes.VehicleWithOrders;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is to convert the stored data to response
 */
public class DispatchResponseConverter {

    private static final Logger logger = LoggerFactory.getLogger(DispatchResponseConverter.class);

    public static PlanResponse getPlanResponse(LoadBalancer loadBalancer) {
        List<VehicleWithOrders> dispatch = loadBalancer.getDispatch();
        List<DispatchResponse> dispatchResponsePlan = new ArrayList<>();
        logger.info("dispatch plan is converting to response");
        dispatch.forEach(v -> {
            //convert each vehicle to response
            DispatchResponse temp = new DispatchResponse(v);
            //store each orders of the corresponding vehicle in the vehicleResponse
            v.assignedOrders.forEach(o -> temp.assignedOrders.add(new OrderResponse(o)));
            //store the converted vehicleResponse in the dispatch plan response
            Collections.reverse(temp.assignedOrders);
            dispatchResponsePlan.add(temp);
        });
        logger.info("dispatch plan is converted to response");

        return new PlanResponse(dispatchResponsePlan);
    }
}
