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
public enum ValorEnum {

    SI("SI"),
    DIASSEMANA("7"),
    DIVISORPORCENTAJE("100"),
    NO("NO");
    String tipo;

    private ValorEnum(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "ValorEnum{" + "tipo=" + tipo + '}';
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
