package com.empresa.gestionproveedoresbackend.service;

import com.empresa.gestionproveedoresbackend.dto.ProductoDTO;

import java.util.List;

/**
 * Interfaz de servicio para operaciones de negocio relacionadas con Productos
 *
 * @author Sistema
 * @version 1.0.0
 */
public interface ProductoService {

    /**
     * Crea un nuevo producto
     *
     * @param productoDTO Datos del producto
     * @return Producto creado
     */
    ProductoDTO crear(ProductoDTO productoDTO);

    /**
     * Actualiza un producto existente
     *
     * @param id ID del producto
     * @param productoDTO Datos actualizados
     * @return Producto actualizado
     */
    ProductoDTO actualizar(Long id, ProductoDTO productoDTO);

    /**
     * Busca un producto por ID
     *
     * @param id ID del producto
     * @return Producto encontrado
     */
    ProductoDTO buscarPorId(Long id);

    /**
     * Lista todos los productos
     *
     * @return Lista de productos
     */
    List<ProductoDTO> listarTodos();

    /**
     * Lista productos activos
     *
     * @return Lista de productos activos
     */
    List<ProductoDTO> listarActivos();

    /**
     * Lista productos por proveedor
     *
     * @param proveedorId ID del proveedor
     * @return Lista de productos
     */
    List<ProductoDTO> listarPorProveedor(Long proveedorId);

    /**
     * Busca productos por nombre
     *
     * @param nombre Texto a buscar
     * @return Lista de productos
     */
    List<ProductoDTO> buscarPorNombre(String nombre);

    /**
     * Busca un producto por código
     *
     * @param codigo Código del producto
     * @return Producto encontrado
     */
    ProductoDTO buscarPorCodigo(String codigo);

    /**
     * Busca productos por código o nombre
     *
     * @param searchTerm Término de búsqueda
     * @return Lista de productos
     */
    List<ProductoDTO> buscar(String searchTerm);

    /**
     * Activa o desactiva un producto
     *
     * @param id ID del producto
     * @param activo Estado a establecer
     * @return Producto actualizado
     */
    ProductoDTO cambiarEstado(Long id, Boolean activo);

    /**
     * Elimina un producto (eliminación lógica)
     *
     * @param id ID del producto
     */
    void eliminar(Long id);

    /**
     * Verifica si un código ya está registrado
     *
     * @param codigo Código a verificar
     * @param id ID a excluir (null para creación)
     * @return true si existe
     */
    boolean existeCodigo(String codigo, Long id);

    /**
     * Cuenta productos activos
     *
     * @return Número de productos activos
     */
    long contarActivos();
}