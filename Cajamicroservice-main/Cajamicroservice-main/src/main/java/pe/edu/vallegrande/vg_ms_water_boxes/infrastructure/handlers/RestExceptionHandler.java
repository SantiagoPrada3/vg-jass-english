package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> manejarNoEncontrado(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "No encontrado");
        error.put("mensaje", ex.getMessage());
        error.put("fecha", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarGeneral(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error interno");
        error.put("mensaje", ex.getMessage());
        error.put("fecha", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}