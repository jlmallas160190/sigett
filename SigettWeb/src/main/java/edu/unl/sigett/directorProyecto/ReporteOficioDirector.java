/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.directorProyecto;

import edu.unl.sigett.reporte.ReporteOficio;

/**
 *
 * @author jorge-luis
 */
public class ReporteOficioDirector extends ReporteOficio {

    private String autores;

    public ReporteOficioDirector(byte[] logoCarrera, String rutaLogoIntitucion, String nombreCarrera, String nombreArea, String carreraSigla,
            String etiquetaNO, String cabecera1, String cabecera2, String lugar, String fecha, String numeracion, String cargoDestinatario,
            String destinatario, String cargoQuienFirma, String datosQuienFirma, String cuerpo, String referencia, String asunto, String despedida,
            String saludo, String tipoArchivo, String responsable, String ruta, String autores) {
        super(logoCarrera, rutaLogoIntitucion, nombreCarrera, nombreArea, carreraSigla, etiquetaNO, cabecera1, cabecera2, lugar,
                fecha, numeracion, cargoDestinatario, destinatario, cargoQuienFirma, datosQuienFirma, cuerpo, referencia, asunto,
                despedida, saludo, tipoArchivo, responsable, ruta);
        this.autores = autores;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

}
