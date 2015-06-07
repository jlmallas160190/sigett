/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ConfiguracionGeneralDTO implements Serializable {

    private String secureKey;
    private Double tamanioArchivo;

    public ConfiguracionGeneralDTO() {
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public Double getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(Double tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

}
