package com.ll.demo03.domain.lora.controller;

import com.ll.demo03.domain.lora.dto.LoraResponse;
import com.ll.demo03.domain.lora.entity.MediaType;
import com.ll.demo03.domain.lora.entity.StyleType;
import com.ll.demo03.domain.lora.service.LoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lora")
public class LoraController {

    private final LoraService loraService;

    @GetMapping
    public ResponseEntity<List<LoraResponse>> getLoras(
            @RequestParam MediaType mediaType,
            @RequestParam StyleType styleType
    ) {
        return ResponseEntity.ok(loraService.getLora(mediaType, styleType));
    }

}

