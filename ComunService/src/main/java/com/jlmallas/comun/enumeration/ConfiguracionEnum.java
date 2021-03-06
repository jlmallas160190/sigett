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
    RUTADOCUMENTOACTIVIDAD("RUTA_DOCUMENTO_ACTIVIDAD"),
    RUTAINFORME("RUTA_INFORME"),
    RUTAOFICIO("RUTA_OFICIO"),
    RUTAFEPRESENTACION("RUTA_FE_PRESENTACION"),
    TAMANIOARCHIVO("TAMANIO_ARCHIVO"),
    RUTALOGOINSTITUCION("RUTA_LOGO_INSTITUCION"),
    RUTAREPORTEOFICIO("RUTA_REPORTE_OFICIO"),
    RUTAINFORMEPERTINENCIA("RUTA_INFORME_PERTINENCIA"),
    RUTAREPORTEFEPRESENTACION("RUTA_REPORTE_FE_PRESENTACION"),
    RUTAREPORTEFECERTIFICADO("RUTA_REPORTE_CERTIFICADO_DIRECTOR"),
    CLAVEWS("CLAVE_WS"),
    USUARIOWS("USUARIO_WS"),
    LOGINDOCENTEWS("LOGIN_DOCENTE_WS"),
    LOGINESTUDIANTEWS("LOGIN_ESTUDIANTE_WS"),
    TIEMPODESARROLLOTT("TIEMPO_DESARROLLO_TT"),
    AGREGARDOCENTEPROYECTO("AGREGAR_DOCENTE_PROYECTO"),
    AGREGARMIEMBRODIRECTOR("AGREGAR_MIEMBRO_DIRECTOR"),
    AGREGARDIRECTORPROYECTO("AGREGAR_DIRECTOR_PROYECTO"),
    PORCENTAJEPRORROGA("PORCENTAJE_PRORROGA"),
    TIEMPOGRACIA("TIEMPO_GRACIA"),
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
