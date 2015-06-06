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
public enum ConfiguracionEnum {

    TIEMPOPERTINENCIA("TIEMPO_PERTINENCIA"),
    RUTARDF("RUTA_RDF"),
    RUTADOCUMENTOPROYECTO("RUTA_DOCUMENTO_PROYECTO"),
    RUTAOFICIO("RUTA_OFICIO"),
    RUTAFEPRESENTACION("RUTA_FE_PRESENTACION"),
    TAMANIOARCHIVO("TAMANIO_ARCHIVO"),
    RUTALOGOINSTITUCION("RUTA_LOGO_INSTITUCION"),
    RUTAREPORTEPERTINENCIA("RUTA_REPORTE_PERTINENCIA"),
    RUTAREPORTEFEPERTINENCIA("RUTA_REPORTE_FE_PERTINENCIA"),
    CLAVEWS("CLAVE_WS"),
    USUARIOWS("USUARIO_WS"),
    LOGINDOCENTEWS("LOGIN_DOCENTE_WS"),
    URLAREAWS("AREAS_WS"),
    URLOFERTAACADEMICAWS("OFERTAS_WS"),
    URLCARRERAWS("CARRERAS_WS"),
    URLPERIODOLECTIVOWS("PERIODOS_WS"),
    SECRETKEY("SECRET_KEY");
    String tipo;

    private ConfiguracionEnum(String tipo) {
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
