package com.monitor.api.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monitor.api.validation.ValidRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidRange
public final class SensorDto {
    private UUID id;
    @JsonProperty("name")
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    @NotBlank
    @Size(max = 15)
    private String model;
    private Integer rangeFrom;
    @NotNull
    private Integer rangeTo;
    @NotBlank
    private String type;
    private String unit;
    @Size(max = 40)
    private String location;
    @Size(max = 200)
    private String description;

}
