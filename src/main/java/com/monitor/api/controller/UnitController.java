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

@RestController("/api/units")
@RequiredArgsConstructor
@RequestMapping
public class UnitController {

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public PagedModel<UnitDto> getAll(Pageable pageable) {
        Page<Unit> units = unitRepository.findAll(pageable);
        Page<UnitDto> unitDtoPage = units.map(unitMapper::toDto);
        return new PagedModel<>(unitDtoPage);
    }

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
