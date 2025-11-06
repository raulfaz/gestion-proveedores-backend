package com.empresa.gestionproveedoresbackend.repository;

import com.empresa.gestionproveedoresbackend.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Producto
 * Proporciona métodos de acceso a datos para productos
 *
 * @author Sistema
 * @version 1.0.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca un producto por su código
     *
     * @param codigo Código del producto
     * @return Optional con el producto si existe
     */
    Optional<Producto> findByCodigo(String codigo);

    /**
     * Busca productos por estado (activo/inactivo)
     *
     * @param activo Estado del producto
     * @return Lista de productos
     */
    List<Producto> findByActivo(Boolean activo);

    /**
     * Busca productos cuyo nombre contenga el texto especificado
     *
     * @param nombre Texto a buscar
     * @return Lista de productos
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca productos activos cuyo nombre contenga el texto especificado
     *
     * @param nombre Texto a buscar
     * @param activo Estado del producto
     * @return Lista de productos
     */
    List<Producto> findByNombreContainingIgnoreCaseAndActivo(String nombre, Boolean activo);

    /**
     * Busca productos por proveedor
     *
     * @param proveedorId ID del proveedor
     * @return Lista de productos
     */
    List<Producto> findByProveedorId(Long proveedorId);

    /**
     * Busca productos activos por proveedor
     *
     * @param proveedorId ID del proveedor
     * @param activo Estado del producto
     * @return Lista de productos
     */
    List<Producto> findByProveedorIdAndActivo(Long proveedorId, Boolean activo);

    /**
     * Verifica si existe un producto con el código especificado
     *
     * @param codigo Código del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica si existe un producto con el código especificado excluyendo un ID
     * Útil para validaciones de actualización
     *
     * @param codigo Código del producto
     * @param id ID del producto a excluir
     * @return true si existe otro producto con ese código
     */
    boolean existsByCodigoAndIdNot(String codigo, Long id);

    /**
     * Cuenta los productos activos
     *
     * @return Número de productos activos
     */
    long countByActivo(Boolean activo);

    /**
     * Cuenta los productos de un proveedor específico
     *
     * @param proveedorId ID del proveedor
     * @return Número de productos
     */
    long countByProveedorId(Long proveedorId);

    /**
     * Busca productos ordenados por nombre
     *
     * @return Lista de productos ordenados
     */
    @Query("SELECT p FROM Producto p ORDER BY p.nombre ASC")
    List<Producto> findAllOrderByNombre();

    /**
     * Busca productos por unidad de medida
     *
     * @param unidadMedida Unidad de medida
     * @return Lista de productos
     */
    List<Producto> findByUnidadMedida(String unidadMedida);

    /**
     * Busca productos activos con información del proveedor
     *
     * @return Lista de productos con proveedor cargado
     */
    @Query("SELECT p FROM Producto p JOIN FETCH p.proveedor WHERE p.activo = true ORDER BY p.nombre")
    List<Producto> findAllActivosConProveedor();

    /**
     * Busca productos por código o nombre
     *
     * @param searchTerm Término de búsqueda
     * @return Lista de productos
     */
    @Query("SELECT p FROM Producto p WHERE LOWER(p.codigo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Producto> searchByCodigoOrNombre(@Param("searchTerm") String searchTerm);
}