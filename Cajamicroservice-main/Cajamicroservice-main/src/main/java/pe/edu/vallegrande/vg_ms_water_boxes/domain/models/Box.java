package pe.edu.vallegrande.vg_ms_water_boxes.domain.models;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cajas")
public class Box {
    @Id
    private String id;

    @Field("codigo")
    private String code;

    @Field("tipo")
    private String type;

    @Field("fechaInstalacion")
    private LocalDate installationDate;


    @Field("observaciones")
    private String observations;

    @Field("fechaRegistro")
    private LocalDate registrationDate;

    @Field("estado")
    private String status;

    @Field("usuarioId")
    private String userId;

    @Override
    public String toString() {
        return "Box{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", installationDate=" + installationDate +
                ", observations='" + observations + '\'' +
                ", registrationDate=" + registrationDate +
                ", userId='" + userId + '\'' +
                '}';
    }
}