package com.minilinkr.api.repository;

import com.minilinkr.api.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository  extends JpaRepository<UrlMapping, Long> {

    /**
     * Finds a UrlMapping by its custom alias.
     *
     * @param alias the custom alias for the shortened URL
     * @return an Optional containing the UrlMapping if found, or an empty Optional if not found
     */
    Optional<UrlMapping> findByAlias(String alias);

    /**
     * Deletes a UrlMapping by its custom alias.
     *
     * @param alias the custom alias for the shortened URL
     */
    void deleteByAlias(String alias);
}