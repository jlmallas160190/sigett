/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import edu.unl.sigett.reporte.ReporteFePresentacion;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jorge-luis
 */
public class ReporteFePresentacionPertinencia extends ReporteFePresentacion {

    private HttpServletResponse response;
    private String ruta;
    private String tipoArchivo;

    public ReporteFePresentacionPertinencia() {
    }

    public ReporteFePresentacionPertinencia(HttpServletResponse response, String ruta, String tipoArchivo, String referencia, String cuerpo,
            String firmaInvolucrados, String parteFinal, String responsable) {
        super(referencia, cuerpo, firmaInvolucrados, parteFinal, responsable);
        this.ruta = ruta;
        this.response = response;
        this.tipoArchivo = tipoArchivo;
    }

    public ReporteFePresentacionPertinencia(String ruta, String tipoArchivo) {
        this.ruta = ruta;
        this.tipoArchivo = tipoArchivo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

}
