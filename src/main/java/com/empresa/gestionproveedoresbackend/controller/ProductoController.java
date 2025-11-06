package com.empresa.gestionproveedoresbackend.controller;

import com.empresa.gestionproveedoresbackend.dto.ApiResponseDTO;
import com.empresa.gestionproveedoresbackend.dto.ProductoDTO;
import com.empresa.gestionproveedoresbackend.service.ProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Productos
 *
 * @author Sistema
 * @version 1.0.0
 */
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Crea un nuevo producto
     * POST /api/productos
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> crear(@Valid @RequestBody ProductoDTO productoDTO) {
        log.info("Petición para crear producto: {}", productoDTO.getNombre());

        ProductoDTO productoCreado = productoService.crear(productoDTO);
        ApiResponseDTO<ProductoDTO> response = ApiResponseDTO.success(
                productoCreado,
                "Producto creado exitosamente"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza un producto existente
     * PUT /api/productos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {

        log.info("Petición para actualizar producto ID: {}", id);

        ProductoDTO productoActualizado = productoService.actualizar(id, productoDTO);
        ApiResponseDTO<ProductoDTO> response = ApiResponseDTO.success(
                productoActualizado,
                "Producto actualizado exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un producto por ID
     * GET /api/productos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> buscarPorId(@PathVariable Long id) {
        log.info("Petición para buscar producto ID: {}", id);

        ProductoDTO producto = productoService.buscarPorId(id);
        ApiResponseDTO<ProductoDTO> response = ApiResponseDTO.success(
                producto,
                "Producto encontrado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los productos
     * GET /api/productos
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarTodos() {
        log.info("Petición para listar todos los productos");

        List<ProductoDTO> productos = productoService.listarTodos();
        ApiResponseDTO<List<ProductoDTO>> response = ApiResponseDTO.success(
                productos,
                "Lista de productos obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista productos activos
     * GET /api/productos/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarActivos() {
        log.info("Petición para listar productos activos");

        List<ProductoDTO> productos = productoService.listarActivos();
        ApiResponseDTO<List<ProductoDTO>> response = ApiResponseDTO.success(
                productos,
                "Lista de productos activos obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista productos por proveedor
     * GET /api/productos/proveedor/{proveedorId}
     */
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarPorProveedor(
            @PathVariable Long proveedorId) {

        log.info("Petición para listar productos del proveedor ID: {}", proveedorId);

        List<ProductoDTO> productos = productoService.listarPorProveedor(proveedorId);
        ApiResponseDTO<List<ProductoDTO>> response = ApiResponseDTO.success(
                productos,
                "Lista de productos del proveedor obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca productos por nombre
     * GET /api/productos/buscar-nombre?nombre=texto
     */
    @GetMapping("/buscar-nombre")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> buscarPorNombre(
            @RequestParam String nombre) {

        log.info("Petición para buscar productos por nombre: {}", nombre);

        List<ProductoDTO> productos = productoService.buscarPorNombre(nombre);
        ApiResponseDTO<List<ProductoDTO>> response = ApiResponseDTO.success(
                productos,
                "Búsqueda completada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca un producto por código
     * GET /api/productos/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> buscarPorCodigo(@PathVariable String codigo) {
        log.info("Petición para buscar producto por código: {}", codigo);

        ProductoDTO producto = productoService.buscarPorCodigo(codigo);
        ApiResponseDTO<ProductoDTO> response = ApiResponseDTO.success(
                producto,
                "Producto encontrado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca productos por código o nombre
     * GET /api/productos/buscar?term=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> buscar(
            @RequestParam String term) {

        log.info("Petición para buscar productos con término: {}", term);

        List<ProductoDTO> productos = productoService.buscar(term);
        ApiResponseDTO<List<ProductoDTO>> response = ApiResponseDTO.success(
                productos,
                "Búsqueda completada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cambia el estado de un producto (activar/desactivar)
     * PATCH /api/productos/{id}/estado?activo=true
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo) {

        log.info("Petición para cambiar estado del producto ID: {} a {}", id, activo);

        ProductoDTO producto = productoService.cambiarEstado(id, activo);
        ApiResponseDTO<ProductoDTO> response = ApiResponseDTO.success(
                producto,
                "Estado del producto actualizado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un producto (eliminación lógica)
     * DELETE /api/productos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
        log.info("Petición para eliminar producto ID: {}", id);

        productoService.eliminar(id);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Producto eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    /**
     * Verifica si un código ya existe
     * GET /api/productos/existe-codigo?codigo=ABC123&id=1
     */
    @GetMapping("/existe-codigo")
    public ResponseEntity<ApiResponseDTO<Boolean>> existeCodigo(
            @RequestParam String codigo,
            @RequestParam(required = false) Long id) {

        log.info("Verificando existencia de código: {}", codigo);

        boolean existe = productoService.existeCodigo(codigo, id);
        ApiResponseDTO<Boolean> response = ApiResponseDTO.success(
                existe,
                existe ? "El código ya está registrado" : "El código está disponible"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cuenta productos activos
     * GET /api/productos/contar/activos
     */
    @GetMapping("/contar/activos")
    public ResponseEntity<ApiResponseDTO<Long>> contarActivos() {
        log.info("Petición para contar productos activos");

        long cantidad = productoService.contarActivos();
        ApiResponseDTO<Long> response = ApiResponseDTO.success(
                cantidad,
                "Total de productos activos"
        );

        return ResponseEntity.ok(response);
    }
}