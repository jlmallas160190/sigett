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
public enum CatalogoDocumentoCarreraEnum {

    DOCENTEPROYECTO("DOCENTE_PROYECTO"),
    PERTINENCIA("PERTINENCIA"),
    DIRECTORPROYECTO("DIRECTOR_PROYECTO"),
    PRORROGA("PRORROGA"),
    INFORMEDIRECTORPRORROGA("INFORME_DIRECTOR_PRORROGA"),
    RESPUESTAPRORROGAAUTOR("INFORME_PRORROGA_AUTOR"),
    MIEMBROTRIBUNALPRIVADA("MIEMBRO_TRIBUNAL_PRIVADA"),
    MIEMBROTRIBUNALPUBLICA("MIEMBRO_TRIBUNAL_PUBLICA");
    String tipo;

    private CatalogoDocumentoCarreraEnum(String tipo) {
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
