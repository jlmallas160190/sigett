/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.enumeration;

/**
 *
 * @author jorge-luis
 */
public enum EstadoAutorEnum {

    INICIO("INICIO"),
    PERTINENTE("PERTINENTE"),
    SEGUIMIENTO("SEGUIMIENTO"),
    SUSTENTACIONPRIVADA("SUSTENTACIONPRIVADA"),
    RECUPERACIONPRIVADA("RECUPERACIONPRIVADA"),
    SUSTENTACIONPUBLICA("SUSTENTACIONPUBLICA"),
    RECUPERACIONPUBLICA("RECUPERACIONPUBLICA"),
    APROBADO("APROBADO"),
    REPROBADO("REPROBADO"),
    ABANDONADO("ABANDONADO");
    String tipo;

    private EstadoAutorEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return this.tipo;
    }
}
