package pe.edu.vallegrande.vg_ms_water_boxes.domain.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "asignaciones")
public class Assignment {
    @Id
    private String id;

    @Field("cajaId")
    private String boxId;

    @Field("usuarioId")
    private String userId;

    @Field("fechaAsignacion")
    private Date assignmentDate;

    @Field("observaciones")
    private String observations;

    @Field("fechaRegistro")
    private Date registrationDate;

    @Field("estado")
    private String status;

    @Field("usuarioDatos")
    private UserData userData;

    @Field("cajaDatos")
    private BoxData boxData;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserData {
        @Field("nombreCompleto")
        private String fullName;

        @Field("documento")
        private String document;

        @Field("direccion")
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoxData {
        @Field("codigo")
        private String code;

        @Field("tipo")
        private String type;
    }
}