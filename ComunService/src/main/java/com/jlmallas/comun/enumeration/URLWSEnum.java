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
public enum URLWSEnum {

    URLAREAWS("AREAS_WS"),
    URLOFERTAACADEMICAWS("OFERTAS_WS"),
    URLCARRERAWS("CARRERAS_WS"),
    URLPERIODOLECTIVOWS("PERIODOS_WS"),
    DATOSDOCENTE("DATOS_DOCENTE_WS"),
    VALIDARDOCENTE("VALIDAR_DOCENTE_WS"),
    PARALELOCARRERA("PARALELO_CARRERA_WS"),
    MATRICULAESTUDIANTE("MATRICULA_ESTUDIANTE_WS"),
    REPORTEMATRICULAESTUDIANTE("REPORTE_MATRICULA_ESTUDIANTE_WS"),
    ESTADOESTUDIANTEPARALELO("ESTADO_ESTUDIANTE_PARALELO_WS"),
    UNIDADDOCENTEPARALELO("UNIDAD_DOCENTE_PARALELO_WS"),
    DATOSESTUDIANTE("DATOS_ESTUDIANTE_WS");
    String tipo;

    private URLWSEnum(String tipo) {
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
