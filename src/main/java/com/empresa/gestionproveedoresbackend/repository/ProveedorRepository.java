package com.empresa.gestionproveedoresbackend.repository;

import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Proveedor
 * Proporciona métodos de acceso a datos para proveedores
 *
 * @author Sistema
 * @version 1.0.0
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Busca un proveedor por su RUC
     *
     * @param ruc RUC del proveedor
     * @return Optional con el proveedor si existe
     */
    Optional<Proveedor> findByRuc(String ruc);

    /**
     * Busca un proveedor por su email
     *
     * @param email Email del proveedor
     * @return Optional con el proveedor si existe
     */
    Optional<Proveedor> findByEmail(String email);

    /**
     * Busca proveedores por estado (activo/inactivo)
     *
     * @param activo Estado del proveedor
     * @return Lista de proveedores
     */
    List<Proveedor> findByActivo(Boolean activo);

    /**
     * Busca proveedores cuya razón social contenga el texto especificado
     *
     * @param razonSocial Texto a buscar
     * @return Lista de proveedores
     */
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);

    /**
     * Busca proveedores activos cuya razón social contenga el texto especificado
     *
     * @param razonSocial Texto a buscar
     * @param activo Estado del proveedor
     * @return Lista de proveedores
     */
    List<Proveedor> findByRazonSocialContainingIgnoreCaseAndActivo(String razonSocial, Boolean activo);

    /**
     * Verifica si existe un proveedor con el RUC especificado
     *
     * @param ruc RUC del proveedor
     * @return true si existe, false en caso contrario
     */
    boolean existsByRuc(String ruc);

    /**
     * Verifica si existe un proveedor con el RUC especificado excluyendo un ID
     * Útil para validaciones de actualización
     *
     * @param ruc RUC del proveedor
     * @param id ID del proveedor a excluir
     * @return true si existe otro proveedor con ese RUC
     */
    boolean existsByRucAndIdNot(String ruc, Long id);

    /**
     * Cuenta los proveedores activos
     *
     * @return Número de proveedores activos
     */
    long countByActivo(Boolean activo);

    /**
     * Busca proveedores ordenados por razón social
     *
     * @return Lista de proveedores ordenados
     */
    @Query("SELECT p FROM Proveedor p ORDER BY p.razonSocial ASC")
    List<Proveedor> findAllOrderByRazonSocial();

    /**
     * Busca proveedores activos con al menos un producto
     *
     * @return Lista de proveedores
     */
    @Query("SELECT DISTINCT p FROM Proveedor p LEFT JOIN p.productos prod WHERE p.activo = true AND prod.activo = true")
    List<Proveedor> findProveedoresActivosConProductos();

    /**
     * Busca proveedores por nombre comercial
     *
     * @param nombreComercial Nombre comercial a buscar
     * @return Lista de proveedores
     */
    List<Proveedor> findByNombreComercialContainingIgnoreCase(String nombreComercial);
}