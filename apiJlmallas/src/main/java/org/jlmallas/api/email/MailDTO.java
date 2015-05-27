/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.api.email;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class MailDTO implements Serializable {

    private String smtp;
    private String puerto;
    private String usuario;
    private String clave;
    private String mensaje;
    private String archivo;
    private String destino;
    private String datosDestinario;

    public MailDTO(String smtp, String puerto, String usuario, String clave, String mensaje, String archivo, String destino, String datosDestinario) {
        this.smtp = smtp;
        this.puerto = puerto;
        this.usuario = usuario;
        this.clave = clave;
        this.mensaje = mensaje;
        this.archivo = archivo;
        this.destino = destino;
        this.datosDestinario = datosDestinario;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDatosDestinario() {
        return datosDestinario;
    }

    public void setDatosDestinario(String datosDestinario) {
        this.datosDestinario = datosDestinario;
    }

}
