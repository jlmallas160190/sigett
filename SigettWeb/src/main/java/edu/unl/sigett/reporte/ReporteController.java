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
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
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
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }
}
