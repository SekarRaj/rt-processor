package com.ld.processor.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Deal(Long id,
                   String code,
                   String description,
                   LocalDateTime validFrom,
                   LocalDateTime validTo,
                   LocalDateTime createdOn,
                   LocalDateTime updatedOn) {
    public static Deal from(DealDTO dto) {
        return new Deal(0L, dto.code(), dto.description(), dto.validFrom(), dto.validTo(), LocalDateTime.now(), null);
    }
}
