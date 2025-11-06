package com.empresa.gestionproveedoresbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO simplificado para respuestas de Producto
 * Usado en listas y referencias dentro de otros DTOs
 *
 * @author Sistema
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;

    private String codigo;

    private String nombre;

    private String unidadMedida;

    private BigDecimal precio;

    private Boolean activo;
}