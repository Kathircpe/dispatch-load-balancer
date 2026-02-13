package com.balancer.core.dto;


import com.balancer.core.service.Nodes.OrderNode;
import com.balancer.core.utils.OrderInputValidator;

public class OrderResponse {
    public String orderId;
    public Double latitude;
    public Double longitude;
    public String address;
    public Integer packageWeight;
    public String priority;
     public OrderResponse(OrderNode o){
         this.orderId=o.orderId;
         this.latitude=o.latitude;
         this.longitude=o.longitude;
         this.address=o.address;
         this.packageWeight=o.packageWeight;
         this.priority= OrderInputValidator.PRIORITY.get(o.priority);
     }

}
