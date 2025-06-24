package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_water_boxes.application.services.impl.BoxService;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Assignment;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.request.AssignBoxRequest;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository.BoxRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/boxes")
@CrossOrigin(origins = "*")
public class BoxRest {

    @Autowired
    private BoxService boxService;

    @Autowired
    private BoxRepository boxRepository;

    @GetMapping("/all")
    public Flux<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    @GetMapping
    public Flux<Box> getBoxes(@RequestParam(required = false) String status) {
        return boxService.getAllBoxes()
                .filter(box -> status == null || box.getStatus().equalsIgnoreCase(status))
                .doOnNext(box -> System.out.println("Box: " + box));
    }

    public Mono<Box> getBoxById(String id) {
        return boxRepository.findById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Box> createBox(@RequestBody Box box) {
        System.out.println("POST request received for box creation: " + box);
        box.setStatus("active");
        return boxService.registerBox(box)
                .doOnSuccess(savedBox -> System.out.println("Box created successfully with ID: " + savedBox.getId()))
                .doOnError(error -> System.err.println("Error creating box: " + error.getMessage()));
    }

    @PutMapping("/{id}")
    public Mono<Box> updateBox(@PathVariable String id, @RequestBody Box box) {
        box.setId(id);
        return boxService.registerBox(box);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBox(@PathVariable String id) {
        return boxRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Box not found with id: " + id)))
                .flatMap(existingBox -> {
                    existingBox.setStatus("inactive");
                    return boxRepository.save(existingBox);
                })
                .then();
    }


    @GetMapping("/user/{userId}")
    public Flux<Box> getBoxesByUserId(@PathVariable String userId) {
        return boxService.getBoxesByUserId(userId);
    }

    @PostMapping("/assign")
    public Mono<Box> assignBox(@RequestBody AssignBoxRequest request) {
        return boxService.assignBox(request);
    }

    @PatchMapping("/{id}/status")
    public Mono<Box> updateBoxStatus(@PathVariable String id, @RequestBody BoxStatusUpdate statusUpdate) {
        return boxRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Box not found with id: " + id)))
                .flatMap(existingBox -> {
                    existingBox.setStatus(statusUpdate.getStatus());
                    return boxRepository.save(existingBox);
                });
    }

    @PatchMapping("/{id}/restore")
    public Mono<Box> restoreBox(@PathVariable String id) {
        return getBoxById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Box not found with id: " + id)))
                .flatMap(existingBox -> {
                    existingBox.setStatus("active");
                    return boxService.registerBox(existingBox);
                });
    }



    public static class BoxStatusUpdate {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}