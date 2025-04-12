package com.minilinkr.api.controller.v1;

import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller class that handles all operations for URL mappings.
 * The base path `/v1/urls` is used for both creating shortened URLs and redirecting.
 */
@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlShorteningService service;

    public UrlController(UrlShorteningService service) {
        this.service = service;
    }

    /**
     * POST endpoint to create a new URL mapping.
     *
     * Example usage:
     * POST https://linnkr.co/api/v1/urls?longUrl=http://example.com&alias=customAlias
     *
     * @param longUrl The original URL to be shortened.
     * @param alias   The custom alias for the shortened URL.
     * @return The created UrlMapping in case of success or a conflict error if the alias already exists.
     */
    @Operation(
            summary = "Shorten a URL with a custom alias",
            description = "Creates a URL mapping that associates the provided long URL with the custom alias. " +
                    "If the alias is already in use, an error is returned."
    )
    @ApiResponse(responseCode = "200", description = "URL shortened successfully")
    @ApiResponse(responseCode = "409", description = "Alias already exists")
    @PostMapping
    public ResponseEntity<?> createUrlMapping(
            @Parameter(description = "The original URL to be shortened", required = true)
            @RequestParam("longUrl") String longUrl,
            @Parameter(description = "The custom alias for the shortened URL", required = true)
            @RequestParam("alias") String alias) {
        try {
            UrlMapping mapping = service.createCustomAlias(longUrl, alias);
            return ResponseEntity.ok(mapping);
        } catch (IllegalAccessError ex) {
            // In case the alias exists or any other business rule violation occurs.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    /**
     * DELETE endpoint to delete a URL mapping.
     *
     * Example usage:
     * DELETE https://linnkr.co/api/v1/urls/customAlias
     *
     * @param alias The custom alias for the shortened URL.
     * @return A success message if the mapping is deleted, or a not found error if the alias does not exist.
     */
    @Operation(
            summary = "Delete a URL mapping",
            description = "Deletes the URL mapping associated with the provided custom alias."
    )
    @ApiResponse(responseCode = "200", description = "URL mapping deleted successfully")
    @ApiResponse(responseCode = "404", description = "Alias not found")
    @DeleteMapping("/{alias}")
    public ResponseEntity<?> deleteUrlMapping(
            @Parameter(description = "The custom alias for the shortened URL", required = true)
            @PathVariable("alias") String alias) {
        try {
            service.deleteUrlMapping(alias);
            return ResponseEntity.ok("URL mapping deleted successfully");
        } catch (IllegalAccessError ex) {
            // In case the alias does not exist or any other business rule violation occurs.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
