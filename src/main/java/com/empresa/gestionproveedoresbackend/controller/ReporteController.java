package com.empresa.gestionproveedoresbackend.controller;

import com.empresa.gestionproveedoresbackend.dto.ApiResponseDTO;
import com.empresa.gestionproveedoresbackend.report.ReporteProveedoresService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador REST para generación de reportes
 */
@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private final ReporteProveedoresService proveedoresService;

    /**
     * Genera reporte PDF de todos los proveedores
     * GET /api/reportes/proveedores/pdf
     */
    @GetMapping("/proveedores/pdf")
    public ResponseEntity<?> generarPdfProveedores() {
        try {
            log.info("📥 Petición de reporte PDF de proveedores");

            byte[] pdf = proveedoresService.generarPdf();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("proveedores_%s.pdf", timestamp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", filename);
            headers.setContentLength(pdf.length);

            log.info("✅ PDF generado exitosamente: {}", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (IllegalStateException e) {
            log.warn("⚠️ {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponseDTO.error(e.getMessage()));

        } catch (Exception e) {
            log.error("❌ Error generando PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error generando el reporte: " + e.getMessage()));
        }
    }

    /**
     * Genera reporte PDF de proveedores activos
     * GET /api/reportes/proveedores/activos/pdf
     */
    @GetMapping("/proveedores/activos/pdf")
    public ResponseEntity<?> generarPdfProveedoresActivos() {
        try {
            log.info("📥 Petición de reporte PDF de proveedores activos");

            byte[] pdf = proveedoresService.generarPdfActivos();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("proveedores_activos_%s.pdf", timestamp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", filename);
            headers.setContentLength(pdf.length);

            log.info("✅ PDF de activos generado: {}", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            log.error("❌ Error generando PDF de activos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error generando reporte de activos"));
        }
    }

    /**
     * Descarga directa del PDF (attachment)
     * GET /api/reportes/proveedores/descargar
     */
    @GetMapping("/proveedores/descargar")
    public ResponseEntity<?> descargarPdfProveedores() {
        try {
            log.info("📥 Petición de descarga de PDF");

            byte[] pdf = proveedoresService.generarPdf();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("proveedores_%s.pdf", timestamp);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename); // ← DESCARGA
            headers.setContentLength(pdf.length);

            log.info("✅ PDF preparado para descarga: {}", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            log.error("❌ Error en descarga de PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error("Error descargando el reporte"));
        }
    }
}