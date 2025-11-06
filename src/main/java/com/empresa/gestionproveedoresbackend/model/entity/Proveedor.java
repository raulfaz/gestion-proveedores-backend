package com.empresa.gestionproveedoresbackend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un Proveedor en el sistema
 *
 * @author Sistema
 * @version 1.0.0
 */
@Entity
@Table(name = "proveedores")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 13, message = "El RUC no puede tener más de 13 caracteres")
    @Column(name = "ruc", nullable = false, unique = true, length = 13)
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 200, message = "La razón social no puede tener más de 200 caracteres")
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @Size(max = 200, message = "El nombre comercial no puede tener más de 200 caracteres")
    @Column(name = "nombre_comercial", length = 200)
    private String nombreComercial;

    @Size(max = 300, message = "La dirección no puede tener más de 300 caracteres")
    @Column(name = "direccion", length = 300)
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 150, message = "El nombre del contacto no puede tener más de 150 caracteres")
    @Column(name = "contacto", length = 150)
    private String contacto;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con Productos
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    // Relación con Órdenes de Compra
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrdenCompra> ordenesCompra = new ArrayList<>();

    /**
     * Método de conveniencia para agregar un producto
     */
    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.setProveedor(this);
    }

    /**
     * Método de conveniencia para remover un producto
     */
    public void removerProducto(Producto producto) {
        productos.remove(producto);
        producto.setProveedor(null);
    }
}