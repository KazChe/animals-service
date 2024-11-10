package com.challenge.service;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;
import com.challenge.repository.AnimalRepository;
import com.challenge.dto.CatApiResponse;
import com.challenge.dto.DogApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Value("${api.url.dog}")
    private String dogApiUrl;

    @Value("${api.url.cat}")
    private String catApiUrl;

    @Value("${api.url.bear}")
    private String bearApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AnimalRepository animalRepository;

    public List<Animal> saveImages(AnimalType type, int count) {
        List<Animal> savedAnimals = new ArrayList<>();
        Set<String> usedUrls = new HashSet<>();

        for (int i = 0; i < count; i++) {
            String imageUrl = null;
            switch (type) {
                case DOG:
                    imageUrl = fetchDogImage();
                    break;
                case CAT:
                    imageUrl = fetchCatImage();
                    break;
                case BEAR:
                    imageUrl = fetchBearImage();
                    break;
            }

            if (imageUrl != null && !usedUrls.contains(imageUrl)) {
                Animal animal = new Animal(type, imageUrl);
                savedAnimals.add(animalRepository.save(animal));
                usedUrls.add(imageUrl);
                logger.info("Saved {} image URL: {}", type, imageUrl);
            }
        }
        return savedAnimals;
    }

    private String fetchDogImage() {
        try {
            DogApiResponse response = restTemplate.getForObject(dogApiUrl, DogApiResponse.class);
            return response != null ? response.getMessage() : null;
        } catch (Exception e) {
            logger.error("Error fetching dog image: {}", e.getMessage());
            return null;
        }
    }

    private String fetchCatImage() {
        try {
            CatApiResponse[] response = restTemplate.getForObject(catApiUrl, CatApiResponse[].class);
            return (response != null && response.length > 0) ? response[0].getUrl() : null;
        } catch (Exception e) {
            logger.error("Error fetching cat image: {}", e.getMessage());
            return null;
        }
    }

    private String fetchBearImage() {
        try {
            int width = 300 + new Random().nextInt(301);
            int height = 300 + new Random().nextInt(301);
            return String.format(bearApiUrl, width, height);
        } catch (Exception e) {
            logger.error("Error generating bear image URL: {}", e.getMessage());
            return null;
        }
    }

    public Animal getLatestImage(AnimalType type) {
        return animalRepository.findFirstByTypeOrderBySavedAtDesc(type)
                .orElseThrow(() -> new RuntimeException(
                        "No images found for " + type + ". Please save an image first before trying to retrieve it."));
    }
}
