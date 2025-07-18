package com.ll.demo03.domain.videoLora.repository;

import com.ll.demo03.domain.videoLora.entity.Lora;
import com.ll.demo03.domain.videoLora.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoraRepository extends JpaRepository<Lora, Long> {
    List<Lora> findByType(Type type);
}

