package com.monitor.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitor.api.entity.Sensor;
import com.monitor.api.entity.dto.SensorDto;
import com.monitor.api.filter.SensorFilter;
import com.monitor.api.mapper.SensorMapper;
import com.monitor.api.repository.SensorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    private final ObjectMapper objectMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public SensorDto create(@RequestBody @Valid SensorDto sensorDto) {
        if (sensorDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Sensor sensor = sensorMapper.toEntity(sensorDto);
        Sensor resultSensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(resultSensor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    @Cacheable(value = "sensors", key = "#id")
    public SensorDto getOne(@PathVariable UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        Optional<Sensor> sensorOptional = sensorRepository.findById(id);
        return sensorMapper.toDto(sensorOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public PagedModel<SensorDto> getAll(@ModelAttribute SensorFilter sensorFilter, Pageable pageable) {
        Page<Sensor> units = sensorRepository.findAll(sensorFilter.toSpecification(), pageable);
        Page<SensorDto> unitDtoPage = units.map(sensorMapper::toDto);
        return new PagedModel<>(unitDtoPage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @CacheEvict(value = "sensors", key = "#id")
    public SensorDto update(@PathVariable UUID id, @RequestBody @Valid SensorDto sensorDto) {
        if (sensorDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id)));
        sensorMapper.updateWithNull(sensorDto, sensor);
        Sensor resultSensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(resultSensor);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @CacheEvict(value = "sensors", key = "#id")
    public SensorDto patch(@PathVariable UUID id, @RequestBody JsonNode patchNode) throws IOException {
        Sensor sensor = sensorRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id)));

        SensorDto sensorDto = sensorMapper.toDto(sensor);
        objectMapper.readerForUpdating(sensorDto).readValue(patchNode);
        sensorMapper.updateWithNull(sensorDto, sensor);

        Sensor resultSensor = sensorRepository.save(sensor);
        return sensorMapper.toDto(resultSensor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @CacheEvict(value = "sensors", key = "#id")
    public SensorDto delete(@PathVariable UUID id) {
        Sensor sensor = sensorRepository.findById(id).orElse(null);
        if (sensor != null) {
            sensorRepository.delete(sensor);
        }
        return sensorMapper.toDto(sensor);
    }
}
