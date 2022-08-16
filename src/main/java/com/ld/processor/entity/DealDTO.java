package com.ld.processor.entity;

import java.time.LocalDateTime;

public record DealDTO(
        String code,
        String description,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {
}
