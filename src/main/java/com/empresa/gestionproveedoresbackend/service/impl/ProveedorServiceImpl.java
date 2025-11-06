package com.empresa.gestionproveedoresbackend.service.impl;

import com.empresa.gestionproveedoresbackend.dto.ProveedorDTO;
import com.empresa.gestionproveedoresbackend.exception.ResourceNotFoundException;
import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import com.empresa.gestionproveedoresbackend.repository.ProveedorRepository;
import com.empresa.gestionproveedoresbackend.service.ProveedorService;
import com.empresa.gestionproveedoresbackend.util.ProveedorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Proveedores
 *
 * @author Sistema
 * @version 1.0.0
 */
@Service
@Transactional
@Slf4j
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository,
                                ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public ProveedorDTO crear(ProveedorDTO proveedorDTO) {
        log.info("Creando nuevo proveedor con RUC: {}", proveedorDTO.getRuc());

        // Validar que el RUC no exista
        if (existeRuc(proveedorDTO.getRuc(), null)) {
            throw new IllegalArgumentException("Ya existe un proveedor con el RUC: " + proveedorDTO.getRuc());
        }

        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);

        log.info("Proveedor creado exitosamente con ID: {}", proveedorGuardado.getId());
        return proveedorMapper.toDTO(proveedorGuardado);
    }

    @Override
    public ProveedorDTO actualizar(Long id, ProveedorDTO proveedorDTO) {
        log.info("Actualizando proveedor con ID: {}", id);

        Proveedor proveedorExistente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // Validar que el RUC no exista en otro proveedor
        if (existeRuc(proveedorDTO.getRuc(), id)) {
            throw new IllegalArgumentException("Ya existe otro proveedor con el RUC: " + proveedorDTO.getRuc());
        }

        proveedorMapper.updateEntity(proveedorExistente, proveedorDTO);
        Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);

        log.info("Proveedor actualizado exitosamente con ID: {}", id);
        return proveedorMapper.toDTO(proveedorActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO buscarPorId(Long id) {
        log.info("Buscando proveedor con ID: {}", id);

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        return proveedorMapper.toDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarTodos() {
        log.info("Listando todos los proveedores");

        return proveedorRepository.findAllOrderByRazonSocial().stream()
                .map(proveedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarActivos() {
        log.info("Listando proveedores activos");

        return proveedorRepository.findByActivo(true).stream()
                .map(proveedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> buscarPorRazonSocial(String razonSocial) {
        log.info("Buscando proveedores por razón social: {}", razonSocial);

        return proveedorRepository.findByRazonSocialContainingIgnoreCaseAndActivo(razonSocial, true).stream()
                .map(proveedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDTO buscarPorRuc(String ruc) {
        log.info("Buscando proveedor por RUC: {}", ruc);

        Proveedor proveedor = proveedorRepository.findByRuc(ruc)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con RUC: " + ruc));

        return proveedorMapper.toDTO(proveedor);
    }

    @Override
    public ProveedorDTO cambiarEstado(Long id, Boolean activo) {
        log.info("Cambiando estado del proveedor ID: {} a {}", id, activo);

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        proveedor.setActivo(activo);
        Proveedor proveedorActualizado = proveedorRepository.save(proveedor);

        log.info("Estado del proveedor actualizado exitosamente");
        return proveedorMapper.toDTO(proveedorActualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando proveedor con ID: {}", id);

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con ID: " + id));

        // Eliminación lógica
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);

        log.info("Proveedor eliminado (desactivado) exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRuc(String ruc, Long id) {
        if (id == null) {
            return proveedorRepository.existsByRuc(ruc);
        } else {
            return proveedorRepository.existsByRucAndIdNot(ruc, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivos() {
        return proveedorRepository.countByActivo(true);
    }
}