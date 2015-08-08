/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.enumeration;

/**
 *
 * @author jorge-luis
 */
public enum EventoEnum {

    ACTIVIDAD("ACTIVIDAD"),
    SUSTENTACIONPUBLICA("SUSTENTACION_PUBLICA"),
    SUSTENTACIONPRIVADA("SUSTENTACION_PRIVADA");
    String tipo;

    private EventoEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
