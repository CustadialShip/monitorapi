package com.monitor.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

/**
 * Entity class representing a Sensor.
 * This class is mapped to the "sensors" table in the database.
 */
@Entity
@Table(name = "sensors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sensor extends AbstractPersistable<UUID> {

    /**
     * The name of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The model of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String model;

    /**
     * The minimum range of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private Long rangeFrom;

    /**
     * The maximum range of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private Long rangeTo;

    /**
     * The unit associated with the sensor.
     * This field is mapped to the "unit" column and links to the {@link Unit} entity.
     */
    @ManyToOne
    @JoinColumn(name = "unit")
    private Unit unit;

    /**
     * The type of the sensor.
     * This field is mapped to the "type" column and links to the {@link Type} entity.
     * This field is required and cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private Type type;

    /**
     * The location of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String location;

    /**
     * A description of the sensor.
     * This field is required and cannot be null.
     */
    @Column(nullable = false)
    private String description;
}