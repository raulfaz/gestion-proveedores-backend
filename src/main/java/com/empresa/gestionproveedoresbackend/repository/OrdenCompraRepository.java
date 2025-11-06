package com.empresa.gestionproveedoresbackend.repository;

import com.empresa.gestionproveedoresbackend.model.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad OrdenCompra
 * Proporciona métodos de acceso a datos para órdenes de compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    /**
     * Busca una orden de compra por su número de orden
     *
     * @param numeroOrden Número de la orden
     * @return Optional con la orden si existe
     */
    Optional<OrdenCompra> findByNumeroOrden(String numeroOrden);

    /**
     * Busca órdenes de compra por estado
     *
     * @param estado Estado de la orden
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByEstado(String estado);

    /**
     * Busca órdenes de compra por proveedor
     *
     * @param proveedorId ID del proveedor
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByProveedorId(Long proveedorId);

    /**
     * Busca órdenes de compra por proveedor y estado
     *
     * @param proveedorId ID del proveedor
     * @param estado Estado de la orden
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByProveedorIdAndEstado(Long proveedorId, String estado);

    /**
     * Busca órdenes de compra por rango de fechas
     *
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByFechaOrdenBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca órdenes de compra por rango de fechas y estado
     *
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @param estado Estado de la orden
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByFechaOrdenBetweenAndEstado(LocalDate fechaInicio, LocalDate fechaFin, String estado);

    /**
     * Busca órdenes de compra por proveedor y rango de fechas
     *
     * @param proveedorId ID del proveedor
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de órdenes
     */
    List<OrdenCompra> findByProveedorIdAndFechaOrdenBetween(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Verifica si existe una orden con el número especificado
     *
     * @param numeroOrden Número de la orden
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumeroOrden(String numeroOrden);

    /**
     * Verifica si existe una orden con el número especificado excluyendo un ID
     *
     * @param numeroOrden Número de la orden
     * @param id ID de la orden a excluir
     * @return true si existe otra orden con ese número
     */
    boolean existsByNumeroOrdenAndIdNot(String numeroOrden, Long id);

    /**
     * Cuenta las órdenes por estado
     *
     * @param estado Estado de la orden
     * @return Número de órdenes
     */
    long countByEstado(String estado);

    /**
     * Busca órdenes ordenadas por fecha descendente
     *
     * @return Lista de órdenes ordenadas
     */
    @Query("SELECT o FROM OrdenCompra o ORDER BY o.fechaOrden DESC")
    List<OrdenCompra> findAllOrderByFechaOrdenDesc();

    /**
     * Busca órdenes con sus detalles y proveedor cargados
     *
     * @return Lista de órdenes completas
     */
    @Query("SELECT DISTINCT o FROM OrdenCompra o " +
            "LEFT JOIN FETCH o.detalles d " +
            "LEFT JOIN FETCH d.producto " +
            "LEFT JOIN FETCH o.proveedor " +
            "ORDER BY o.fechaOrden DESC")
    List<OrdenCompra> findAllWithDetallesAndProveedor();

    /**
     * Busca una orden por ID con sus detalles cargados
     *
     * @param id ID de la orden
     * @return Optional con la orden completa
     */
    @Query("SELECT o FROM OrdenCompra o " +
            "LEFT JOIN FETCH o.detalles d " +
            "LEFT JOIN FETCH d.producto " +
            "LEFT JOIN FETCH o.proveedor " +
            "WHERE o.id = :id")
    Optional<OrdenCompra> findByIdWithDetalles(@Param("id") Long id);

    /**
     * Calcula el total de compras por proveedor en un rango de fechas
     *
     * @param proveedorId ID del proveedor
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Total de compras
     */
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM OrdenCompra o " +
            "WHERE o.proveedor.id = :proveedorId " +
            "AND o.fechaOrden BETWEEN :fechaInicio AND :fechaFin " +
            "AND o.estado != 'CANCELADA'")
    BigDecimal calcularTotalComprasPorProveedor(
            @Param("proveedorId") Long proveedorId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Calcula el total de compras en un rango de fechas
     *
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Total de compras
     */
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM OrdenCompra o " +
            "WHERE o.fechaOrden BETWEEN :fechaInicio AND :fechaFin " +
            "AND o.estado != 'CANCELADA'")
    BigDecimal calcularTotalCompras(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Busca las últimas N órdenes de compra
     *
     * @param limit Número máximo de órdenes
     * @return Lista de órdenes recientes
     */
    @Query("SELECT o FROM OrdenCompra o ORDER BY o.fechaOrden DESC, o.id DESC")
    List<OrdenCompra> findTopNOrderByFechaOrdenDesc(@Param("limit") int limit);
}