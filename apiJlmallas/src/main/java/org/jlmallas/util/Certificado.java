/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.util;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class Certificado implements Serializable {

    private String lugar;
    private String fecha;
    private String cargoQuienCertifica;
    private String datosQuienCertifica;
    private String cuerpo;
    private String titulo;

    public Certificado() {
    }

    public Certificado(String lugar, String fecha, String cargoQuienCertifica, String datosQuienCertifica, String cuerpo, String titulo) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.cargoQuienCertifica = cargoQuienCertifica;
        this.datosQuienCertifica = datosQuienCertifica;
        this.cuerpo = cuerpo;
        this.titulo = titulo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCargoQuienCertifica() {
        return cargoQuienCertifica;
    }

    public void setCargoQuienCertifica(String cargoQuienCertifica) {
        this.cargoQuienCertifica = cargoQuienCertifica;
    }

    public String getDatosQuienCertifica() {
        return datosQuienCertifica;
    }

    public void setDatosQuienCertifica(String datosQuienCertifica) {
        this.datosQuienCertifica = datosQuienCertifica;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
