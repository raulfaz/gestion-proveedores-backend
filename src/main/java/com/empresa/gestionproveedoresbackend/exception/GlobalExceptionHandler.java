package com.empresa.gestionproveedoresbackend.exception;

import com.empresa.gestionproveedoresbackend.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación
 * Captura y formatea las excepciones de manera consistente
 *
 * @author Sistema
 * @version 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando un recurso no es encontrado
     *
     * @param ex Excepción lanzada
     * @param request Petición web
     * @return Respuesta con código 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {

        log.error("Recurso no encontrado: {}", ex.getMessage());

        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de argumentos ilegales (validaciones de negocio)
     *
     * @param ex Excepción lanzada
     * @param request Petición web
     * @return Respuesta con código 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        log.error("Argumento ilegal: {}", ex.getMessage());

        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de estado ilegal (operaciones no permitidas)
     *
     * @param ex Excepción lanzada
     * @param request Petición web
     * @return Respuesta con código 409
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleIllegalStateException(
            IllegalStateException ex,
            WebRequest request) {

        log.error("Estado ilegal: {}", ex.getMessage());

        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de validación de datos
     *
     * @param ex Excepción de validación
     * @return Respuesta con código 400 y detalles de errores
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.error("Errores de validación encontrados");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponseDTO<Map<String, String>> response = ApiResponseDTO.error(
                "Error de validación en los datos enviados",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier otra excepción no contemplada
     *
     * @param ex Excepción lanzada
     * @param request Petición web
     * @return Respuesta con código 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGlobalException(
            Exception ex,
            WebRequest request) {

        log.error("Error interno del servidor: ", ex);

        ApiResponseDTO<Object> response = ApiResponseDTO.error(
                "Ha ocurrido un error interno en el servidor. Por favor, contacte al administrador."
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}