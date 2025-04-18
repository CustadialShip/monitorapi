package com.monitor.api.controller;

import com.monitor.api.entity.Type;
import com.monitor.api.entity.dto.TypeDto;
import com.monitor.api.mapper.TypeMapper;
import com.monitor.api.repository.TypeRepository;
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
 * REST controller for managing Type entities.
 * Provides endpoints to create, retrieve, update, and delete types.
 */
@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypeController {

    private final TypeRepository typeRepository;
    private final TypeMapper typeMapper;

    /**
     * Creates a new Type.
     *
     * @param typeDto the DTO representation of the Type to create; must not have an ID.
     * @return the created Type as a DTO
     * @throws ResponseStatusException if the ID is not null
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public TypeDto create(@RequestBody @Valid TypeDto typeDto) {
        if (typeDto.id() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Type type = typeMapper.toEntity(typeDto);
        Type resultType = typeRepository.save(type);
        return typeMapper.toDto(resultType);
    }

    /**
     * Retrieves a specific Type by its ID.
     *
     * @param id the ID of the Type to retrieve
     * @return the Type as a DTO
     * @throws ResponseStatusException if the ID is null or the Type is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public TypeDto getOne(@PathVariable String id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        Optional<Type> typeOptional = typeRepository.findById(id);
        return typeMapper.toDto(typeOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id))));
    }

    /**
     * Retrieves a paginated list of all Types.
     *
     * @param pageable the pagination information
     * @return a PagedModel of TypeDto
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMINISTRATOR')")
    public PagedModel<TypeDto> getAll(Pageable pageable) {
        Page<Type> types = typeRepository.findAll(pageable);
        Page<TypeDto> typeDtoPage = types.map(typeMapper::toDto);
        return new PagedModel<>(typeDtoPage);
    }

    /**
     * Updates an existing Type by its ID.
     *
     * @param id the ID of the Type to update
     * @param typeDto the updated Type data; must not have an ID
     * @return the updated Type as a DTO
     * @throws ResponseStatusException if the Type is not found or the ID is not null
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public TypeDto update(@PathVariable String id, @RequestBody @Valid TypeDto typeDto) {
        if (typeDto.id() != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Id must be null");
        }
        Type type = typeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id '%s' not found".formatted(id)));
        typeMapper.updateWithNull(typeDto, type);
        Type resultType = typeRepository.save(type);
        return typeMapper.toDto(resultType);
    }

    /**
     * Deletes a Type by its ID.
     *
     * @param id the ID of the Type to delete
     * @return the deleted Type as a DTO, or null if not found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public TypeDto delete(@PathVariable String id) {
        Type type = typeRepository.findById(id).orElse(null);
        if (type != null) {
            typeRepository.delete(type);
        }
        return typeMapper.toDto(type);
    }
}
