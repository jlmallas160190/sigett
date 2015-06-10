/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import edu.unl.sigett.directorProyecto.ReporteFePresentacionDirector;
import edu.unl.sigett.directorProyecto.ReporteOficioDirector;
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
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

    /**
     * REPORTE OFICIO DE DIRECTOR
     *
     * @param reporteOficioDirector
     * @return
     */
    public byte[] oficioDirector(final ReporteOficioDirector reporteOficioDirector) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(reporteOficioDirector.getLogoCarrera());
            p.put("lugarFecha", reporteOficioDirector.getLugar() + ", " + reporteOficioDirector.getFecha());
            p.put("carrera", reporteOficioDirector.getNombreCarrera().toUpperCase());
            p.put("numeracion", reporteOficioDirector.getNumeracion());
            p.put("etiquetaNO", reporteOficioDirector.getEtiquetaNO());
            p.put("cabecera1", reporteOficioDirector.getCabecera1());
            p.put("cabecera2", reporteOficioDirector.getCabecera2());
            p.put("saludo", reporteOficioDirector.getSaludo());
            p.put("logoCarrera", is);
            p.put("area", reporteOficioDirector.getNombreArea());
            p.put("cargoDestinatario", reporteOficioDirector.getCargoDestinatario().toUpperCase());
            p.put("destinatario", reporteOficioDirector.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteOficioDirector.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteOficioDirector.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteOficioDirector.getCuerpo());
            p.put("despedida", reporteOficioDirector.getDespedida());
            p.put("logoInstitucion", reporteOficioDirector.getRutaLogoIntitucion());
            p.put("responsable", reporteOficioDirector.getResponsable());
            p.put("autores", reporteOficioDirector.getAutores());
            File fileReport = new File(reporteOficioDirector.getRuta());
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
    /**
     * Reporte fe de presentación de pertinencia
     *
     * @param reporteFePresentacionDirector
     * @return
     */
    public byte[] feDirector(final ReporteFePresentacionDirector reporteFePresentacionDirector) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            p.put("referencia", reporteFePresentacionDirector.getReferencia());
            p.put("cuerpo", reporteFePresentacionDirector.getCuerpo());
            p.put("final", reporteFePresentacionDirector.getParteFinal());
            p.put("firmaInvolucrados", reporteFePresentacionDirector.getFirmaInvolucrados());
            p.put("responsable", reporteFePresentacionDirector.getResponsable());
            File fileReport = new File(reporteFePresentacionDirector.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }
}
