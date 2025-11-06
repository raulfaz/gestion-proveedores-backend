package com.empresa.gestionproveedoresbackend.controller;

import com.empresa.gestionproveedoresbackend.dto.ApiResponseDTO;
import com.empresa.gestionproveedoresbackend.dto.OrdenCompraDTO;
import com.empresa.gestionproveedoresbackend.service.OrdenCompraService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de Órdenes de Compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@RestController
@RequestMapping("/ordenes-compra")
@CrossOrigin(origins = "*")
@Slf4j
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    /**
     * Crea una nueva orden de compra
     * POST /api/ordenes-compra
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> crear(
            @Valid @RequestBody OrdenCompraDTO ordenCompraDTO) {

        log.info("Petición para crear orden de compra");

        OrdenCompraDTO ordenCreada = ordenCompraService.crear(ordenCompraDTO);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                ordenCreada,
                "Orden de compra creada exitosamente"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Actualiza una orden de compra existente
     * PUT /api/ordenes-compra/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdenCompraDTO ordenCompraDTO) {

        log.info("Petición para actualizar orden de compra ID: {}", id);

        OrdenCompraDTO ordenActualizada = ordenCompraService.actualizar(id, ordenCompraDTO);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                ordenActualizada,
                "Orden de compra actualizada exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca una orden por ID
     * GET /api/ordenes-compra/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> buscarPorId(@PathVariable Long id) {
        log.info("Petición para buscar orden ID: {}", id);

        OrdenCompraDTO orden = ordenCompraService.buscarPorId(id);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Orden de compra encontrada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca una orden por número
     * GET /api/ordenes-compra/numero/{numeroOrden}
     */
    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> buscarPorNumeroOrden(
            @PathVariable String numeroOrden) {

        log.info("Petición para buscar orden por número: {}", numeroOrden);

        OrdenCompraDTO orden = ordenCompraService.buscarPorNumeroOrden(numeroOrden);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Orden de compra encontrada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas las órdenes
     * GET /api/ordenes-compra
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarTodas() {
        log.info("Petición para listar todas las órdenes");

        List<OrdenCompraDTO> ordenes = ordenCompraService.listarTodas();
        ApiResponseDTO<List<OrdenCompraDTO>> response = ApiResponseDTO.success(
                ordenes,
                "Lista de órdenes obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista órdenes por estado
     * GET /api/ordenes-compra/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorEstado(
            @PathVariable String estado) {

        log.info("Petición para listar órdenes por estado: {}", estado);

        List<OrdenCompraDTO> ordenes = ordenCompraService.listarPorEstado(estado);
        ApiResponseDTO<List<OrdenCompraDTO>> response = ApiResponseDTO.success(
                ordenes,
                "Lista de órdenes obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista órdenes por proveedor
     * GET /api/ordenes-compra/proveedor/{proveedorId}
     */
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorProveedor(
            @PathVariable Long proveedorId) {

        log.info("Petición para listar órdenes del proveedor ID: {}", proveedorId);

        List<OrdenCompraDTO> ordenes = ordenCompraService.listarPorProveedor(proveedorId);
        ApiResponseDTO<List<OrdenCompraDTO>> response = ApiResponseDTO.success(
                ordenes,
                "Lista de órdenes del proveedor obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista órdenes por rango de fechas
     * GET /api/ordenes-compra/fechas?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/fechas")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Petición para listar órdenes entre {} y {}", inicio, fin);

        List<OrdenCompraDTO> ordenes = ordenCompraService.listarPorRangoFechas(inicio, fin);
        ApiResponseDTO<List<OrdenCompraDTO>> response = ApiResponseDTO.success(
                ordenes,
                "Lista de órdenes obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lista órdenes por proveedor y fechas
     * GET /api/ordenes-compra/proveedor/{proveedorId}/fechas?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/proveedor/{proveedorId}/fechas")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorProveedorYFechas(
            @PathVariable Long proveedorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Petición para listar órdenes del proveedor {} entre {} y {}", proveedorId, inicio, fin);

        List<OrdenCompraDTO> ordenes = ordenCompraService.listarPorProveedorYFechas(proveedorId, inicio, fin);
        ApiResponseDTO<List<OrdenCompraDTO>> response = ApiResponseDTO.success(
                ordenes,
                "Lista de órdenes obtenida exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cambia el estado de una orden
     * PATCH /api/ordenes-compra/{id}/estado?estado=APROBADA
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        log.info("Petición para cambiar estado de orden ID: {} a {}", id, estado);

        OrdenCompraDTO orden = ordenCompraService.cambiarEstado(id, estado);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Estado de la orden actualizado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Aprueba una orden
     * PATCH /api/ordenes-compra/{id}/aprobar
     */
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> aprobar(@PathVariable Long id) {
        log.info("Petición para aprobar orden ID: {}", id);

        OrdenCompraDTO orden = ordenCompraService.aprobar(id);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Orden aprobada exitosamente"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Marca orden como recibida
     * PATCH /api/ordenes-compra/{id}/recibir
     */
    @PatchMapping("/{id}/recibir")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> marcarRecibida(@PathVariable Long id) {
        log.info("Petición para marcar orden ID: {} como recibida", id);

        OrdenCompraDTO orden = ordenCompraService.marcarRecibida(id);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Orden marcada como recibida"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Cancela una orden
     * PATCH /api/ordenes-compra/{id}/cancelar
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> cancelar(@PathVariable Long id) {
        log.info("Petición para cancelar orden ID: {}", id);

        OrdenCompraDTO orden = ordenCompraService.cancelar(id);
        ApiResponseDTO<OrdenCompraDTO> response = ApiResponseDTO.success(
                orden,
                "Orden cancelada"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una orden (solo si está pendiente)
     * DELETE /api/ordenes-compra/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
        log.info("Petición para eliminar orden ID: {}", id);

        ordenCompraService.eliminar(id);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Orden eliminada exitosamente");

        return ResponseEntity.ok(response);
    }

    /**
     * Verifica si un número de orden existe
     * GET /api/ordenes-compra/existe-numero?numero=OC-001&id=1
     */
    @GetMapping("/existe-numero")
    public ResponseEntity<ApiResponseDTO<Boolean>> existeNumeroOrden(
            @RequestParam String numero,
            @RequestParam(required = false) Long id) {

        log.info("Verificando existencia de número de orden: {}", numero);

        boolean existe = ordenCompraService.existeNumeroOrden(numero, id);
        ApiResponseDTO<Boolean> response = ApiResponseDTO.success(
                existe,
                existe ? "El número de orden ya existe" : "El número está disponible"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Calcula total de compras en un rango de fechas
     * GET /api/ordenes-compra/total?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/total")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalCompras(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Calculando total de compras entre {} y {}", inicio, fin);

        BigDecimal total = ordenCompraService.calcularTotalCompras(inicio, fin);
        ApiResponseDTO<BigDecimal> response = ApiResponseDTO.success(
                total,
                "Total de compras calculado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Calcula total de compras de un proveedor
     * GET /api/ordenes-compra/proveedor/{proveedorId}/total?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/proveedor/{proveedorId}/total")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalPorProveedor(
            @PathVariable Long proveedorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        log.info("Calculando total de compras del proveedor {} entre {} y {}", proveedorId, inicio, fin);

        BigDecimal total = ordenCompraService.calcularTotalComprasPorProveedor(proveedorId, inicio, fin);
        ApiResponseDTO<BigDecimal> response = ApiResponseDTO.success(
                total,
                "Total de compras del proveedor calculado"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Genera un nuevo número de orden
     * GET /api/ordenes-compra/generar-numero
     */
    @GetMapping("/generar-numero")
    public ResponseEntity<ApiResponseDTO<String>> generarNumeroOrden() {
        log.info("Generando nuevo número de orden");

        String numeroOrden = ordenCompraService.generarNumeroOrden();
        ApiResponseDTO<String> response = ApiResponseDTO.success(
                numeroOrden,
                "Número de orden generado"
        );

        return ResponseEntity.ok(response);
    }
}