package com.balancer.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoadBalancerEdgeTest {

    private final LoadBalancer loadBalancer = new LoadBalancer();

    @Test
    void emptyCollections_noException() {
        assertDoesNotThrow(() -> loadBalancer.addNewOrders(Collections.emptyList()));
        assertDoesNotThrow(() -> loadBalancer.addNewVehicles(Collections.emptyList()));
        assertDoesNotThrow(loadBalancer::dispatch);
        assertTrue(loadBalancer.getDispatch().isEmpty());
    }

}
