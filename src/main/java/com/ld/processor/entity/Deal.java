package com.ld.processor.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@Table("samples")
public record Deal(@Id Long id,
                   @Column String code,
                   @Column String description,
                   @Column LocalDateTime validFrom,
                   @Column LocalDateTime validTo,
                   @Column LocalDateTime createdOn,
                   @Column LocalDateTime updatedOn) {
    public static Deal from(DealDTO dto) {
        return new Deal(0L, dto.code(), dto.description(), dto.validFrom(), dto.validTo(), LocalDateTime.now(), null);
    }
}
