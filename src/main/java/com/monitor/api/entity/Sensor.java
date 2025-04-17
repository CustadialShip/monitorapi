package com.monitor.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sensor extends AbstractPersistable<UUID> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Long rangeFrom;

    @Column(nullable = false)
    private Long rangeTo;

    @ManyToOne
    @JoinColumn(name = "unit")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false)
    private Type type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;

}
