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

/**
 * REST controller for managing {@link Sensor} entities.
 * <p>
 * Provides CRUD operations and filtering capabilities with role-based access control.
 */
@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new Sensor.
     * Accessible only to users with the 'ADMINISTRATOR' role.
     *
     * @param sensorDto DTO representing the sensor to create
     * @return the created sensor as a DTO
     * @throws ResponseStatusException if the provided ID is not null
     */
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

    /**
     * Retrieves a Sensor by its ID.
     * Accessible to users with 'VIEWER' or 'ADMINISTRATOR' roles.
     * Response is cached based on sensor ID.
     *
     * @param id UUID of the sensor
     * @return the found sensor as a DTO
     * @throws ResponseStatusException if not found
     */
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

    /**
     * Retrieves a paginated list of Sensors with optional filtering.
     * Accessible to users with 'VIEWER' or 'ADMINISTRATOR' roles.
     *
     * @param sensorFilter filter criteria
     * @param pageable pagination parameters
     * @return a paged list of Sensor DTOs
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public PagedModel<SensorDto> getAll(@ModelAttribute SensorFilter sensorFilter, Pageable pageable) {
        Page<Sensor> units = sensorRepository.findAll(sensorFilter.toSpecification(), pageable);
        Page<SensorDto> unitDtoPage = units.map(sensorMapper::toDto);
        return new PagedModel<>(unitDtoPage);
    }

    /**
     * Fully updates a Sensor.
     * Accessible only to users with the 'ADMINISTRATOR' role.
     * Invalidates the cache for the updated sensor.
     *
     * @param id ID of the sensor to update
     * @param sensorDto new sensor data
     * @return the updated sensor as a DTO
     * @throws ResponseStatusException if sensor not found or ID in DTO is not null
     */
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

    /**
     * Partially updates a Sensor using JSON patch.
     * Accessible only to users with the 'ADMINISTRATOR' role.
     * Invalidates the cache for the updated sensor.
     *
     * @param id ID of the sensor to patch
     * @param patchNode JSON node with patch data
     * @return the updated sensor as a DTO
     * @throws IOException if patch application fails
     * @throws ResponseStatusException if sensor not found
     */
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

    /**
     * Deletes a Sensor by its ID.
     * Accessible only to users with the 'ADMINISTRATOR' role.
     * Invalidates the cache for the deleted sensor.
     *
     * @param id ID of the sensor to delete
     * @return the deleted sensor as a DTO or null if not found
     */
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
