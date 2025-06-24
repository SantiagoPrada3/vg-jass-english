package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BoxResponse {
    private String id;
    private String code;
    private String type;
    private LocalDate installationDate;
    private String status;
    private String observations;
    private LocalDate registrationDate;
    private String userId;
}