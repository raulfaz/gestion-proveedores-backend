package com.empresa.gestionproveedoresbackend.controller;

import com.empresa.gestionproveedoresbackend.report.ReporteProveedoresService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private final ReporteProveedoresService proveedoresService;

    @GetMapping("/proveedores/pdf")
    public ResponseEntity<byte[]> proveedoresPdf() {
        log.info("Generando reporte PDF de proveedores");
        byte[] pdf = proveedoresService.generarPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=proveedores.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}