package com.empresa.gestionproveedoresbackend.report;

import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import com.empresa.gestionproveedoresbackend.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteProveedoresService {

    private final ProveedorRepository proveedorRepository;

    /**
     * Genera reporte PDF de proveedores
     */
    public byte[] generarPdf() {
        try {
            log.info("🚀 Iniciando generación de reporte PDF de proveedores");

            // 1. Obtener datos
            List<Proveedor> proveedores = proveedorRepository.findAll();
            log.info("📊 Proveedores encontrados: {}", proveedores.size());

            if (proveedores.isEmpty()) {
                log.warn("⚠️ No hay proveedores para generar el reporte");
                throw new IllegalStateException("No hay datos de proveedores para generar el reporte");
            }

            // 2. Cargar plantilla .jasper
            InputStream jasperStream = new ClassPathResource("reports/proveedores_listado.jasper")
                    .getInputStream();

            log.info("✅ Plantilla jasper cargada correctamente");

            // 3. Preparar parámetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("TITULO", "LISTADO DE PROVEEDORES");
            parametros.put("FECHA_GENERACION", Date.from(
                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
            ));

            log.info("📝 Parámetros configurados");

            // 4. Crear datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proveedores);

            // 5. Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperStream,
                    parametros,
                    dataSource
            );

            log.info("📄 Reporte llenado correctamente - Páginas: {}", jasperPrint.getPages().size());

            // 6. Exportar a PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            log.info("✅ PDF generado exitosamente - Tamaño: {} KB", pdfBytes.length / 1024);

            return pdfBytes;

        } catch (JRException e) {
            log.error("❌ Error de JasperReports: {}", e.getMessage(), e);
            throw new RuntimeException("Error generando reporte PDF: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ Error inesperado generando reporte: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al generar reporte PDF", e);
        }
    }

    /**
     * Genera reporte de proveedores activos
     */
    public byte[] generarPdfActivos() {
        try {
            log.info("🚀 Generando reporte de proveedores ACTIVOS");

            List<Proveedor> proveedoresActivos = proveedorRepository.findByActivo(true);
            log.info("📊 Proveedores activos: {}", proveedoresActivos.size());

            InputStream jasperStream = new ClassPathResource("reports/proveedores_listado.jasper")
                    .getInputStream();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("TITULO", "PROVEEDORES ACTIVOS");
            parametros.put("FECHA_GENERACION", new Date());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proveedoresActivos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            log.info("✅ PDF de proveedores activos generado");

            return pdf;

        } catch (Exception e) {
            log.error("❌ Error generando PDF de activos: {}", e.getMessage(), e);
            throw new RuntimeException("Error generando reporte de proveedores activos", e);
        }
    }
}