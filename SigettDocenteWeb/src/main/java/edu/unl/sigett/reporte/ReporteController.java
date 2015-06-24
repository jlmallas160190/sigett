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
import org.jlmallas.util.Certificado;

/**
 *
 * @author jorge-luis
 */
public class ReporteController {

    private static final Logger LOG = Logger.getLogger(ReporteController.class.getName());

    /**
     * Reporte oficio de pertinencia
     *
     * @param reporteOficio
     * @return
     */
    public byte[] informePertinencia(final ReporteOficio reporteOficio) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(reporteOficio.getLogoCarrera());
            p.put("lugarFecha", reporteOficio.getLugar() + ", " + reporteOficio.getFecha());
            p.put("cabecera1", reporteOficio.getCabecera1());
            p.put("cabecera2", reporteOficio.getCabecera2());
            p.put("saludo", reporteOficio.getSaludo());
            p.put("carreraLogo", is);
            p.put("cargoDestinatario", reporteOficio.getCargoDestinatario().toUpperCase());
            p.put("destinatario", reporteOficio.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteOficio.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteOficio.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteOficio.getCuerpo());
            p.put("despedida", reporteOficio.getDespedida());
            p.put("referencia", reporteOficio.getReferencia());
            p.put("selloInstitucion", reporteOficio.getRutaLogoIntitucion());
            File fileReport = new File(reporteOficio.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

    /**
     *
     * @param certificadoReporte
     * @return
     */
    public byte[] certificado(final CertificadoReporte certificadoReporte) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            p.put("lugarFecha", certificadoReporte.getLugar() + ", " + certificadoReporte.getFecha());
            p.put("titulo", certificadoReporte.getTitulo());
            p.put("quienCertifica", certificadoReporte.getDatosQuienCertifica());
            p.put("cargoQuienCertifica", certificadoReporte.getCargoQuienCertifica());
            p.put("cuerpo", certificadoReporte.getCuerpo());
            File fileReport = new File(certificadoReporte.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

}
