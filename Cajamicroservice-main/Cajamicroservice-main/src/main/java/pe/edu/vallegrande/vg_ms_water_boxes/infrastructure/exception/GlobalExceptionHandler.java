package pe.edu.vallegrande.vg_ms_water_boxes.infrastructure.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
            WebProperties.Resources resources,
            ApplicationContext applicationContext,
            ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        Map<String, Object> errorPropertiesMap = new HashMap<>();

        HttpStatus status = determineHttpStatus(error);

        errorPropertiesMap.put("status", status.value());
        errorPropertiesMap.put("error", status.getReasonPhrase());
        errorPropertiesMap.put("message", error.getMessage());
        errorPropertiesMap.put("path", request.path());

        if (error instanceof MethodNotAllowedException) {
            errorPropertiesMap.put("detail", "El método " + ((MethodNotAllowedException) error).getHttpMethod() +
                    " no está permitido para este endpoint. Métodos permitidos: " +
                    ((MethodNotAllowedException) error).getSupportedMethods());
        }

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof MethodNotAllowedException) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        } else if (error instanceof ResponseStatusException) {
            return HttpStatus.valueOf(((ResponseStatusException) error).getStatusCode().value());
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Configuration
    public static class ErrorAttributesConfig {
        @Bean
        public WebProperties.Resources resources() {
            return new WebProperties.Resources();
        }
    }
}