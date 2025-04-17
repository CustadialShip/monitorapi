package com.monitor.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "units")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
    @Id
    @Column(nullable = false, unique = true)
    private String name;
}
