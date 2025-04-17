package com.monitor.api.repository;

import com.monitor.api.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, String> {
    Optional<Unit> findByName(String name);
}
