package com.challenge.controller;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;
import com.challenge.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/save/{type}")
    public ResponseEntity<List<String>> saveImages(
            @PathVariable AnimalType type,
            @RequestParam(defaultValue = "1") int count) {
        List<String> imageUrls = imageService.saveImages(type, count)
                .stream()
                .map(Animal::getImageUrl)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }

    @GetMapping("/latest/{type}")
    public ResponseEntity<String> getLatestImage(@PathVariable AnimalType type) {
        Animal animal = imageService.getLatestImage(type);
        return ResponseEntity.ok(animal.getImageUrl());
    }
}