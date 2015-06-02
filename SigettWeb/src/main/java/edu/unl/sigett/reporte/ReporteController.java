/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

/**
 *
 * @author jorge-luis
 */
public class ReporteController {

    private static final Logger LOG = Logger.getLogger(ReporteController.class.getName());
    
    public byte[] pertinencia(final ReporteOficio reporteOficioPertinenciaDTO) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(reporteOficioPertinenciaDTO.getLogoCarrera());
            p.put("lugarFecha", reporteOficioPertinenciaDTO.getLugar() + ", " + reporteOficioPertinenciaDTO.getFecha());
            p.put("carrera", reporteOficioPertinenciaDTO.getNombreCarrera().toUpperCase());
            p.put("numeracion", reporteOficioPertinenciaDTO.getNumeracion());
            p.put("etiquetaNO", reporteOficioPertinenciaDTO.getEtiquetaNO());
            p.put("cabecera1", reporteOficioPertinenciaDTO.getCabecera1());
            p.put("cabecera2", reporteOficioPertinenciaDTO.getCabecera2());
            p.put("saludo", reporteOficioPertinenciaDTO.getSaludo());
            p.put("logoCarrera", is);
            p.put("area", reporteOficioPertinenciaDTO.getNombreArea());
            p.put("cargoDestinatario", reporteOficioPertinenciaDTO.getCargoDestinatario());
            p.put("destinatario", reporteOficioPertinenciaDTO.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteOficioPertinenciaDTO.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteOficioPertinenciaDTO.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteOficioPertinenciaDTO.getCuerpo());
            p.put("despedida", reporteOficioPertinenciaDTO.getDespedida());
            p.put("logoInstitucion", reporteOficioPertinenciaDTO.getRutaLogoIntitucion());
            File fileReport = new File(reporteOficioPertinenciaDTO.getRuta());
            bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
            
            if (reporteOficioPertinenciaDTO.getTipoArchivo().equalsIgnoreCase("pdf")) {
                reporteOficioPertinenciaDTO.getResponse().setCharacterEncoding("ISO-8859-1");
                reporteOficioPertinenciaDTO.getResponse().setContentType("application/pdf");
                reporteOficioPertinenciaDTO.getResponse().setContentLength(bytes.length);
                ServletOutputStream outStream = reporteOficioPertinenciaDTO.getResponse().getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
                return bytes;
            }
            reporteOficioPertinenciaDTO.getResponse().setCharacterEncoding("ISO-8859-1");
            reporteOficioPertinenciaDTO.getResponse().addHeader("Content-disposition", "attachment; filename=informeDocenteProyecto.docx");
            JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
            ServletOutputStream servletOutputStream = reporteOficioPertinenciaDTO.getResponse().getOutputStream();
            JRDocxExporter docxExporter = new JRDocxExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
            
        } catch (JRException | IOException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }
}
