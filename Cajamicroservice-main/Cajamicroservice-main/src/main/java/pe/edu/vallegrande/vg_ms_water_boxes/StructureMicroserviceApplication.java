package pe.edu.vallegrande.vg_ms_water_boxes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.vg_ms_water_boxes.domain.models.Box;
import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.repository.BoxRepository;

import java.time.LocalDate;

@SpringBootApplication
@OpenAPIDefinition
@Slf4j
public class StructureMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StructureMicroserviceApplication.class, args);
    }
}