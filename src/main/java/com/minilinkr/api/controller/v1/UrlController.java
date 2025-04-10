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
 * Controller class for handling URL shortening requests.
 * This class provides endpoints for creating short URLs with custom aliases.
 *
 * @RestController annotation indicates that this class is a REST controller in the Spring context.
 * @RequestMapping annotation specifies the base URL for all endpoints in this controller.
 */
@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private final UrlShorteningService service;

    public UrlController(UrlShorteningService service) {
        this.service = service;
    }

    @Operation(
            summary = "Shorten a URL with a custom alias",
            description = "Creates a URL mapping that associates the provided original URL with the custom alias. " +
                    "If the alias is already in use, an error is returned."
    )
    @ApiResponse(responseCode = "200", description = "URL shortened successfully")
    @ApiResponse(responseCode = "409", description = "Alias already exists")
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl (
            @Parameter(description = "The original URL to be shortened.", required = true)
            @RequestParam String originalUrl,
            @Parameter(description = "The custom alias for the shortened URL.", required = true)
            @RequestParam String alias
    ) {
        try {
            UrlMapping mapping = service.createCustomAlias(originalUrl, alias);
            return ResponseEntity.ok(mapping);
        } catch (IllegalAccessError ex) {
            // Return HTTP 409 Conflict if alias exists or alias is empty.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}
