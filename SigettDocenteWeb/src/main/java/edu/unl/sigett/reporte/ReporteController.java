/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import edu.unl.sigett.pertinencia.ReporteInformePertinencia;
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
     * @param reporteInformePertinencia
     * @return
     */
    public byte[] informePertinencia(final ReporteInformePertinencia reporteInformePertinencia) {
        byte[] bytes = null;
        try {
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(reporteInformePertinencia.getLogoCarrera());
            p.put("lugarFecha", reporteInformePertinencia.getLugar() + ", " + reporteInformePertinencia.getFecha());
            p.put("cabecera1", reporteInformePertinencia.getCabecera1());
            p.put("cabecera2", reporteInformePertinencia.getCabecera2());
            p.put("saludo", reporteInformePertinencia.getSaludo());
            p.put("carreraLogo", is);
            p.put("cargoDestinatario", reporteInformePertinencia.getCargoDestinatario().toUpperCase());
            p.put("destinatario", reporteInformePertinencia.getDestinatario().toUpperCase());
            p.put("quienFirma", reporteInformePertinencia.getDatosQuienFirma().toUpperCase());
            p.put("cargoQuienFirma", reporteInformePertinencia.getCargoQuienFirma().toUpperCase());
            p.put("cuerpo", reporteInformePertinencia.getCuerpo());
            p.put("despedida", reporteInformePertinencia.getDespedida());
            p.put("selloInstitucion", reporteInformePertinencia.getRutaLogoIntitucion());
            File fileReport = new File(reporteInformePertinencia.getRuta());
            return JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
        } catch (JRException ex) {
            LOG.info(ex.getMessage());
        }
        return bytes;
    }

}
