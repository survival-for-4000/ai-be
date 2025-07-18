package com.ll.demo03.domain.videoLora.service;

import com.ll.demo03.domain.videoLora.dto.LoraResponse;
import com.ll.demo03.domain.videoLora.entity.Lora;
import com.ll.demo03.domain.videoLora.entity.Type;
import com.ll.demo03.domain.videoLora.repository.LoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoraService {

    private final LoraRepository loraRepository;

    public List<LoraResponse> getLora(Type type) {
        List<Lora> lists = loraRepository.findByType(type);
        return lists.stream()
                .map(LoraResponse::from)
                .collect(Collectors.toList());
    }
}

