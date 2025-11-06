package com.empresa.gestionproveedoresbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir información de Proveedor
 *
 * @author Sistema
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 13, message = "El RUC no puede tener más de 13 caracteres")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    private String razonSocial;

    @Size(max = 200, message = "El nombre comercial no puede tener más de 200 caracteres")
    private String nombreComercial;

    @Size(max = 300, message = "La dirección no puede tener más de 300 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Size(max = 150, message = "El nombre del contacto no puede tener más de 150 caracteres")
    private String contacto;

    private Boolean activo;

    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaActualizacion;

    // Información adicional para vistas
    private Integer cantidadProductos;

    private Integer cantidadOrdenes;
}