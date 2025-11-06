package com.empresa.gestionproveedoresbackend.util;

import com.empresa.gestionproveedoresbackend.dto.ProveedorDTO;
import com.empresa.gestionproveedoresbackend.dto.ProveedorResponseDTO;
import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Proveedor y sus DTOs
 *
 * @author Sistema
 * @version 1.0.0
 */
@Component
public class ProveedorMapper {

    /**
     * Convierte una entidad Proveedor a ProveedorDTO
     *
     * @param proveedor Entidad a convertir
     * @return DTO con los datos del proveedor
     */
    public ProveedorDTO toDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return ProveedorDTO.builder()
                .id(proveedor.getId())
                .ruc(proveedor.getRuc())
                .razonSocial(proveedor.getRazonSocial())
                .nombreComercial(proveedor.getNombreComercial())
                .direccion(proveedor.getDireccion())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .contacto(proveedor.getContacto())
                .activo(proveedor.getActivo())
                .fechaRegistro(proveedor.getFechaRegistro())
                .fechaActualizacion(proveedor.getFechaActualizacion())
                .cantidadProductos(proveedor.getProductos() != null ? proveedor.getProductos().size() : 0)
                .cantidadOrdenes(proveedor.getOrdenesCompra() != null ? proveedor.getOrdenesCompra().size() : 0)
                .build();
    }

    /**
     * Convierte un ProveedorDTO a entidad Proveedor
     *
     * @param dto DTO con los datos
     * @return Entidad Proveedor
     */
    public Proveedor toEntity(ProveedorDTO dto) {
        if (dto == null) {
            return null;
        }

        return Proveedor.builder()
                .id(dto.getId())
                .ruc(dto.getRuc())
                .razonSocial(dto.getRazonSocial())
                .nombreComercial(dto.getNombreComercial())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .contacto(dto.getContacto())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    /**
     * Actualiza una entidad Proveedor existente con datos de un DTO
     *
     * @param proveedor Entidad a actualizar
     * @param dto DTO con los nuevos datos
     */
    public void updateEntity(Proveedor proveedor, ProveedorDTO dto) {
        if (proveedor == null || dto == null) {
            return;
        }

        proveedor.setRuc(dto.getRuc());
        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNombreComercial(dto.getNombreComercial());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setContacto(dto.getContacto());
        if (dto.getActivo() != null) {
            proveedor.setActivo(dto.getActivo());
        }
    }

    /**
     * Convierte una entidad Proveedor a ProveedorResponseDTO (versión simplificada)
     *
     * @param proveedor Entidad a convertir
     * @return DTO simplificado
     */
    public ProveedorResponseDTO toResponseDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return ProveedorResponseDTO.builder()
                .id(proveedor.getId())
                .ruc(proveedor.getRuc())
                .razonSocial(proveedor.getRazonSocial())
                .nombreComercial(proveedor.getNombreComercial())
                .activo(proveedor.getActivo())
                .build();
    }
}