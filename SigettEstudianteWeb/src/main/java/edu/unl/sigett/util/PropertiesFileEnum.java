/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

/**
 *
 * @author jorge-luis
 */
public enum PropertiesFileEnum {

    ETIQUETASES("i18n/etiquetas_es.properties");
    String archivo;

    private PropertiesFileEnum(String tipo) {
        this.archivo = tipo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public String toString() {
        return this.archivo;
    }
}
