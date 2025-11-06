package com.empresa.gestionproveedoresbackend.util;

import com.empresa.gestionproveedoresbackend.dto.DetalleOrdenCompraDTO;
import com.empresa.gestionproveedoresbackend.model.entity.DetalleOrdenCompra;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DetalleOrdenCompra y su DTO
 *
 * @author Sistema
 * @version 1.0.0
 */
@Component
public class DetalleOrdenCompraMapper {

    private final ProductoMapper productoMapper;

    public DetalleOrdenCompraMapper(ProductoMapper productoMapper) {
        this.productoMapper = productoMapper;
    }

    /**
     * Convierte una entidad DetalleOrdenCompra a DetalleOrdenCompraDTO
     *
     * @param detalle Entidad a convertir
     * @return DTO con los datos del detalle
     */
    public DetalleOrdenCompraDTO toDTO(DetalleOrdenCompra detalle) {
        if (detalle == null) {
            return null;
        }

        DetalleOrdenCompraDTO dto = DetalleOrdenCompraDTO.builder()
                .id(detalle.getId())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubtotal())
                .build();

        // Mapear información del producto
        if (detalle.getProducto() != null) {
            dto.setProductoId(detalle.getProducto().getId());
            dto.setProducto(productoMapper.toResponseDTO(detalle.getProducto()));
        }

        // Mapear ID de la orden
        if (detalle.getOrdenCompra() != null) {
            dto.setOrdenCompraId(detalle.getOrdenCompra().getId());
        }

        return dto;
    }

    /**
     * Convierte un DetalleOrdenCompraDTO a entidad DetalleOrdenCompra
     * NO establece las relaciones (deben hacerse en el servicio)
     *
     * @param dto DTO con los datos
     * @return Entidad DetalleOrdenCompra
     */
    public DetalleOrdenCompra toEntity(DetalleOrdenCompraDTO dto) {
        if (dto == null) {
            return null;
        }

        return DetalleOrdenCompra.builder()
                .id(dto.getId())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .subtotal(dto.getSubtotal())
                .build();
    }

    /**
     * Actualiza una entidad DetalleOrdenCompra existente con datos de un DTO
     *
     * @param detalle Entidad a actualizar
     * @param dto DTO con los nuevos datos
     */
    public void updateEntity(DetalleOrdenCompra detalle, DetalleOrdenCompraDTO dto) {
        if (detalle == null || dto == null) {
            return;
        }

        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        // El subtotal se calcula automáticamente con @PrePersist/@PreUpdate
    }
}