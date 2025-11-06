package com.empresa.gestionproveedoresbackend.util;

import com.empresa.gestionproveedoresbackend.dto.ProductoDTO;
import com.empresa.gestionproveedoresbackend.dto.ProductoResponseDTO;
import com.empresa.gestionproveedoresbackend.model.entity.Producto;
import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Producto y sus DTOs
 *
 * @author Sistema
 * @version 1.0.0
 */
@Component
public class ProductoMapper {

    private final ProveedorMapper proveedorMapper;

    public ProductoMapper(ProveedorMapper proveedorMapper) {
        this.proveedorMapper = proveedorMapper;
    }

    /**
     * Convierte una entidad Producto a ProductoDTO
     *
     * @param producto Entidad a convertir
     * @return DTO con los datos del producto
     */
    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dto = ProductoDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .unidadMedida(producto.getUnidadMedida())
                .precio(producto.getPrecio())
                .activo(producto.getActivo())
                .fechaRegistro(producto.getFechaRegistro())
                .build();

        // Mapear información del proveedor
        if (producto.getProveedor() != null) {
            dto.setProveedorId(producto.getProveedor().getId());
            dto.setProveedor(proveedorMapper.toResponseDTO(producto.getProveedor()));
        }

        return dto;
    }

    /**
     * Convierte un ProductoDTO a entidad Producto
     * NO establece la relación con Proveedor (debe hacerse en el servicio)
     *
     * @param dto DTO con los datos
     * @return Entidad Producto
     */
    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        return Producto.builder()
                .id(dto.getId())
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .unidadMedida(dto.getUnidadMedida())
                .precio(dto.getPrecio())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    /**
     * Actualiza una entidad Producto existente con datos de un DTO
     *
     * @param producto Entidad a actualizar
     * @param dto DTO con los nuevos datos
     */
    public void updateEntity(Producto producto, ProductoDTO dto) {
        if (producto == null || dto == null) {
            return;
        }

        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setPrecio(dto.getPrecio());
        if (dto.getActivo() != null) {
            producto.setActivo(dto.getActivo());
        }
        // La actualización del proveedor se hace en el servicio
    }

    /**
     * Convierte una entidad Producto a ProductoResponseDTO (versión simplificada)
     *
     * @param producto Entidad a convertir
     * @return DTO simplificado
     */
    public ProductoResponseDTO toResponseDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .unidadMedida(producto.getUnidadMedida())
                .precio(producto.getPrecio())
                .activo(producto.getActivo())
                .build();
    }
}