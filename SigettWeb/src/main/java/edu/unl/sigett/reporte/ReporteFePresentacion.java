/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reporte;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ReporteFePresentacion implements Serializable {

    private String referencia;
    private String cuerpo;
    private String firmaInvolucrados;
    private String parteFinal;
    private String ruta;
    private String responsable;

    public ReporteFePresentacion(String referencia, String cuerpo, String firmaInvolucrados, String parteFinal,String responsable,String ruta) {
        this.referencia = referencia;
        this.cuerpo = cuerpo;
        this.firmaInvolucrados = firmaInvolucrados;
        this.parteFinal = parteFinal;
        this.responsable=responsable;
        this.ruta=ruta;
    }

    public ReporteFePresentacion() {
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getFirmaInvolucrados() {
        return firmaInvolucrados;
    }

    public void setFirmaInvolucrados(String firmaInvolucrados) {
        this.firmaInvolucrados = firmaInvolucrados;
    }

    public String getParteFinal() {
        return parteFinal;
    }

    public void setParteFinal(String parteFinal) {
        this.parteFinal = parteFinal;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
