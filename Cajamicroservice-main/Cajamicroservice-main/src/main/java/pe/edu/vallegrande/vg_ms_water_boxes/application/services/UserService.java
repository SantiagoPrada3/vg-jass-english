package pe.edu.vallegrande.vg_ms_water_boxes.application.services;

import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.request.AsignarCajaRequest;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.response.BoxResponse;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<Box> getAll();

    Flux<Box> getAllActive();

    Flux<Box> getAllInactive();

    Mono<Box> getById(String id);

    Mono<BoxResponse> save(AsignarCajaRequest userRequest);

    Mono<BoxResponse> update(String id, Box box);

    Mono<Void> delete(String id);

    Mono<Box> activate(String id);

    Mono<Box> deactivate(String id);
}