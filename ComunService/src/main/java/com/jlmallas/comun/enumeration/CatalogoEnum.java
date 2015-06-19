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
public enum CatalogoEnum {

    TIPODOCUMENTOIDENTIFICACION("TIPODOCUMENTOIDENTIFICACION"),
    GENERO("GENERO"),
    ESTADOESTUDIANTECARRERA("ESTADO_ESTUDIANTE_CARRERA"),
    ESTADOMATRICULA("ESTADO_MATRICULA"),
    TIPOCONTRATO("TIPOCONTRATO"),
    ESTADOPROYECTO("ESTADO_PROYECTO"),
    TIPOPROYECTO("TIPO_PROYECTO"),
    CATALOGOPROYECTO("CATALOGO_PROYECTO"),
    ESTADOAUTOR("ESTADO_AUTOR"),
    CATALOGOOFICIO("CATALOGO_OFICIO"),
    CATALOGOINFORME("CATALOGO_INFORME"),
    FEPRESENTACION("FE_PRESENTACION"),
    CATALOGODOCUMENTOPROYECTO("CATALOGO_DOCUMENTO_PROYECTO"),
    CATALOGODOCUMENTOCTIVIDAD("CATALOGO_DOCUMENTO_ACTIVIDAD"),
    CATALOGOEVENTO("CATALOGO_EVENTO"),
    TIPOACTIVIDAD("TIPO_ACTIVIDAD"),
    ESTADOACTIVIDAD("ESTADO_ACTIVIDAD"),
    ESTADODIRECTOR("ESTADO_DIRECTOR");
    String tipo;

    private CatalogoEnum(String tipo) {
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
