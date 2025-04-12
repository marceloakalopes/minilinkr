package com.minilinkr.api.service;

import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for URL shortening functionality.
 * This class handles the business logic for creating short URLs and retrieving original URLs.
 *
 * @Service annotation indicates that this class is a service component in the Spring context.
 */
@Service
public class UrlShorteningService {

    private final UrlMappingRepository repository;

    public UrlShorteningService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new URL mapping with a custom alias.
     *
     * @param originalUrl the original URL to be shortened
     * @param customAlias the custom alias for the shortened URL
     * @return the created UrlMapping object
     * @throws IllegalArgumentException if the alias is empty or already exists
     */
    public UrlMapping createCustomAlias(String originalUrl, String customAlias) {
        // Check if alias is provided
        if (customAlias == null || customAlias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias must not be empty");
        }

        // Check if the alias is longer than 16 characters
//        if (customAlias.length() > 16) {
//            throw new IllegalArgumentException("Alias can't be longer than 16 characters");
//        }

        // Check if the alias already exists in the database
        Optional<UrlMapping> existing = repository.findByShortUrl(customAlias);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Alias already exists");
        }

        UrlMapping mapping = new UrlMapping(originalUrl);
        mapping.setShortUrl(customAlias);
        return repository.save(mapping);
    }


    /**
     * Gets the original URL for a given short URL.
     *
     * @param shortUrl the custom alias.
     * @return an Optional containing the UrlMapping object if found, or an empty Optional if not found
     */
    public Optional<UrlMapping> getOriginalUrl(String shortUrl) {
        return repository.findByShortUrl(shortUrl);
    }

    /**
     * Deletes a URL mapping by its custom alias.
     *
     * @param shortUrl the custom alias for the shortened URL
     */
    public void deleteUrlMapping(String shortUrl) {
        Optional<UrlMapping> mappingOpt = repository.findByShortUrl(shortUrl);
        if (mappingOpt.isPresent()) {
            repository.delete(mappingOpt.get());
        } else {
            throw new IllegalArgumentException("Alias not found");
        }
    }

}

// NOTE: @Service annotation is used to mark this class as a service component in the Spring context. It indicates that this class is a service layer component, which typically contains business logic and interacts with the data access layer (repositories). The @Service annotation is a specialization of the @Component annotation, allowing for better organization and clarity in the application structure. It also enables Spring to automatically detect and register this class as a bean in the application context during component scanning. This allows for dependency injection and other Spring features to be applied to this class.