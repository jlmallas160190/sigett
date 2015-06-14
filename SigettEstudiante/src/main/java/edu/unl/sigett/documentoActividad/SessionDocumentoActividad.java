/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoActividad;

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

    private DocumentoActividadDTO documentoActividadDTO;

    public SessionDocumentoActividad() {
        this.documentoActividadDTO = new DocumentoActividadDTO();
    }

    public DocumentoActividadDTO getDocumentoActividadDTO() {
        return documentoActividadDTO;
    }

    public void setDocumentoActividadDTO(DocumentoActividadDTO documentoActividadDTO) {
        this.documentoActividadDTO = documentoActividadDTO;
    }

}
