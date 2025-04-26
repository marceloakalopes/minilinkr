package com.minilinkr.api.service;

import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * @param urlMapping the UrlMapping object containing the original URL and custom alias
     * @throws IllegalArgumentException if the alias is empty or already exists
     */
    public void createCustomAlias(UrlMapping urlMapping) {
        // Check if alias is provided
        if (urlMapping.getAlias() == null || urlMapping.getAlias().isEmpty()) {
            throw new IllegalArgumentException("Alias can't be empty");
        }

        // Check if the alias already exists in the database
        if (repository.findByAlias(urlMapping.getAlias()).isPresent()) {
            throw new IllegalArgumentException("Alias already exists");
        }

        repository.save(urlMapping);
    }


    /**
     * Gets the original URL for a given short URL.
     *
     * @param alias the custom alias.
     * @return an Optional containing the UrlMapping object if found, or an empty Optional if not found
     */
    public Optional<UrlMapping> getOriginalUrl(String alias) {
        return repository.findByAlias(alias);
    }

    /**
     * Deletes a URL mapping by its custom alias.
     *
     * @param alias the custom alias for the shortened URL
     */
    public void deleteUrlMapping(String alias) {
        Optional<UrlMapping> mappingOpt = repository.findByAlias(alias);
        if (mappingOpt.isPresent()) {
            repository.delete(mappingOpt.get());
        } else {
            throw new IllegalArgumentException("Alias not found");
        }
    }

    /**
     * Retrieves all URL mappings.
     *
     * @return a list of all UrlMapping objects
     */
    public List<UrlMapping> getAllUrlMappings() {
        return repository.findAll();
    }

}

// NOTE: @Service annotation is used to mark this class as a service component in the Spring context. It indicates that this class is a service layer component, which typically contains business logic and interacts with the data access layer (repositories). The @Service annotation is a specialization of the @Component annotation, allowing for better organization and clarity in the application structure. It also enables Spring to automatically detect and register this class as a bean in the application context during component scanning. This allows for dependency injection and other Spring features to be applied to this class.