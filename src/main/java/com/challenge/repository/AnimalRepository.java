package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;

// finds last saved image
// if found return an Optional containing the Animal or empty Optional if none found
// using Spring jpa derived query method
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findFirstByTypeOrderBySavedAtDesc(AnimalType type);
    
}
