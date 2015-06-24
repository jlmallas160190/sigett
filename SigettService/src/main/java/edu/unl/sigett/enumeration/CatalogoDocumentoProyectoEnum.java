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
public enum CatalogoDocumentoProyectoEnum {

    ANTEPROYECTO("ANTEPROYECTO"),
    CERTIFICACIONDIRECTOR("CERTIFICACION_DIRECTOR"),
    DOCUMENTOFINAL("DOCUMENTO_FINAL");
    String tipo;

    private CatalogoDocumentoProyectoEnum(String tipo) {
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
