package com.ll.demo03.domain.videoLora.entity;

import com.ll.demo03.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키 필요

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;
}
