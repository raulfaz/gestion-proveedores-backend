package com.empresa.gestionproveedoresbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase principal de la aplicación Gestión de Proveedores Backend
 *
 * @author Sistema
 * @version 1.0.0
 */
@SpringBootApplication
public class GestionProveedoresBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionProveedoresBackendApplication.class, args);
        System.out.println("=================================================");
        System.out.println("   Gestión Proveedores Backend - INICIADO");
        System.out.println("   Puerto: 8081");
        System.out.println("   Context Path: /api");
        System.out.println("   Perfil: default");
        System.out.println("=================================================");
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend JSF/PrimeFaces
     *
     * @return WebMvcConfigurer con la configuración CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "http://localhost:8081",
                                "http://localhost:3000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}