package com.monitor.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a Unit.
 * This class is mapped to the "units" table in the database.
 */
@Entity
@Table(name = "units")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unit {

    /**
     * The name of the unit.
     * This field is marked as the primary key and must be unique and not null.
     * It is used to uniquely identify each unit in the "units" table.
     */
    @Id
    @Column(nullable = false, unique = true)
    private String name;
}
