package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.model.Animal;
import com.challenge.model.AnimalType;

// TODO: Add explanation

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findFirstByTypeOrderBySavedAtDesc(AnimalType type);
    
}
