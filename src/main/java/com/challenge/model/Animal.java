package com.challenge.model;

import java.util.Date;

import javax.persistence.*;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AnimalType type;

    private String imageUrl;
    @Temporal(TemporalType.TIMESTAMP)
    private Date saveedAt;

    public Animal(AnimalType type, String imageUrl) {
        this.type = type;
        this.imageUrl = imageUrl;
        this.saveedAt = new Date();
    }

    // Getters and Setters
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
    public Date getSaveedAt() {
        return saveedAt;
    }
    public void setSaveedAt(Date saveedAt) {
        this.saveedAt = saveedAt;
    }

}
