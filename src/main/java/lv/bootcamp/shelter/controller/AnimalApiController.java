package lv.bootcamp.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for shelter animal endpoints.
 * Returns JSON — does not render HTML pages.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
@Tag(name = "Animals", description = "Mnaging animals at the shelter")
@SecurityRequirement(name = "bearerAuth")
public class AnimalApiController {

    private final AnimalService animalService;

    @Operation(summary = "Get all animals", description = "Returns every animal registered in the shelter")
    @ApiResponse(responseCode = "200", description = "Full list of animals returned successfully")
    @GetMapping
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    @Operation(summary = "Get a specific animal by their ID", description = "Returns a specific animal, based on a given ID")
    @ApiResponse(responseCode = "200", description = "Animal exists and was successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Animal with such an ID does not exist", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> findById(@PathVariable Long id) {
        return animalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists adopted animals. Restricted to ROLE_ADMIN — see SecurityConfig.
     * Read-only, so it's a good endpoint for testing role-based JWT authorization:
     * calling it repeatedly (e.g. with/without a token, or with a ROLE_USER token)
     * has no side effects, unlike {@code POST /api/animals}.
     */
    @Operation(summary = "List all adopted animals", description = "Returns every animal that has been adopted")
    @ApiResponse(responseCode = "200", description = "Adopted animals successfully retrieved")
    @ApiResponse(responseCode = "403", description = "User does not have administrative privilages", content = @Content)
    @GetMapping("/adopted")
    public List<AnimalResponse> findAdopted() {
        return animalService.findAdopted();
    }

    /**
     * Creates a new animal. Restricted to ROLE_ADMIN — see SecurityConfig.
     */
    @Operation(summary = "Create an animal", description = "Adds a new animal to the shelter app")
    @ApiResponse(responseCode = "201", description = "Animal successfully cerated")
    @ApiResponse(responseCode = "400", description = "Invalid request body for the animal", content = @Content)
    @ApiResponse(responseCode = "403", description = "User does not have administrative privilages", content = @Content)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse create(@RequestBody @Valid AnimalCreateRequest request) {
        return animalService.create(request);
    }

    /**
     * Adopts an animal as the currently logged-in user. Restricted to ROLE_USER
     * (not ROLE_ADMIN) — see SecurityConfig.
     */
    @Operation(summary = "Adopt an animal", description = "Marks a specific animal as adopted")
    @ApiResponse(responseCode = "200", description = "Animal has been adopted sucessfully")
    @ApiResponse(responseCode = "404", description = "No such animal with the given ID", content = @Content)
    @ApiResponse(responseCode = "403", description = "User does not have administrative privilages", content = @Content)
    @ApiResponse(responseCode = "409", description = "The animal is already adopted", content = @Content)
    @PostMapping("/{id}/adopt")
    public ResponseEntity<AnimalResponse> adopt(@PathVariable Long id, Authentication authentication) {
        return animalService.adopt(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleAlreadyAdopted(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
