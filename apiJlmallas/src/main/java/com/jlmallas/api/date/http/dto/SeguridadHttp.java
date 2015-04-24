/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.api.date.http.dto;

/**
 *
 * @author jorge-luis
 */
public class SeguridadHttp {

    private String clave;
    private String url;
    private String usuario;

    public SeguridadHttp() {
    }

    public SeguridadHttp(String clave, String url, String usuario) {
        this.clave = clave;
        this.url = url;
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
