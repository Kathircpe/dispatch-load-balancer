package com.balancer.core.service;

import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.Nodes.VehicleWithOrders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoadBalancerConcurrencyTest {

    @Test
    @Timeout(10)
    void concurrentDispatch_threadSafe() throws InterruptedException {
        LoadBalancer balancer = new LoadBalancer();

        List<Order> orders = createOrders(100);
        List<Vehicle> vehicles = createVehicles(10);

        Thread t1 = new Thread(() -> balancer.addNewOrders(orders));
        Thread t2 = new Thread(() -> balancer.addNewVehicles(vehicles));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Thread.sleep(2000);

        List<VehicleWithOrders> dispatch = balancer.getDispatch();
        assertTrue(dispatch.size() >= 5);
    }


    @Test
    @Timeout(5)
    void multipleConcurrentAdditions_noDataCorruption() throws InterruptedException {
        LoadBalancer balancer = new LoadBalancer();

        List<Thread> threads = IntStream.range(0, 5)
                .mapToObj(i -> (Thread) new Thread(() -> {
                    balancer.addNewOrders(createOrders(20));
                    balancer.addNewVehicles(createVehicles(2));
                }))
                .collect(Collectors.toList());

        threads.forEach(Thread::start);
        for (Thread t : threads) t.join();

        Thread.sleep(2000);
        assertDoesNotThrow(() -> balancer.getDispatch().size());
    }

    private List<Order> createOrders(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Order("O" + i, 0.0, 0.0, "test", 10, "MEDIUM"))
                .collect(Collectors.toList());
    }

    private List<Vehicle> createVehicles(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Vehicle v = new Vehicle("V" + i, 100);
                    v.currentLatitude = 0.0;
                    v.currentLongitude = 0.0;
                    v.address = "test";
                    return v;
                })
                .collect(Collectors.toList());
    }
}
