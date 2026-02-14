package com.balancer.core.model;

public class Order {
    public String orderId;
    public Double latitude;
    public Double longitude;
    public String address;
    public Integer packageWeight;
    public String priority;

    public Order(String orderId, Double latitude, Double longitude, String address, Integer packageWeight, String priority) {
        this.orderId = orderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.packageWeight = packageWeight;
        this.priority = priority;
    }
}
