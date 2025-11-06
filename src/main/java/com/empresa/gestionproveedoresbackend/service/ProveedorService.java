package com.empresa.gestionproveedoresbackend.service;

import com.empresa.gestionproveedoresbackend.dto.ProveedorDTO;

import java.util.List;

/**
 * Interfaz de servicio para operaciones de negocio relacionadas con Proveedores
 *
 * @author Sistema
 * @version 1.0.0
 */
public interface ProveedorService {

    /**
     * Crea un nuevo proveedor
     *
     * @param proveedorDTO Datos del proveedor
     * @return Proveedor creado
     */
    ProveedorDTO crear(ProveedorDTO proveedorDTO);

    /**
     * Actualiza un proveedor existente
     *
     * @param id ID del proveedor
     * @param proveedorDTO Datos actualizados
     * @return Proveedor actualizado
     */
    ProveedorDTO actualizar(Long id, ProveedorDTO proveedorDTO);

    /**
     * Busca un proveedor por ID
     *
     * @param id ID del proveedor
     * @return Proveedor encontrado
     */
    ProveedorDTO buscarPorId(Long id);

    /**
     * Lista todos los proveedores
     *
     * @return Lista de proveedores
     */
    List<ProveedorDTO> listarTodos();

    /**
     * Lista proveedores activos
     *
     * @return Lista de proveedores activos
     */
    List<ProveedorDTO> listarActivos();

    /**
     * Busca proveedores por razón social
     *
     * @param razonSocial Texto a buscar
     * @return Lista de proveedores
     */
    List<ProveedorDTO> buscarPorRazonSocial(String razonSocial);

    /**
     * Busca un proveedor por RUC
     *
     * @param ruc RUC del proveedor
     * @return Proveedor encontrado
     */
    ProveedorDTO buscarPorRuc(String ruc);

    /**
     * Activa o desactiva un proveedor
     *
     * @param id ID del proveedor
     * @param activo Estado a establecer
     * @return Proveedor actualizado
     */
    ProveedorDTO cambiarEstado(Long id, Boolean activo);

    /**
     * Elimina un proveedor (eliminación lógica)
     *
     * @param id ID del proveedor
     */
    void eliminar(Long id);

    /**
     * Verifica si un RUC ya está registrado
     *
     * @param ruc RUC a verificar
     * @param id ID a excluir (null para creación)
     * @return true si existe
     */
    boolean existeRuc(String ruc, Long id);

    /**
     * Cuenta proveedores activos
     *
     * @return Número de proveedores activos
     */
    long contarActivos();
}