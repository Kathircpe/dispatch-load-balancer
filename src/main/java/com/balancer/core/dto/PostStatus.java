package com.balancer.core.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message", "status"})
public class PostStatus {
    public final String message;
    public final String status;

    public PostStatus(String message, String status) {
        this.message = message;
        this.status = status;
    }

}
