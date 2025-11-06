package com.empresa.gestionproveedoresbackend.service.impl;

import com.empresa.gestionproveedoresbackend.dto.ProductoDTO;
import com.empresa.gestionproveedoresbackend.exception.ResourceNotFoundException;
import com.empresa.gestionproveedoresbackend.model.entity.Producto;
import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import com.empresa.gestionproveedoresbackend.repository.ProductoRepository;
import com.empresa.gestionproveedoresbackend.repository.ProveedorRepository;
import com.empresa.gestionproveedoresbackend.service.ProductoService;
import com.empresa.gestionproveedoresbackend.util.ProductoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Productos
 *
 * @author Sistema
 * @version 1.0.0
 */
@Service
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               ProveedorRepository proveedorRepository,
                               ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDTO crear(ProductoDTO productoDTO) {
        log.info("Creando nuevo producto con código: {}", productoDTO.getCodigo());

        // Validar que el código no exista
        if (existeCodigo(productoDTO.getCodigo(), null)) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + productoDTO.getCodigo());
        }

        // Buscar proveedor
        Proveedor proveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + productoDTO.getProveedorId()));

        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setProveedor(proveedor);

        Producto productoGuardado = productoRepository.save(producto);

        log.info("Producto creado exitosamente con ID: {}", productoGuardado.getId());
        return productoMapper.toDTO(productoGuardado);
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Validar que el código no exista en otro producto
        if (existeCodigo(productoDTO.getCodigo(), id)) {
            throw new IllegalArgumentException("Ya existe otro producto con el código: " + productoDTO.getCodigo());
        }

        // Actualizar proveedor si cambió
        if (!productoExistente.getProveedor().getId().equals(productoDTO.getProveedorId())) {
            Proveedor nuevoProveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + productoDTO.getProveedorId()));
            productoExistente.setProveedor(nuevoProveedor);
        }

        productoMapper.updateEntity(productoExistente, productoDTO);
        Producto productoActualizado = productoRepository.save(productoExistente);

        log.info("Producto actualizado exitosamente con ID: {}", id);
        return productoMapper.toDTO(productoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO buscarPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        log.info("Listando todos los productos");

        return productoRepository.findAllOrderByNombre().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarActivos() {
        log.info("Listando productos activos");

        return productoRepository.findAllActivosConProveedor().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorProveedor(Long proveedorId) {
        log.info("Listando productos del proveedor ID: {}", proveedorId);

        return productoRepository.findByProveedorIdAndActivo(proveedorId, true).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);

        return productoRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, true).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO buscarPorCodigo(String codigo) {
        log.info("Buscando producto por código: {}", codigo);

        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con código: " + codigo));

        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscar(String searchTerm) {
        log.info("Buscando productos con término: {}", searchTerm);

        return productoRepository.searchByCodigoOrNombre(searchTerm).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoDTO cambiarEstado(Long id, Boolean activo) {
        log.info("Cambiando estado del producto ID: {} a {}", id, activo);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setActivo(activo);
        Producto productoActualizado = productoRepository.save(producto);

        log.info("Estado del producto actualizado exitosamente");
        return productoMapper.toDTO(productoActualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Eliminación lógica
        producto.setActivo(false);
        productoRepository.save(producto);

        log.info("Producto eliminado (desactivado) exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigo(String codigo, Long id) {
        if (id == null) {
            return productoRepository.existsByCodigo(codigo);
        } else {
            return productoRepository.existsByCodigoAndIdNot(codigo, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivos() {
        return productoRepository.countByActivo(true);
    }
}