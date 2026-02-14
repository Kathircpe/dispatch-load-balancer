package com.balancer.core.service;

import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.Nodes.OrderNode;
import com.balancer.core.service.Nodes.VehicleWithOrders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class LoadBalancerTest {

    private LoadBalancer loadBalancer;

    @BeforeEach
    void setUp() {
        loadBalancer = new LoadBalancer();
    }

    @Test
    void addNewOrders_addsOrdersToHeap() throws Exception {
        List<Order> orders = Arrays.asList(
                new Order("O1", 10.0, 20.0, "addr1", 5, "MEDIUM"),
                new Order("O2", 30.0, 40.0, "addr2", 3, "MEDIUM")
        );
        loadBalancer.addNewOrders(orders);

        PriorityQueue<OrderNode> queue = TestUtils.getPrivateField(loadBalancer, "orders");
        Thread.sleep(1000);
        assertEquals(2, queue.size());
    }

    @Test
    void addNewVehicles_addsVehiclesToDispatch() {
        Vehicle v1 = new Vehicle("V1", 100);
        v1.currentLatitude = 10.0;
        v1.currentLongitude = 20.0;
        v1.address = "addr1";

        Vehicle v2 = new Vehicle("V2", 200);
        v2.currentLatitude = 30.0;
        v2.currentLongitude = 40.0;
        v2.address = "addr2";

        List<Vehicle> vehicles = Arrays.asList(v1, v2);
        loadBalancer.addNewVehicles(vehicles);

        List<VehicleWithOrders> dispatch = loadBalancer.getDispatch();
        assertEquals("V1", dispatch.get(0).vehicleId);
        assertEquals(100, dispatch.get(0).capacity);
    }

    @Test
    void dispatch_capacityExceeded_ordersOrphaned() {
        try (MockedStatic<LoadBalancer.Haversine> mock = mockStatic(LoadBalancer.Haversine.class)) {
            mock.when(() -> LoadBalancer.Haversine.findDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                    .thenReturn(1.0);

            loadBalancer.addNewOrders(List.of(new Order("O1", 10.0, 20.0, "addr1", 150, "HIGH")));
            Vehicle v1 = new Vehicle("V1", 100);
            v1.currentLatitude = 20.0;
            v1.currentLongitude = 20.0;
            v1.address = "addr1";
            loadBalancer.addNewVehicles(List.of(v1));

            loadBalancer.dispatch();

            var queue = assertDoesNotThrow(() -> TestUtils.getPrivateField(loadBalancer, "orders"));
            Thread.sleep(1000);
            assertEquals(1, ((java.util.PriorityQueue<?>) queue).size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void dispatch_selectsNearestVehicle() {
        try (MockedStatic<LoadBalancer.Haversine> mock = mockStatic(LoadBalancer.Haversine.class)) {
            mock.when(() -> LoadBalancer.Haversine.findDistance(10.0, 20.0, 10.0, 20.0))
                    .thenReturn(0.0);
            mock.when(() -> LoadBalancer.Haversine.findDistance(10.0, 20.0, 50.0, 50.0))
                    .thenReturn(5000.0);

            loadBalancer.addNewOrders(List.of(new Order("O1", 10.0, 20.0, "addr1", 10, "LOW")));
            Vehicle v1 = new Vehicle("V1", 100);
            v1.currentLatitude = 10.0;
            v1.currentLongitude = 20.0;
            v1.address = "addr1";
            Vehicle v2 = new Vehicle("V2", 100);
            v2.currentLatitude = 50.0;
            v2.currentLongitude = 50.0;
            v2.address = "addr2";
            loadBalancer.addNewVehicles(Arrays.asList(v1, v2));

            loadBalancer.dispatch();

            assertEquals("V1", loadBalancer.getDispatch().get(0).vehicleId);
        }
    }

    @Test
    void getDispatch_returnsCorrectPlan() {
        Vehicle v1 = new Vehicle("V1", 100);
        v1.currentLatitude = 0.0;
        v1.currentLongitude = 0.0;
        v1.address = "addr";
        loadBalancer.addNewVehicles(List.of(v1));
        List<VehicleWithOrders> dispatch = loadBalancer.getDispatch();
        assertEquals(1, dispatch.size());
        assertEquals("V1", dispatch.get(0).vehicleId);
        assertEquals(100, dispatch.get(0).capacity);
    }

}
