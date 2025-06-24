package pe.edu.vallegrande.vg_ms_water_boxes.application.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.request.AssignBoxRequest;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.exception.ResourceNotFoundException;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository.BoxRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoxService {

    private final BoxRepository repository;

    public Flux<Box> getAllBoxes() {
        log.info("Finding all boxes");
        return repository.findAll()
                .doOnComplete(() -> log.info("All boxes retrieved successfully"))
                .doOnError(e -> log.error("Error retrieving boxes: {}", e.getMessage()));
    }

    public Mono<Box> registerBox(Box box) {
        log.info("Attempting to register box: {}", box);
        box.setRegistrationDate(LocalDate.now());
        if (box.getInstallationDate() == null) {
            box.setInstallationDate(LocalDate.now());
        }
        return repository.save(box)
                .doOnSuccess(savedBox -> log.info("Box registered successfully: {}", savedBox))
                .doOnError(e -> log.error("Error registering box: {}", e.getMessage()));
    }

    public Mono<Box> assignBox(AssignBoxRequest request) {
        log.info("Attempting to assign box {} to user {}", request.getBoxId(), request.getUserId());
        return repository.findById(request.getBoxId())
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Box not found with ID: " + request.getBoxId())))
                .flatMap(box -> {
                    box.setUserId(request.getUserId());
                    return repository.save(box);
                })
                .doOnSuccess(updatedBox -> log.info("Box assigned successfully: {}", updatedBox))
                .doOnError(e -> log.error("Error assigning box: {}", e.getMessage()));
    }

    public Flux<Box> getBoxesByUserId(String userId) {
        log.info("Finding boxes for user ID: {}", userId);
        return repository.findByUserId(userId)
                .doOnComplete(() -> log.info("Boxes for user {} retrieved successfully", userId))
                .doOnError(e -> log.error("Error retrieving boxes for user: {}", e.getMessage()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No boxes found for user: " + userId)));
    }
}