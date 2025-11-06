package com.empresa.gestionproveedoresbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuración para habilitar la auditoría automática de JPA
 * Permite el uso de @CreatedDate y @LastModifiedDate en las entidades
 *
 * @author Sistema
 * @version 1.0.0
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // La anotación @EnableJpaAuditing activa automáticamente la auditoría
}