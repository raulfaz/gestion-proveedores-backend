package com.empresa.gestionproveedoresbackend.service;

import com.empresa.gestionproveedoresbackend.dto.OrdenCompraDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio para operaciones de negocio relacionadas con Órdenes de Compra
 *
 * @author Sistema
 * @version 1.0.0
 */
public interface OrdenCompraService {

    /**
     * Crea una nueva orden de compra
     *
     * @param ordenCompraDTO Datos de la orden
     * @return Orden creada
     */
    OrdenCompraDTO crear(OrdenCompraDTO ordenCompraDTO);

    /**
     * Actualiza una orden de compra existente
     *
     * @param id ID de la orden
     * @param ordenCompraDTO Datos actualizados
     * @return Orden actualizada
     */
    OrdenCompraDTO actualizar(Long id, OrdenCompraDTO ordenCompraDTO);

    /**
     * Busca una orden por ID
     *
     * @param id ID de la orden
     * @return Orden encontrada
     */
    OrdenCompraDTO buscarPorId(Long id);

    /**
     * Busca una orden por número de orden
     *
     * @param numeroOrden Número de orden
     * @return Orden encontrada
     */
    OrdenCompraDTO buscarPorNumeroOrden(String numeroOrden);

    /**
     * Lista todas las órdenes
     *
     * @return Lista de órdenes
     */
    List<OrdenCompraDTO> listarTodas();

    /**
     * Lista órdenes por estado
     *
     * @param estado Estado de la orden
     * @return Lista de órdenes
     */
    List<OrdenCompraDTO> listarPorEstado(String estado);

    /**
     * Lista órdenes por proveedor
     *
     * @param proveedorId ID del proveedor
     * @return Lista de órdenes
     */
    List<OrdenCompraDTO> listarPorProveedor(Long proveedorId);

    /**
     * Lista órdenes por rango de fechas
     *
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Lista de órdenes
     */
    List<OrdenCompraDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Lista órdenes por proveedor y rango de fechas
     *
     * @param proveedorId ID del proveedor
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Lista de órdenes
     */
    List<OrdenCompraDTO> listarPorProveedorYFechas(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Cambia el estado de una orden
     *
     * @param id ID de la orden
     * @param estado Nuevo estado
     * @return Orden actualizada
     */
    OrdenCompraDTO cambiarEstado(Long id, String estado);

    /**
     * Aprueba una orden de compra
     *
     * @param id ID de la orden
     * @return Orden aprobada
     */
    OrdenCompraDTO aprobar(Long id);

    /**
     * Marca una orden como recibida
     *
     * @param id ID de la orden
     * @return Orden actualizada
     */
    OrdenCompraDTO marcarRecibida(Long id);

    /**
     * Cancela una orden de compra
     *
     * @param id ID de la orden
     * @return Orden cancelada
     */
    OrdenCompraDTO cancelar(Long id);

    /**
     * Elimina una orden (solo si está pendiente)
     *
     * @param id ID de la orden
     */
    void eliminar(Long id);

    /**
     * Verifica si un número de orden ya existe
     *
     * @param numeroOrden Número a verificar
     * @param id ID a excluir (null para creación)
     * @return true si existe
     */
    boolean existeNumeroOrden(String numeroOrden, Long id);

    /**
     * Calcula el total de compras en un rango de fechas
     *
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Total de compras
     */
    BigDecimal calcularTotalCompras(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Calcula el total de compras de un proveedor
     *
     * @param proveedorId ID del proveedor
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Total de compras
     */
    BigDecimal calcularTotalComprasPorProveedor(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera el siguiente número de orden
     *
     * @return Número de orden generado
     */
    String generarNumeroOrden();
}