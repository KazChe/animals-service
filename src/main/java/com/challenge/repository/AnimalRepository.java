package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;

// finds most recently saved animal of specified type
// if found return an Optional containing the Animal or empty Optional if none found
// uses Spring jpa derived query method @link{https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html}
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findFirstByTypeOrderBySavedAtDesc(AnimalType type);
    
}
