package com.challenge.controller;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;
import com.challenge.service.ImageService;
import com.challenge.exception.ImageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/images") // TODO: maybe change to /v1/images later
public class ImageController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ImageController.class);
    private static final int MAX_COUNT = 10; // hardcoded for now

    @Autowired
    private ImageService imageService;

    // basic cache to prevent hamering the external APIs
    private Map<AnimalType, List<String>> recentUrlCache = new HashMap<>();

    @PostMapping("/save/{type}")
    public ResponseEntity<List<String>> saveImages(
            @PathVariable AnimalType type,
            @RequestParam(defaultValue = "1") int count) {

        // sanitize input
        count = Math.min(count, MAX_COUNT);

        logger.debug("Saving {} {} images", count, type); // for prod debugging

        List<String> imageUrls = imageService.saveImages(type, count)
                .stream()
                .map(Animal::getImageUrl)
                .collect(Collectors.toList());
        // TODO: add cache laters
        // update cache
        // recentUrlCache.put(type, imageUrls);

        return ResponseEntity.ok(imageUrls);
    }

    // quick endpoint for testing
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/latest/{type}")
    public ResponseEntity<String> getLatestImage(@PathVariable AnimalType type) {
        try {
            // debug log
            logger.debug("Fetching latest {} image", type);

            Animal animal = imageService.getLatestImage(type);
            if (animal == null) {
                throw new ImageNotFoundException("No saved images found for type: " + type);
            }
            return ResponseEntity.ok(animal.getImageUrl());
        } catch (ImageNotFoundException e) {
            logger.warn("No image found for type: {}", type);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching latest image for type: {}", type, e);
            throw new RuntimeException("Failed to fetch latest image");
        }
    }
}