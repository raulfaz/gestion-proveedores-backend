package com.empresa.gestionproveedoresbackend.report;

import com.empresa.gestionproveedoresbackend.model.entity.Proveedor;
import com.empresa.gestionproveedoresbackend.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReporteProveedoresService {

    private final ProveedorRepository proveedorRepository;

    public byte[] generarPdf() {
        try {
            // 1) Datos
            List<Proveedor> proveedores = proveedorRepository.findAll();

            // 2) Cargar el .jasper desde resources (ajusta el nombre si usas guión en lugar de guion_bajo)
            InputStream jasperStream = getClass().getResourceAsStream("/reports/proveedores_listado.jasper");
            if (jasperStream == null) {
                throw new IllegalStateException("No se encontró /reports/proveedores_listado.jasper en el classpath");
            }

            // 3) Parámetros
            Map<String, Object> params = new HashMap<>();
            params.put("TITULO", "Listado de Proveedores");
            params.put("FECHA_GENERACION", Date.from(Instant.now()));

            // 4) DataSource de beans
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(proveedores);

            // 5) Llenar reporte directamente desde el InputStream (.jasper)
            JasperPrint print = JasperFillManager.fillReport(jasperStream, params, ds);

            // 6) Exportar a PDF
            var exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            var baos = new java.io.ByteArrayOutputStream();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();

            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generando reporte PDF de proveedores", e);
            throw new RuntimeException("Error generando reporte PDF", e);
        }
    }
}