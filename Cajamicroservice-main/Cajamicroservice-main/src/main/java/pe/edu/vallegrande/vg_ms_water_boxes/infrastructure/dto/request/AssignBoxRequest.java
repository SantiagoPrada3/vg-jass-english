package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignBoxRequest {
    private String boxId;
    private String userId;
}