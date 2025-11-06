package com.empresa.gestionproveedoresbackend.controller;

import com.empresa.gestionproveedoresbackend.dto.ApiResponseDTO;
import com.empresa.gestionproveedoresbackend.dto.ProveedorDTO;
import com.empresa.gestionproveedoresbackend.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Proveedores
 *
 * @author Sistema
 * @version 1.0.0
 */
@RestController
@RequestMapping("/proveedores")
@CrossOrigin(origins = "*")
@Slf4j
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    /**
     * Crea un nuevo proveedor
     * POST /api/proveedores
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProveedorDTO>> crear(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        log.info("Petición para crear proveedor: {}", proveedorDTO.getRazonSocial());

        ProveedorDTO proveedorCreado = proveedorService.crear(proveedorDTO);
        ApiResponseDTO<ProveedorDTO> response = ApiResponseDTO.success(
                proveedorCreado,
                "Proveedor creado exitosamente"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza un proveedor existente
     * PUT /api/proveedores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProveedorDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO proveedorDTO) {

        log.info("Petición para actualizar proveedor ID: {}", id);

        ProveedorDTO proveedorActualizado = proveedorService.actualizar(id, proveedorDTO);
        ApiResponseDTO<ProveedorDTO> response = ApiResponseDTO.success(
                proveedorActualizado,
                "Proveedor actualizado exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un proveedor por ID
     * GET /api/proveedores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProveedorDTO>> buscarPorId(@PathVariable Long id) {
        log.info("Petición para buscar proveedor ID: {}", id);

        ProveedorDTO proveedor = proveedorService.buscarPorId(id);
        ApiResponseDTO<ProveedorDTO> response = ApiResponseDTO.success(
                proveedor,
                "Proveedor encontrado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los proveedores
     * GET /api/proveedores
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProveedorDTO>>> listarTodos() {
        log.info("Petición para listar todos los proveedores");

        List<ProveedorDTO> proveedores = proveedorService.listarTodos();
        ApiResponseDTO<List<ProveedorDTO>> response = ApiResponseDTO.success(
                proveedores,
                "Lista de proveedores obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista proveedores activos
     * GET /api/proveedores/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponseDTO<List<ProveedorDTO>>> listarActivos() {
        log.info("Petición para listar proveedores activos");

        List<ProveedorDTO> proveedores = proveedorService.listarActivos();
        ApiResponseDTO<List<ProveedorDTO>> response = ApiResponseDTO.success(
                proveedores,
                "Lista de proveedores activos obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca proveedores por razón social
     * GET /api/proveedores/buscar?razonSocial=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ProveedorDTO>>> buscarPorRazonSocial(
            @RequestParam String razonSocial) {

        log.info("Petición para buscar proveedores por razón social: {}", razonSocial);

        List<ProveedorDTO> proveedores = proveedorService.buscarPorRazonSocial(razonSocial);
        ApiResponseDTO<List<ProveedorDTO>> response = ApiResponseDTO.success(
                proveedores,
                "Búsqueda completada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un proveedor por RUC
     * GET /api/proveedores/ruc/{ruc}
     */
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<ApiResponseDTO<ProveedorDTO>> buscarPorRuc(@PathVariable String ruc) {
        log.info("Petición para buscar proveedor por RUC: {}", ruc);

        ProveedorDTO proveedor = proveedorService.buscarPorRuc(ruc);
        ApiResponseDTO<ProveedorDTO> response = ApiResponseDTO.success(
                proveedor,
                "Proveedor encontrado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cambia el estado de un proveedor (activar/desactivar)
     * PATCH /api/proveedores/{id}/estado?activo=true
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<ProveedorDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo) {

        log.info("Petición para cambiar estado del proveedor ID: {} a {}", id, activo);

        ProveedorDTO proveedor = proveedorService.cambiarEstado(id, activo);
        ApiResponseDTO<ProveedorDTO> response = ApiResponseDTO.success(
                proveedor,
                "Estado del proveedor actualizado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un proveedor (eliminación lógica)
     * DELETE /api/proveedores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
        log.info("Petición para eliminar proveedor ID: {}", id);

        proveedorService.eliminar(id);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Proveedor eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    /**
     * Verifica si un RUC ya existe
     * GET /api/proveedores/existe-ruc?ruc=1234567890&id=1
     */
    @GetMapping("/existe-ruc")
    public ResponseEntity<ApiResponseDTO<Boolean>> existeRuc(
            @RequestParam String ruc,
            @RequestParam(required = false) Long id) {

        log.info("Verificando existencia de RUC: {}", ruc);

        boolean existe = proveedorService.existeRuc(ruc, id);
        ApiResponseDTO<Boolean> response = ApiResponseDTO.success(
                existe,
                existe ? "El RUC ya está registrado" : "El RUC está disponible"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cuenta proveedores activos
     * GET /api/proveedores/contar/activos
     */
    @GetMapping("/contar/activos")
    public ResponseEntity<ApiResponseDTO<Long>> contarActivos() {
        log.info("Petición para contar proveedores activos");

        long cantidad = proveedorService.contarActivos();
        ApiResponseDTO<Long> response = ApiResponseDTO.success(
                cantidad,
                "Total de proveedores activos"
        );

        return ResponseEntity.ok(response);
    }
}