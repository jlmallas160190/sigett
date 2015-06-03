/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import edu.unl.sigett.docenteProyecto.ReporteFePresentacionPertinencia;
import edu.unl.sigett.docenteProyecto.ReporteOficioPertinencia;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author jorge-luis
 */
public class ReporteController {

    private static final Logger LOG = Logger.getLogger(ReporteController.class.getName());

    /**
     * Reporte oficio de pertinencia
     *
     * @param reporteOficioPertinenciaDTO
     * @return
     */
    public byte[] oficioPertinencia(final ReporteOficioPertinencia reporteOficioPertinenciaDTO) {
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
            p.put("cargoDestinatario", reporteOficioPertinenciaDTO.getCargoDestinatario().toUpperCase());
            p.put("destinatario", reporteOficioPertinenciaDTO.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteOficioPertinenciaDTO.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteOficioPertinenciaDTO.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteOficioPertinenciaDTO.getCuerpo());
            p.put("despedida", reporteOficioPertinenciaDTO.getDespedida());
            p.put("logoInstitucion", reporteOficioPertinenciaDTO.getRutaLogoIntitucion());
            p.put("responsable", reporteOficioPertinenciaDTO.getResponsable());
            p.put("autores", reporteOficioPertinenciaDTO.getAutores());
            File fileReport = new File(reporteOficioPertinenciaDTO.getRuta());
            bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());

//            if (reporteOficioPertinenciaDTO.getTipoArchivo().equalsIgnoreCase("pdf")) {
//                reporteOficioPertinenciaDTO.getResponse().setCharacterEncoding("ISO-8859-1");
//                reporteOficioPertinenciaDTO.getResponse().setContentType("application/pdf");
//                reporteOficioPertinenciaDTO.getResponse().setContentLength(bytes.length);
//                reporteOficioPertinenciaDTO.getResponse().getOutputStream().write(bytes, 0, bytes.length);
//                reporteOficioPertinenciaDTO.getResponse().getOutputStream().flush();
//                reporteOficioPertinenciaDTO.getResponse().getOutputStream().close();
//                return bytes;
//            }
//            reporteOficioPertinenciaDTO.getResponse().setCharacterEncoding("ISO-8859-1");
//            reporteOficioPertinenciaDTO.getResponse().addHeader("Content-disposition", "attachment; filename=informeDocenteProyecto.docx");
//            JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
//            ServletOutputStream servletOutputStream = reporteOficioPertinenciaDTO.getResponse().getOutputStream();
//            JRDocxExporter docxExporter = new JRDocxExporter();
//            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
//            docxExporter.exportReport();
            return bytes;

        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

    /**
     * Reporte fe de presentación de pertinencia
     *
     * @param reporteFePresentacionPertinencia
     * @return
     */
    public byte[] fepertinencia(final ReporteFePresentacionPertinencia reporteFePresentacionPertinencia) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            p.put("referencia", reporteFePresentacionPertinencia.getReferencia());
            p.put("cuerpo", reporteFePresentacionPertinencia.getCuerpo());
            p.put("final", reporteFePresentacionPertinencia.getParteFinal());
            p.put("firmaInvolucrados", reporteFePresentacionPertinencia.getFirmaInvolucrados());
            p.put("responsable", reporteFePresentacionPertinencia.getResponsable());
            File fileReport = new File(reporteFePresentacionPertinencia.getRuta());
            bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());

//            if (reporteFePresentacionPertinencia.getTipoArchivo().equalsIgnoreCase("pdf")) {
//                reporteFePresentacionPertinencia.getResponse().setCharacterEncoding("ISO-8859-1");
//                reporteFePresentacionPertinencia.getResponse().setContentType("application/pdf");
//                reporteFePresentacionPertinencia.getResponse().setContentLength(bytes.length);
//                reporteFePresentacionPertinencia.getResponse().getOutputStream().write(bytes, 0, bytes.length);
//                reporteFePresentacionPertinencia.getResponse().getOutputStream().flush();
//                reporteFePresentacionPertinencia.getResponse().getOutputStream().close();
//                return bytes;
//            }
//            reporteFePresentacionPertinencia.getResponse().setCharacterEncoding("ISO-8859-1");
//            reporteFePresentacionPertinencia.getResponse().addHeader("Content-disposition", "attachment; filename=informeDocenteProyecto.docx");
//            JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
//            ServletOutputStream servletOutputStream = reporteFePresentacionPertinencia.getResponse().getOutputStream();
//            JRDocxExporter docxExporter = new JRDocxExporter();
//            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
//            docxExporter.exportReport();
            return bytes;
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }
}
