package com.example.demo.dto;

import java.util.List;

import lombok.Data;


@Data
public class ChatRequest {

    private Long clientId;

    private String model;
     private String message;
    private List<Message> messages;

    private Double temperature;
    private Integer max_completion_tokens;
    private Double top_p;
    private Boolean stream;
    private Object stop;

    @Data
    public static class Message {
        private String role;   // system | user | assistant
        private String content;

    }
}