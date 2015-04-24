/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.DocumentoProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocumentoProyecto implements Serializable {

    private DocumentoProyecto documentoProyecto;

    public SessionDocumentoProyecto() {
        this.documentoProyecto = new DocumentoProyecto();
    }

    public DocumentoProyecto getDocumentoProyecto() {
        return documentoProyecto;
    }

    public void setDocumentoProyecto(DocumentoProyecto documentoProyecto) {
        this.documentoProyecto = documentoProyecto;
    }

}
