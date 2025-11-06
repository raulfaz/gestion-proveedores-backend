package com.empresa.gestionproveedoresbackend.service.impl;

import com.empresa.gestionproveedoresbackend.dto.DetalleOrdenCompraDTO;
import com.empresa.gestionproveedoresbackend.dto.OrdenCompraDTO;
import com.empresa.gestionproveedoresbackend.exception.ResourceNotFoundException;
import com.empresa.gestionproveedoresbackend.model.entity.DetalleOrdenCompra;
import com.empresa.gestionproveedoresbackend.model.entity.OrdenCompra;
import com.empresa.gestionproveedoresbackend.model.entity.Producto;
import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import com.empresa.gestionproveedoresbackend.repository.OrdenCompraRepository;
import com.empresa.gestionproveedoresbackend.repository.ProductoRepository;
import com.empresa.gestionproveedoresbackend.repository.ProveedorRepository;
import com.empresa.gestionproveedoresbackend.service.OrdenCompraService;
import com.empresa.gestionproveedoresbackend.util.OrdenCompraMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Órdenes de Compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@Service
@Transactional
@Slf4j
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final OrdenCompraMapper ordenCompraMapper;

    public OrdenCompraServiceImpl(OrdenCompraRepository ordenCompraRepository,
                                  ProveedorRepository proveedorRepository,
                                  ProductoRepository productoRepository,
                                  OrdenCompraMapper ordenCompraMapper) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.ordenCompraMapper = ordenCompraMapper;
    }

    @Override
    public OrdenCompraDTO crear(OrdenCompraDTO ordenCompraDTO) {
        log.info("Creando nueva orden de compra");

        // Validar número de orden
        if (existeNumeroOrden(ordenCompraDTO.getNumeroOrden(), null)) {
            throw new IllegalArgumentException("Ya existe una orden con el número: " + ordenCompraDTO.getNumeroOrden());
        }

        // Buscar proveedor
        Proveedor proveedor = proveedorRepository.findById(ordenCompraDTO.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));

        // Crear orden
        OrdenCompra orden = ordenCompraMapper.toEntity(ordenCompraDTO);
        orden.setProveedor(proveedor);

        // Crear detalles
        List<DetalleOrdenCompra> detalles = new ArrayList<>();
        for (DetalleOrdenCompraDTO detalleDTO : ordenCompraDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));

            DetalleOrdenCompra detalle = DetalleOrdenCompra.builder()
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .producto(producto)
                    .ordenCompra(orden)
                    .build();

            detalle.calcularSubtotal();
            detalles.add(detalle);
        }

        orden.setDetalles(detalles);
        orden.calcularTotales();

        OrdenCompra ordenGuardada = ordenCompraRepository.save(orden);
        log.info("Orden de compra creada con ID: {}", ordenGuardada.getId());

        return ordenCompraMapper.toDTO(ordenGuardada);
    }

    @Override
    public OrdenCompraDTO actualizar(Long id, OrdenCompraDTO ordenCompraDTO) {
        log.info("Actualizando orden de compra ID: {}", id);

        OrdenCompra ordenExistente = ordenCompraRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));

        // Solo permitir actualizar si está PENDIENTE
        if (!"PENDIENTE".equals(ordenExistente.getEstado())) {
            throw new IllegalStateException("Solo se pueden actualizar órdenes en estado PENDIENTE");
        }

        // Validar número de orden
        if (existeNumeroOrden(ordenCompraDTO.getNumeroOrden(), id)) {
            throw new IllegalArgumentException("Ya existe otra orden con el número: " + ordenCompraDTO.getNumeroOrden());
        }

        // Actualizar proveedor si cambió
        if (!ordenExistente.getProveedor().getId().equals(ordenCompraDTO.getProveedorId())) {
            Proveedor nuevoProveedor = proveedorRepository.findById(ordenCompraDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));
            ordenExistente.setProveedor(nuevoProveedor);
        }

        // Actualizar datos básicos
        ordenCompraMapper.updateEntity(ordenExistente, ordenCompraDTO);

        // Actualizar detalles
        ordenExistente.getDetalles().clear();
        for (DetalleOrdenCompraDTO detalleDTO : ordenCompraDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            DetalleOrdenCompra detalle = DetalleOrdenCompra.builder()
                    .cantidad(detalleDTO.getCantidad())
                    .precioUnitario(detalleDTO.getPrecioUnitario())
                    .producto(producto)
                    .ordenCompra(ordenExistente)
                    .build();

            detalle.calcularSubtotal();
            ordenExistente.getDetalles().add(detalle);
        }

        ordenExistente.calcularTotales();
        OrdenCompra ordenActualizada = ordenCompraRepository.save(ordenExistente);

        log.info("Orden actualizada exitosamente");
        return ordenCompraMapper.toDTO(ordenActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraDTO buscarPorId(Long id) {
        log.info("Buscando orden por ID: {}", id);

        OrdenCompra orden = ordenCompraRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + id));

        return ordenCompraMapper.toDTO(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraDTO buscarPorNumeroOrden(String numeroOrden) {
        log.info("Buscando orden por número: {}", numeroOrden);

        OrdenCompra orden = ordenCompraRepository.findByNumeroOrden(numeroOrden)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con número: " + numeroOrden));

        return ordenCompraMapper.toDTO(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarTodas() {
        log.info("Listando todas las órdenes");

        return ordenCompraRepository.findAllWithDetallesAndProveedor().stream()
                .map(ordenCompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorEstado(String estado) {
        return ordenCompraRepository.findByEstado(estado).stream()
                .map(ordenCompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorProveedor(Long proveedorId) {
        return ordenCompraRepository.findByProveedorId(proveedorId).stream()
                .map(ordenCompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ordenCompraRepository.findByFechaOrdenBetween(fechaInicio, fechaFin).stream()
                .map(ordenCompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorProveedorYFechas(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin) {
        return ordenCompraRepository.findByProveedorIdAndFechaOrdenBetween(proveedorId, fechaInicio, fechaFin).stream()
                .map(ordenCompraMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrdenCompraDTO cambiarEstado(Long id, String estado) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        orden.setEstado(estado);
        return ordenCompraMapper.toDTO(ordenCompraRepository.save(orden));
    }

    @Override
    public OrdenCompraDTO aprobar(Long id) {
        return cambiarEstado(id, "APROBADA");
    }

    @Override
    public OrdenCompraDTO marcarRecibida(Long id) {
        return cambiarEstado(id, "RECIBIDA");
    }

    @Override
    public OrdenCompraDTO cancelar(Long id) {
        return cambiarEstado(id, "CANCELADA");
    }

    @Override
    public void eliminar(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (!"PENDIENTE".equals(orden.getEstado())) {
            throw new IllegalStateException("Solo se pueden eliminar órdenes en estado PENDIENTE");
        }

        ordenCompraRepository.delete(orden);
        log.info("Orden eliminada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroOrden(String numeroOrden, Long id) {
        if (id == null) {
            return ordenCompraRepository.existsByNumeroOrden(numeroOrden);
        } else {
            return ordenCompraRepository.existsByNumeroOrdenAndIdNot(numeroOrden, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalCompras(LocalDate fechaInicio, LocalDate fechaFin) {
        return ordenCompraRepository.calcularTotalCompras(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalComprasPorProveedor(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin) {
        return ordenCompraRepository.calcularTotalComprasPorProveedor(proveedorId, fechaInicio, fechaFin);
    }

    @Override
    public String generarNumeroOrden() {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long contador = ordenCompraRepository.count() + 1;
        return String.format("OC-%s-%04d", fecha, contador);
    }
}