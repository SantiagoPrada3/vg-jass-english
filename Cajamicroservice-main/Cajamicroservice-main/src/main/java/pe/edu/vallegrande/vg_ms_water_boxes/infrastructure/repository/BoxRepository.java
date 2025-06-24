package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BoxRepository extends ReactiveMongoRepository<Box, String> {
    Flux<Box> findByUserId(String userId);
}