package com.minilinkr.api.controller.v1;

import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

/**
 * RedirectController handles the redirection of shortened URLs to their original URLs.
 * It uses the UrlShorteningService to retrieve the original URL based on the provided custom alias.
 */
@RestController
public class RedirectController {

    private final UrlShorteningService service;

    public RedirectController(UrlShorteningService service) {
        this.service = service;
    }

    @Operation(
            summary = "Redirect to the original URL",
            description = "Redirects the client to the original URL based on the provided custom alias."
    )
    @ApiResponse(responseCode = "302", description = "Redirection successful")
    @ApiResponse(responseCode = "404", description = "Alias not found")
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirect (
            @Parameter(description = "The custom alias for the shortened URL.", required = true)
            @PathVariable String shortUrl,
            HttpServletResponse response) throws IOException {

        Optional<UrlMapping> mappingOpt = service.getOriginalUrl(shortUrl);
        if (mappingOpt.isPresent()) {
            response.sendRedirect(mappingOpt.get().getOriginalUrl());
            return null;
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alias not found");
    }
}
