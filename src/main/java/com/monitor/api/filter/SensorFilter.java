package com.monitor.api.filter;

import com.monitor.api.entity.Sensor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * A filter class for filtering {@link Sensor} entities based on various criteria.
 * This class constructs a {@link Specification} to filter sensors by name and model.
 * It uses the {@link Specification} API to define the filtering logic for the {@link Sensor} entity.
 */
public record SensorFilter(String nameContains, String modelContains) {

    /**
     * Converts the filter criteria to a {@link Specification} for querying {@link Sensor} entities.
     * This method combines multiple filter specifications using logical AND.
     *
     * @return A {@link Specification} for querying {@link Sensor} entities based on the filter criteria.
     */
    public Specification<Sensor> toSpecification() {
        return Specification.where(nameContainsSpec())
                .and(modelContainsSpec());
    }

    /**
     * Creates a {@link Specification} for filtering sensors by name.
     * The specification applies a "like" filter if the nameContains field is not empty.
     *
     * @return A {@link Specification} that applies a "like" filter on the sensor's name.
     */
    private Specification<Sensor> nameContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameContains)
                ? cb.like(root.get("name"), "%" + nameContains + "%")
                : null);
    }

    /**
     * Creates a {@link Specification} for filtering sensors by model.
     * The specification applies a "like" filter if the modelContains field is not empty.
     *
     * @return A {@link Specification} that applies a "like" filter on the sensor's model.
     */
    private Specification<Sensor> modelContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(modelContains)
                ? cb.like(root.get("model"), "%" + modelContains + "%")
                : null);
    }
}