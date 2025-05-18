package com.minilinkr.api.controller.v1;

import com.minilinkr.api.model.ApiSuccessResponse;
import com.minilinkr.api.model.UrlMapping;
import com.minilinkr.api.service.UrlShorteningService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * Controller for managing URL mappings.
 * Provides endpoints to create, retrieve, and delete URL mappings.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "MiniLinkr URL Shortener API",
                version = "1.0",
                description = "Manage and resolve custom URL aliases.",
                contact = @Contact(
                        name = "Marcelo Lopes",
                        url = "https://github.com/marceloakalopes"
                )
        )
)
@CrossOrigin
@RestController
@RequestMapping("/api/v1/urls")
@Tag(name = "URL Mappings", description = "Operations on URL mappings")
public class UrlController {

    /**
     * List of forbidden aliases that cannot be used for URL mappings.
     */
    private final List<String> forbiddenAliases = List.of("docs", "api", "v1", "v2", "v3", "admin", "login", "register");

    /**
     * The URL shortening service used to manage URL mappings.
     */
    private final UrlShorteningService service;

    /**
     * Constructor for UrlController.
     *
     * @param service The URL shortening service to be used.
     */
    public UrlController(UrlShorteningService service) {
        this.service = service;
    }

    /**
     * Retrieves all URL mappings.
     *
     * @return A list of all URL mappings.
     */
    @Operation(
            summary = "List all URL mappings",
            description = "Retrieve every URL mapping currently stored",
            tags = { "find" }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "A JSON array of URLMapping objects",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UrlMapping.class)
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<UrlMapping>> getAll() {
        return ResponseEntity.ok(service.getAllUrlMappings());
    }

    /**
     * Retrieves a URL mapping by its alias.
     *
     * @param alias The custom alias used in the short URL.
     * @return The URL mapping associated with the given alias.
     */
    @Operation(
            summary = "Get a URL mapping by alias",
            description = "Look up the original URL for a given short alias",
            tags = { "find" }
    )
    @Parameter(
            name = "alias",
            description = "The custom alias used in the short URL",
            required = true,
            in = ParameterIn.PATH,
            example = "docs"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "URL mapping found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UrlMapping.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alias not found"
            )
    })
    @GetMapping("/{alias}")
    public ResponseEntity<UrlMapping> findByAlias(@PathVariable String alias) {
        return service.getOriginalUrl(alias)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alias not found"));
    }

    /**
     * Creates a new URL mapping with a custom alias.
     *
     * @param mapping The URL mapping to be created.
     */
    @Operation(
            summary = "Create a new URL mapping",
            description = "Submit an original URL and a desired alias to create a short link",
            tags = { "create" }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Payload containing the original URL and desired alias",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UrlMapping.class),
                    examples = @ExampleObject(
                            name = "CreateExample",
                            summary = "Create a short link for `https://example.com`",
                            value = "{ \"originalUrl\": \"https://example.com\", \"alias\": \"exmpl\" }"
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "URL mapping successfully created"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Alias already in use"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAlias( @Valid @RequestBody UrlMapping mapping) {
        try {
//            System.out.println("Alias: " + mapping.getAlias());
//            System.out.println("Original URL: " + mapping.getOriginalUrl());
            service.createCustomAlias(mapping);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Alias already exists");
        }
    }

    /**
     * Deletes a URL mapping by its alias.
     *
     * @param alias The custom alias used in the short URL.
     * @return A success response indicating the deletion was successful.
     */
    @Operation(
            summary = "Delete a URL mapping",
            description = "Remove an existing URL alias",
            tags = { "delete" }
    )
    @Parameter(
            name = "alias",
            description = "The custom alias to delete",
            required = true,
            in = ParameterIn.PATH,
            example = "exmpl"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Deletion successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiSuccessResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alias not found"
            )
    })
    @DeleteMapping("/{alias}")
    public ResponseEntity<ApiSuccessResponse> deleteUrlMapping(@PathVariable String alias) {


        // Check if the alias is forbidden
        if (forbiddenAliases.contains(alias)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to use or delete this alias");
        }

        try {
            service.deleteUrlMapping(alias);
            Map<String,Object> meta = Map.of(
                    "messages",
                    Collections.singletonList("[INFO] The alias was deleted successfully. Learn more: https://docs.minilinkr.com"),
                    "deleted_alias", alias
            );
            return ResponseEntity.ok(new ApiSuccessResponse(true, "deleted", meta));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alias not found");
        }
    }
}
