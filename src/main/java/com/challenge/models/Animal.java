package com.challenge.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "animals") // might need to rename this table later
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalType type;
    // TODO: add validation for URL format
    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public Animal() {
        this.savedAt = LocalDateTime.now();
    }
    // main constructor we actually use
    public Animal(AnimalType type, String imageUrl) {
        this();
        this.type = type;
        this.imageUrl = imageUrl;
    }
    
    // basic getter/setters below
    // might haveto add more validation later
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}