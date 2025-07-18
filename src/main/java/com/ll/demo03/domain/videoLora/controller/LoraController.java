package com.ll.demo03.domain.videoLora.controller;

import com.ll.demo03.domain.videoLora.dto.LoraResponse;
import com.ll.demo03.domain.videoLora.entity.Type;
import com.ll.demo03.domain.videoLora.service.LoraService;
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
    public ResponseEntity<List<LoraResponse>> getLoras(@RequestParam Type type) {
        return ResponseEntity.ok(loraService.getLora(type));
    }
}

