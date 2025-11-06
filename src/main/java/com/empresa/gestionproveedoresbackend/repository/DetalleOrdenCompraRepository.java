package com.empresa.gestionproveedoresbackend.repository;

import com.empresa.gestionproveedoresbackend.model.entity.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad DetalleOrdenCompra
 * Proporciona métodos de acceso a datos para detalles de órdenes de compra
 *
 * @author Sistema
 * @version 1.0.0
 */
@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Long> {

    /**
     * Busca detalles por orden de compra
     *
     * @param ordenCompraId ID de la orden de compra
     * @return Lista de detalles
     */
    List<DetalleOrdenCompra> findByOrdenCompraId(Long ordenCompraId);

    /**
     * Busca detalles por producto
     *
     * @param productoId ID del producto
     * @return Lista de detalles
     */
    List<DetalleOrdenCompra> findByProductoId(Long productoId);

    /**
     * Busca detalles por orden de compra con producto cargado
     *
     * @param ordenCompraId ID de la orden de compra
     * @return Lista de detalles con producto
     */
    @Query("SELECT d FROM DetalleOrdenCompra d " +
            "LEFT JOIN FETCH d.producto " +
            "WHERE d.ordenCompra.id = :ordenCompraId")
    List<DetalleOrdenCompra> findByOrdenCompraIdWithProducto(@Param("ordenCompraId") Long ordenCompraId);

    /**
     * Cuenta los detalles de una orden de compra
     *
     * @param ordenCompraId ID de la orden de compra
     * @return Número de detalles
     */
    long countByOrdenCompraId(Long ordenCompraId);

    /**
     * Elimina todos los detalles de una orden de compra
     *
     * @param ordenCompraId ID de la orden de compra
     */
    void deleteByOrdenCompraId(Long ordenCompraId);

    /**
     * Busca productos más comprados (análisis de ventas)
     *
     * @param limit Número máximo de resultados
     * @return Lista de arrays con [productoId, totalCantidad]
     */
    @Query("SELECT d.producto.id, SUM(d.cantidad) as totalCantidad " +
            "FROM DetalleOrdenCompra d " +
            "GROUP BY d.producto.id " +
            "ORDER BY totalCantidad DESC")
    List<Object[]> findProductosMasComprados(@Param("limit") int limit);

    /**
     * Calcula la cantidad total comprada de un producto
     *
     * @param productoId ID del producto
     * @return Cantidad total
     */
    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM DetalleOrdenCompra d " +
            "WHERE d.producto.id = :productoId")
    Integer calcularCantidadTotalComprada(@Param("productoId") Long productoId);

    /**
     * Busca detalles con información completa (orden, producto, proveedor)
     *
     * @return Lista de detalles completos
     */
    @Query("SELECT d FROM DetalleOrdenCompra d " +
            "LEFT JOIN FETCH d.ordenCompra o " +
            "LEFT JOIN FETCH o.proveedor " +
            "LEFT JOIN FETCH d.producto")
    List<DetalleOrdenCompra> findAllWithCompleteInfo();
}