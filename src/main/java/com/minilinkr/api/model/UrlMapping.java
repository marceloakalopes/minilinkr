package com.minilinkr.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalTime;

@Entity
@Table(name = "url_mappings")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Auto-generated ID", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "originalUrl must not be blank")
    @URL(message = "originalUrl must be a valid URL")
    @Column(nullable = false)
    private String originalUrl;

    @NotBlank(message = "shortUrl must not be blank")
    // allow only letters, numbers, dash or underscore, 3–30 chars
    @Pattern(
            regexp = "^[A-Za-z0-9_-]{3,30}$",
            message = "shortUrl must be 3–30 characters, letters/numbers/_/− only"
    )
    @Column(unique = true, nullable = false)
    private String alias;

    @CreationTimestamp
    @Schema(description = "Timestamp when created", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private LocalTime createdAt;

    public UrlMapping() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String shortUrl) {
        this.alias = shortUrl;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalTime createdAt) {
        this.createdAt = createdAt;
    }
}
