package com.monitor.api.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TypeDto(
        UUID id,
        @NotBlank @Size(max = 36) String name) {
}
