package com.balancer.core.service.Nodes;

import com.balancer.core.model.Order;
import com.balancer.core.utils.OrderInputValidator;

/**
 * to store orders for dispatch calculation
 */
public class OrderNode implements Comparable<OrderNode> {
    public final String orderId;
    public final Double latitude;
    public final Double longitude;
    public final String address;
    public final int packageWeight;
    public final int priority;

    public OrderNode(Order o) {
        this.orderId = o.orderId;
        this.latitude = o.latitude;
        this.longitude = o.longitude;
        this.address = o.address;
        this.packageWeight = o.packageWeight;
        this.priority = OrderInputValidator.PRIORITY.indexOf(o.priority);
    }

    //custom compare method to sort on in the heap
    @Override
    public int compareTo(OrderNode o) {
        if (o == null) return 1;
        if (this.priority == o.priority) {
            if (this.packageWeight == o.packageWeight) return 0;
            else if (this.packageWeight < o.packageWeight) return 1;
            else return -1;
        } else if (this.priority > o.priority) return 1;
        else return -1;
    }
}
