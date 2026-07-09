package lv.bootcamp.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lv.bootcamp.shelter.model.AnimalType;

/**
 * JSON request body for creating a new animal via the REST API.
 * Status is not included; all new animals start as AVAILABLE.
 */
public record AnimalCreateRequest(

        @Schema(description = "Animal's name", example = "Jonathan")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Type of the animal", example = "CAT")
        @NotNull(message = "Type is required")
        AnimalType type,

        @Schema(description = "Animal's breed", example = "Orange")
        String breed,

        @Schema(description = "Animal's age", example = "2")
        @Min(value = 0, message = "Age cannot be negative")
        Integer age,

        @Schema(description = "Short text describing the animal")
        String description,

        @Schema(description = "Filename of the animal's image", example = "Jonathan.jpg")
        String imageUrl
) {}