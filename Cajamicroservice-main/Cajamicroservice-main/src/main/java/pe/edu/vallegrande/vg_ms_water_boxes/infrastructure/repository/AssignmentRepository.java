package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Assignment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AssignmentRepository extends ReactiveMongoRepository<Assignment, String> {
    Flux<Assignment> findByBoxId(String boxId);

    Flux<Assignment> findByUserId(String userId);
}