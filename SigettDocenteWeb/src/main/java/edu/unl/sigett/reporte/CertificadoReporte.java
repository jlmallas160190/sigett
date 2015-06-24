/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import org.jlmallas.util.Certificado;

/**
 *
 * @author jorge-luis
 */
public class CertificadoReporte extends Certificado {

    private String ruta;

    public CertificadoReporte(String ruta) {
        this.ruta = ruta;
    }

    public CertificadoReporte(String ruta, String lugar, String fecha, String cargoQuienCertifica, String datosQuienCertifica, String cuerpo, String titulo) {
        super(lugar, fecha, cargoQuienCertifica, datosQuienCertifica, cuerpo, titulo);
        this.ruta = ruta;
    }

   
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
