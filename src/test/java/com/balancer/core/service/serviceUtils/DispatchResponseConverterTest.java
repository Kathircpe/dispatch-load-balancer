package com.balancer.core.service.serviceUtils;

import com.balancer.core.dto.DispatchResponse;
import com.balancer.core.dto.PlanResponse;
import com.balancer.core.service.LoadBalancer;
import com.balancer.core.service.Nodes.VehicleWithOrders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchResponseConverterTest {

    @Mock
    private LoadBalancer loadBalancer;

    @Test
    void getPlanResponse_emptyDispatch_returnsEmptyPlan() {
        when(loadBalancer.getDispatch()).thenReturn(List.of());

        PlanResponse result = DispatchResponseConverter.getPlanResponse(loadBalancer);

        assertNotNull(result);
        assertNotNull(result.dispatchResponsePlan);
        assertTrue(result.dispatchResponsePlan.isEmpty());
    }

    @Test
    void getPlanResponse_singleVehicle_noOrders() {
        VehicleWithOrders vehicle = new VehicleWithOrders("V1", 100);
        when(loadBalancer.getDispatch()).thenReturn(List.of(vehicle));

        PlanResponse result = DispatchResponseConverter.getPlanResponse(loadBalancer);

        assertEquals(1, result.dispatchResponsePlan.size());
        DispatchResponse response = result.dispatchResponsePlan.get(0);
        assertEquals("V1", response.vehicleId);
        assertEquals(0, response.totalLoad);
        assertEquals("0 km", response.totalDistance);
        assertTrue(response.assignedOrders.isEmpty());
    }


}

