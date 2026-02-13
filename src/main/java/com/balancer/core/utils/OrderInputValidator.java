package com.balancer.core.utils;

import com.balancer.core.model.Order;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class OrderInputValidator {
    private static final String id="ORD";
    public static final List<String> PRIORITY=List.of("LOW","MEDIUM","HIGH");

    public static boolean validateInput(Map<String, List<Order>> body){
        if(!body.containsKey("orders"))return false;
        List<Order> orders=body.get("orders");
        return helper(orders,order -> order.orderId.startsWith(id)
                                                && order.latitude>=-90
                                                && order.latitude<=90
                                                && order.longitude>=-180
                                                && order.longitude<=180
                                                && PRIORITY.contains(order.priority));
    }

    private static boolean helper(List<Order> orders, Predicate<Order> predicate){
        return orders.parallelStream().allMatch(predicate);
    }
}
