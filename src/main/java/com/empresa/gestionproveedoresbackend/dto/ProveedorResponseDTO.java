package com.empresa.gestionproveedoresbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificado para respuestas de Proveedor
 * Usado en listas y referencias dentro de otros DTOs
 *
 * @author Sistema
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorResponseDTO {

    private Long id;

    private String ruc;

    private String razonSocial;

    private String nombreComercial;

    private Boolean activo;
}