package com.monitor.api.repository;

import com.monitor.api.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, String> {
    Optional<Type> findByName(String name);
}
