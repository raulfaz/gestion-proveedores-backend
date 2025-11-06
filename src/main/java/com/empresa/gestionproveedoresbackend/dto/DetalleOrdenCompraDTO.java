package com.empresa.gestionproveedoresbackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferir información de Detalle de Orden de Compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrdenCompraDTO {

    private Long id;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    // Información del producto para vistas
    private ProductoResponseDTO producto;

    // ID de la orden (opcional, se usa en creación/actualización)
    private Long ordenCompraId;
}