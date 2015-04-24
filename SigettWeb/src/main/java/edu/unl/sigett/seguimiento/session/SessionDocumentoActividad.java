/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.session;

import edu.unl.sigett.entity.DocumentoActividad;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocumentoActividad implements Serializable {

    private DocumentoActividad documentoActividad;

    public SessionDocumentoActividad() {
        this.documentoActividad = new DocumentoActividad();
    }

    public DocumentoActividad getDocumentoActividad() {
        return documentoActividad;
    }

    public void setDocumentoActividad(DocumentoActividad documentoActividad) {
        this.documentoActividad = documentoActividad;
    }

}
