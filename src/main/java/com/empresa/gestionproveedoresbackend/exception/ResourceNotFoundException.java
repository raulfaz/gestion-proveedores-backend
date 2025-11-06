package com.empresa.gestionproveedoresbackend.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado
 *
 * @author Sistema
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje
     *
     * @param message Mensaje de error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa
     *
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}