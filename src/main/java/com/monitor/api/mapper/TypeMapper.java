package com.monitor.api.mapper;

import com.monitor.api.entity.Type;
import com.monitor.api.entity.dto.TypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link Type} entities and {@link TypeDto} data transfer objects (DTOs).
 * It uses the MapStruct framework to generate the mapping implementation.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TypeMapper {
    Type toEntity(TypeDto typeDto);

    TypeDto toDto(Type type);

    Type updateWithNull(TypeDto typeDto, @MappingTarget Type type);
}
