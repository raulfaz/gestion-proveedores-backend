package com.empresa.gestionproveedoresbackend.util;

import com.empresa.gestionproveedoresbackend.dto.OrdenCompraDTO;
import com.empresa.gestionproveedoresbackend.model.entity.OrdenCompra;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper para convertir entre OrdenCompra y su DTO
 *
 * @author Sistema
 * @version 1.0.0
 */
@Component
public class OrdenCompraMapper {

    private final ProveedorMapper proveedorMapper;
    private final DetalleOrdenCompraMapper detalleMapper;

    public OrdenCompraMapper(ProveedorMapper proveedorMapper,
                             DetalleOrdenCompraMapper detalleMapper) {
        this.proveedorMapper = proveedorMapper;
        this.detalleMapper = detalleMapper;
    }

    /**
     * Convierte una entidad OrdenCompra a OrdenCompraDTO
     *
     * @param orden Entidad a convertir
     * @return DTO con los datos de la orden
     */
    public OrdenCompraDTO toDTO(OrdenCompra orden) {
        if (orden == null) {
            return null;
        }

        OrdenCompraDTO dto = OrdenCompraDTO.builder()
                .id(orden.getId())
                .numeroOrden(orden.getNumeroOrden())
                .fechaOrden(orden.getFechaOrden())
                .estado(orden.getEstado())
                .subtotal(orden.getSubtotal())
                .iva(orden.getIva())
                .total(orden.getTotal())
                .observaciones(orden.getObservaciones())
                .fechaRegistro(orden.getFechaRegistro())
                .fechaActualizacion(orden.getFechaActualizacion())
                .build();

        // Mapear información del proveedor
        if (orden.getProveedor() != null) {
            dto.setProveedorId(orden.getProveedor().getId());
            dto.setProveedor(proveedorMapper.toResponseDTO(orden.getProveedor()));
        }

        // Mapear detalles
        if (orden.getDetalles() != null && !orden.getDetalles().isEmpty()) {
            dto.setDetalles(orden.getDetalles().stream()
                    .map(detalleMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Convierte un OrdenCompraDTO a entidad OrdenCompra
     * NO establece las relaciones (deben hacerse en el servicio)
     *
     * @param dto DTO con los datos
     * @return Entidad OrdenCompra
     */
    public OrdenCompra toEntity(OrdenCompraDTO dto) {
        if (dto == null) {
            return null;
        }

        return OrdenCompra.builder()
                .id(dto.getId())
                .numeroOrden(dto.getNumeroOrden())
                .fechaOrden(dto.getFechaOrden())
                .estado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE")
                .subtotal(dto.getSubtotal())
                .iva(dto.getIva())
                .total(dto.getTotal())
                .observaciones(dto.getObservaciones())
                .build();
    }

    /**
     * Actualiza una entidad OrdenCompra existente con datos de un DTO
     *
     * @param orden Entidad a actualizar
     * @param dto DTO con los nuevos datos
     */
    public void updateEntity(OrdenCompra orden, OrdenCompraDTO dto) {
        if (orden == null || dto == null) {
            return;
        }

        orden.setNumeroOrden(dto.getNumeroOrden());
        orden.setFechaOrden(dto.getFechaOrden());
        orden.setEstado(dto.getEstado());
        orden.setObservaciones(dto.getObservaciones());
        // Los totales se calculan automáticamente con el método calcularTotales()
    }
}