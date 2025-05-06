package com.minilinkr.api.controller.v1;

import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;


@RestController
public class RedirectController {

    private final String DEFAULT_REDIRECT_URL = "https://minilinkr.com";

    private final UrlShorteningService service;

    public RedirectController(UrlShorteningService service) {
        this.service = service;
    }

    @Tag(name = "redirect")
    @Operation(
            summary = "Redirect to the default URL",
            description = "Redirects the client to the default URL."
    )
    @ApiResponse(responseCode = "302", description = "Redirection successful")
    @GetMapping
    public ResponseEntity<Void> redirectToDefault(HttpServletResponse response) throws IOException {
        response.sendRedirect(DEFAULT_REDIRECT_URL);
        return null;
    }

    @Tag(name = "redirect")
    @Operation(
            summary = "Redirect to the original URL",
            description = "Redirects the client to the original URL based on the provided custom alias."
    )
    @ApiResponse(responseCode = "302", description = "Redirection successful")
    @ApiResponse(responseCode = "404", description = "Alias not found")
    @GetMapping("/{alias}")
    public ResponseEntity<Void> redirect (
            @Parameter(description = "The custom alias for the shortened URL.", required = true)
            @PathVariable String alias,
            HttpServletResponse response) throws IOException {

        Optional<UrlMapping> mappingOpt = service.getOriginalUrl(alias);
        if (mappingOpt.isPresent()) {
            response.sendRedirect(mappingOpt.get().getOriginalUrl());
            return null;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
