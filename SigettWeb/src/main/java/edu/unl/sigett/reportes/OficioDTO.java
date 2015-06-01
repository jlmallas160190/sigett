/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reportes;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class OficioDTO implements Serializable {

    private String lugar;
    private String fecha;
    private String numeracion;
    private String cargoDestinatario;
    private String destinatario;
    private String cargoQuienFirma;
    private String datosQuienFirma;
    private String cuerpo;
    private String referencia;
    private String asunto;
    private String despedida;
    private String saludo;

    public OficioDTO(String lugar, String fecha, String numeracion, String cargoDestinatario, String destinatario, String cargoQuienFirma, 
            String datosQuienFirma, String cuerpo, String referencia, String asunto, String despedida, String saludo) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.numeracion = numeracion;
        this.cargoDestinatario = cargoDestinatario;
        this.destinatario = destinatario;
        this.cargoQuienFirma = cargoQuienFirma;
        this.datosQuienFirma = datosQuienFirma;
        this.cuerpo = cuerpo;
        this.referencia = referencia;
        this.asunto = asunto;
        this.despedida = despedida;
        this.saludo = saludo;
    }

    
    public OficioDTO() {
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

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDespedida() {
        return despedida;
    }

    public void setDespedida(String despedida) {
        this.despedida = despedida;
    }

    public String getCargoDestinatario() {
        return cargoDestinatario;
    }

    public void setCargoDestinatario(String cargoDestinatario) {
        this.cargoDestinatario = cargoDestinatario;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCargoQuienFirma() {
        return cargoQuienFirma;
    }

    public void setCargoQuienFirma(String cargoQuienFirma) {
        this.cargoQuienFirma = cargoQuienFirma;
    }

    public String getDatosQuienFirma() {
        return datosQuienFirma;
    }

    public void setDatosQuienFirma(String datosQuienFirma) {
        this.datosQuienFirma = datosQuienFirma;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getSaludo() {
        return saludo;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

}
