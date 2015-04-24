/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.enumeration;

/**
 *
 * @author jorge-luis
 */
public enum TipoContratoEnum {

    CONTRATADO("CONTRATADO"),
    TITULAR("TITULAR");
    String tipo;

    private TipoContratoEnum(String tipo) {
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
