package com.challenge.service;

import com.challenge.models.Animal;
import com.challenge.models.AnimalType;
import com.challenge.repository.AnimalRepository;
import com.challenge.dto.CatApiResponse;
import com.challenge.dto.DogApiResponse;
import com.challenge.exception.ImageNotFoundException;

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

    // TODO: refactor this - getting too complex
    public List<Animal> saveImages(AnimalType type, int count) {
        List<Animal> savedAnimals = new ArrayList<>();
        // hacky way to prevent duplicates but works for now
        Set<String> usedUrls = new HashSet<>();
        
        // temp debug counter
        int attempts = 0;
        final int MAX_ATTEMPTS = 20; // in case API fails

        for (int i = 0; i < count; i++) {
            String imgUrl = null;  // bad name but whatever
            
            // probably should use strategy pattern here...
            switch (type) {
                case DOG:
                    imgUrl = fetchDogImage();
                    break;
                case CAT:
                    imgUrl = fetchCatImage();
                    break;
                case BEAR:
                    // FIXME: sometimes returns broken images
                    imgUrl = fetchBearImage();
                    break;
            }

            if (imgUrl != null && !usedUrls.contains(imgUrl)) {
                Animal animal = new Animal(type, imgUrl);
                savedAnimals.add(animalRepository.save(animal));
                usedUrls.add(imgUrl);
                logger.debug("Saved {} image: {}", type, imgUrl);  // for debugging
            } else {
                i--; // try again if we got a duplicate
                attempts++;
                if (attempts > MAX_ATTEMPTS) {
                    logger.warn("Too many failed attempts to fetch unique images");
                    break;
                }
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

    // cat api trurns an array of josn e.g
    // [{"id":"abc","url":"https://cdn2.thecatapi.com/images/abc.jpg","width":768,"height":1024}]
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
            .orElseThrow(() -> new ImageNotFoundException(
                "No images found for type: " + type + ". Try saving some images first."
            ));
    }
}
