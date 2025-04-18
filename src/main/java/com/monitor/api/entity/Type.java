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
 * Entity class representing a Type.
 * This class is mapped to the "types" table in the database.
 */
@Entity
@Table(name = "types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Type {

    /**
     * The name of the type.
     * This field is marked as the primary key and must be unique and not null.
     * It is used to uniquely identify each type in the "types" table.
     */
    @Id
    @Column(nullable = false, unique = true)
    private String name;
}