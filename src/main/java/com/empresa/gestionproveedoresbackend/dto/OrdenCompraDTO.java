package com.empresa.gestionproveedoresbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para transferir información de Orden de Compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraDTO {

    private Long id;

    @NotBlank(message = "El número de orden es obligatorio")
    @Size(max = 50, message = "El número de orden no puede tener más de 50 caracteres")
    private String numeroOrden;

    @NotNull(message = "La fecha de orden es obligatoria")
    private LocalDate fechaOrden;

    @NotNull(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal total;

    @Size(max = 500, message = "Las observaciones no pueden tener más de 500 caracteres")
    private String observaciones;

    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaActualizacion;

    @NotNull(message = "El proveedor es obligatorio")
    private Long proveedorId;

    // Información del proveedor para vistas
    private ProveedorResponseDTO proveedor;

    // Detalles de la orden
    @Valid
    @Builder.Default
    private List<DetalleOrdenCompraDTO> detalles = new ArrayList<>();
}