package com.monitor.api.repository;

import com.monitor.api.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, UUID>, JpaSpecificationExecutor<Sensor> {
    Optional<Sensor> findById(UUID id);
}
