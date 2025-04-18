package com.monitor.api.mapper;

import com.monitor.api.entity.Unit;
import com.monitor.api.entity.dto.UnitDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between {@link Unit} entities and {@link UnitDto} data transfer objects (DTOs).
 * It uses the MapStruct framework to generate the mapping implementation.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UnitMapper {
    Unit toEntity(UnitDto unitDto);

    UnitDto toDto(Unit unit);

    Unit updateWithNull(UnitDto unitDto, @MappingTarget Unit unit);
}
