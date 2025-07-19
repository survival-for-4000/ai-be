package com.ll.demo03.domain.lora.repository;

import com.ll.demo03.domain.lora.entity.Lora;
import com.ll.demo03.domain.lora.entity.MediaType;
import com.ll.demo03.domain.lora.entity.StyleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoraRepository extends JpaRepository<Lora, Long> {
        List<Lora> findByMediaTypeAndStyleType(MediaType mediaType, StyleType styleType);


}

