package com.monitor.api.controller;

import com.monitor.api.entity.Unit;
import com.monitor.api.entity.dto.UnitDto;
import com.monitor.api.mapper.UnitMapper;
import com.monitor.api.repository.UnitRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * REST controller for managing Unit entities.
 * Provides endpoints for creating, retrieving, updating, and deleting units.
 */
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    /**
     * Creates a new Unit.
     *
     * @param unitDto the DTO representation of the Unit to create; must not have an ID
     * @return the created Unit as a DTO
     * @throws ResponseStatusException if the ID is not null
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UnitDto create(@RequestBody @Valid UnitDto unitDto) {
        if (unitDto.id() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Unit unit = unitMapper.toEntity(unitDto);
        Unit resultUnit = unitRepository.save(unit);
        return unitMapper.toDto(resultUnit);
    }

    /**
     * Retrieves a Unit by its ID.
     *
     * @param id the ID of the Unit to retrieve
     * @return the Unit as a DTO
     * @throws ResponseStatusException if the ID is null or the Unit is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public UnitDto getOne(@PathVariable String id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        Optional<Unit> unitOptional = unitRepository.findById(id);
        return unitMapper.toDto(unitOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id))));
    }

    /**
     * Retrieves a paginated list of all Units.
     *
     * @param pageable the pagination information
     * @return a PagedModel of UnitDto
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public PagedModel<UnitDto> getAll(Pageable pageable) {
        Page<Unit> units = unitRepository.findAll(pageable);
        Page<UnitDto> unitDtoPage = units.map(unitMapper::toDto);
        return new PagedModel<>(unitDtoPage);
    }

    /**
     * Updates an existing Unit by its ID.
     *
     * @param id the ID of the Unit to update
     * @param unitDto the updated Unit data; must not have an ID
     * @return the updated Unit as a DTO
     * @throws ResponseStatusException if the Unit is not found or the DTO contains an ID
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UnitDto update(@PathVariable String id, @RequestBody @Valid UnitDto unitDto) {
        if (unitDto.id() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Unit unit = unitRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id)));
        unitMapper.updateWithNull(unitDto, unit);
        Unit resultUnit = unitRepository.save(unit);
        return unitMapper.toDto(resultUnit);
    }

    /**
     * Deletes a Unit by its ID.
     *
     * @param id the ID of the Unit to delete
     * @return the deleted Unit as a DTO, or null if not found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UnitDto delete(@PathVariable String id) {
        Unit unit = unitRepository.findById(id).orElse(null);
        if (unit != null) {
            unitRepository.delete(unit);
        }
        return unitMapper.toDto(unit);
    }
}
