/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoProyectoDM")
@SessionScoped
public class DocumentoProyectoDM implements Serializable {

    private DocumentoProyectoDTO documentoProyectoDTOSeleccionado;
    private Boolean renderedMediaDocumentoProyecto;

    public DocumentoProyectoDM() {
        this.documentoProyectoDTOSeleccionado = new DocumentoProyectoDTO();
    }

    public DocumentoProyectoDTO getDocumentoProyectoDTOSeleccionado() {
        return documentoProyectoDTOSeleccionado;
    }

    public void setDocumentoProyectoDTOSeleccionado(DocumentoProyectoDTO documentoProyectoDTOSeleccionado) {
        this.documentoProyectoDTOSeleccionado = documentoProyectoDTOSeleccionado;
    }

    public Boolean getRenderedMediaDocumentoProyecto() {
        return renderedMediaDocumentoProyecto;
    }

    public void setRenderedMediaDocumentoProyecto(Boolean renderedMediaDocumentoProyecto) {
        this.renderedMediaDocumentoProyecto = renderedMediaDocumentoProyecto;
    }

}
