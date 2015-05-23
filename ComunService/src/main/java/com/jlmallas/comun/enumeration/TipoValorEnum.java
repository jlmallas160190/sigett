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
public enum TipoValorEnum {

    NUMERICO("NUMERICO"),
    TEXTO("NUMERICO"),
    SELECCIONUNICA("SELECCION_UNICA"),
    SELECCIONMULTIPLE("SELECCION_MULTIPLE");
    String tipo;

    private TipoValorEnum(String tipo) {
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
