/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;


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
     * Reporte oficio
     *
     * @param reporteOficio
     * @return
     */
    public byte[] oficio(final ReporteOficio reporteOficio) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(reporteOficio.getLogoCarrera());
            p.put("lugarFecha", reporteOficio.getLugar() + ", " + reporteOficio.getFecha());
            p.put("carrera", reporteOficio.getNombreCarrera().toUpperCase());
            p.put("numeracion", reporteOficio.getNumeracion());
            p.put("etiquetaNO", reporteOficio.getEtiquetaNO());
            p.put("cabecera1", reporteOficio.getCabecera1());
            p.put("cabecera2", reporteOficio.getCabecera2());
            p.put("saludo", reporteOficio.getSaludo());
            p.put("logoCarrera", is);
            p.put("area", reporteOficio.getNombreArea());
            p.put("cargoDestinatario", reporteOficio.getCargoDestinatario().toUpperCase());
            p.put("destinatario", reporteOficio.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteOficio.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteOficio.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteOficio.getCuerpo());
            p.put("despedida", reporteOficio.getDespedida());
            p.put("logoInstitucion", reporteOficio.getRutaLogoIntitucion());
            p.put("footer", reporteOficio.getFooter());
            File fileReport = new File(reporteOficio.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

   

    /**
     * Reporte fe de presentación
     *
     * @param reporteFePresentacionPresentacion
     * @return
     */
    public byte[] fePresentacion(final ReporteFePresentacion reporteFePresentacionPresentacion) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            p.put("referencia", reporteFePresentacionPresentacion.getReferencia());
            p.put("cuerpo", reporteFePresentacionPresentacion.getCuerpo());
            p.put("final", reporteFePresentacionPresentacion.getParteFinal());
            p.put("firmaInvolucrados", reporteFePresentacionPresentacion.getFirmaInvolucrados());
            p.put("responsable", reporteFePresentacionPresentacion.getResponsable());
            File fileReport = new File(reporteFePresentacionPresentacion.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }
}
