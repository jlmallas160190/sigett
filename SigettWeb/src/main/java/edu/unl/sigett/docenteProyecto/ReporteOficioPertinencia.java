/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import edu.unl.sigett.reporte.ReporteOficio;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jorge-luis
 */
public class ReporteOficioPertinencia extends ReporteOficio {

    private String autores;

    public ReporteOficioPertinencia(byte[] logoCarrera, String rutaLogoIntitucion, String nombreCarrera, String nombreArea, String carreraSigla,
            String etiquetaNO, String cabecera1, String cabecera2, String lugar, String fecha, String numeracion, String cargoDestinatario,
            String destinatario, String cargoQuienFirma, String datosQuienFirma, String cuerpo, String referencia, String asunto, String despedida,
            String saludo, String tipoArchivo, String responsable, String autores, String ruta, HttpServletResponse response) {
        super(logoCarrera, rutaLogoIntitucion, nombreCarrera, nombreArea, carreraSigla, etiquetaNO, cabecera1, cabecera2, lugar, fecha, numeracion,
                cargoDestinatario, destinatario, cargoQuienFirma, datosQuienFirma, cuerpo, referencia, asunto, despedida, saludo, tipoArchivo,
                responsable, ruta, response);
        this.autores = autores;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

}
