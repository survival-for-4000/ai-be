package com.ll.demo03.domain.videoLora.dto;

import com.ll.demo03.domain.videoLora.entity.Lora;

public class LoraResponse {
    private String name;

    public LoraResponse(String name) {
        this.name = name;
    }

    public static LoraResponse from(Lora lora) {
        return new LoraResponse(lora.getName());
    }

    // Getter
    public String getName() {
        return name;
    }
}

