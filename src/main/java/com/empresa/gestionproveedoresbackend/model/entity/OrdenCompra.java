package com.empresa.gestionproveedoresbackend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una Orden de Compra en el sistema
 *
 * @author Sistema
 * @version 1.0.0
 */
@Entity
@Table(name = "ordenes_compra")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de orden es obligatorio")
    @Size(max = 50, message = "El número de orden no puede tener más de 50 caracteres")
    @Column(name = "numero_orden", nullable = false, unique = true, length = 50)
    private String numeroOrden;

    @NotNull(message = "La fecha de orden es obligatoria")
    @Column(name = "fecha_orden", nullable = false)
    private LocalDate fechaOrden;

    @NotNull(message = "El estado es obligatorio")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    private String estado = "PENDIENTE";

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.00", message = "El subtotal debe ser mayor o igual a 0")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.00", message = "El IVA debe ser mayor o igual a 0")
    @Column(name = "iva", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal iva = BigDecimal.ZERO;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.00", message = "El total debe ser mayor o igual a 0")
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Size(max = 500, message = "Las observaciones no pueden tener más de 500 caracteres")
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @CreatedDate
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    // Relación con Detalles de Orden de Compra
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleOrdenCompra> detalles = new ArrayList<>();

    /**
     * Método de conveniencia para agregar un detalle
     */
    public void agregarDetalle(DetalleOrdenCompra detalle) {
        detalles.add(detalle);
        detalle.setOrdenCompra(this);
    }

    /**
     * Método de conveniencia para remover un detalle
     */
    public void removerDetalle(DetalleOrdenCompra detalle) {
        detalles.remove(detalle);
        detalle.setOrdenCompra(null);
    }

    /**
     * Calcula los totales de la orden basándose en sus detalles
     */
    public void calcularTotales() {
        this.subtotal = detalles.stream()
                .map(DetalleOrdenCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // IVA del 15% en Ecuador (ajustar según el país)
        this.iva = this.subtotal.multiply(new BigDecimal("0.15"));
        this.total = this.subtotal.add(this.iva);
    }

    @Override
    public String toString() {
        return "OrdenCompra{" +
                "id=" + id +
                ", numeroOrden='" + numeroOrden + '\'' +
                ", fechaOrden=" + fechaOrden +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                '}';
    }
}