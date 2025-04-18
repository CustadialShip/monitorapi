package com.monitor.api.mapper;

import com.monitor.api.entity.Sensor;
import com.monitor.api.entity.Type;
import com.monitor.api.entity.Unit;
import com.monitor.api.entity.dto.SensorDto;
import org.mapstruct.*;

/**
 * Mapper interface for converting between {@link Sensor} entities and {@link SensorDto} data transfer objects (DTOs).
 * It uses the MapStruct framework to generate the mapping implementation.
 * This interface also handles custom mappings for related entities like {@link Type} and {@link Unit}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SensorMapper {

    Sensor toEntity(SensorDto sensorDto);

    SensorDto toDto(Sensor sensor);

    Sensor updateWithNull(SensorDto sensorDto, @MappingTarget Sensor sensor);

    default Type mapStringToType(String typeValue) {
        if (typeValue == null) return null;
        Type type = new Type();
        type.setName(typeValue);
        return type;
    }

    default String mapTypeToString(Type type) {
        return type != null ? type.getName() : null;
    }

    default Unit mapStringToUnit(String unitValue) {
        if (unitValue == null) return null;
        Unit unit = new Unit();
        unit.setName(unitValue);
        return unit;
    }

    default String mapUnitToString(Unit unit) {
        return unit != null ? unit.getName() : null;
    }
}
