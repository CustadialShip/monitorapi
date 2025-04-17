package com.monitor.api.filter;

import com.monitor.api.entity.Sensor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public record SensorFilter(String nameContains, String modelContains) {
    public Specification<Sensor> toSpecification() {
        return Specification.where(nameContainsSpec())
                .and(modelContainsSpec());
    }

    private Specification<Sensor> nameContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameContains)
                ? cb.like(root.get("name"), "%" + nameContains + "%")
                : null);
    }

    private Specification<Sensor> modelContainsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(modelContains)
                ? cb.like(root.get("model"), "%" + modelContains + "%")
                : null);
    }
}