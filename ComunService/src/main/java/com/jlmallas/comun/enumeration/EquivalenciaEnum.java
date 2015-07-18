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
public enum EquivalenciaEnum {

    REPROBADO("REPROBADO"),
    REGULAR("REGULAR"),
    MUYBUENO("MUYBUENO"),
    BUENO("BUENO"),
    SOBRESALIENTE("SOBRESALIENTE");
    String tipo;

    private EquivalenciaEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
