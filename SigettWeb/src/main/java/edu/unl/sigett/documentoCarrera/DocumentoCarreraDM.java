/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoCarrera;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoCarreraDM")
@SessionScoped
public class DocumentoCarreraDM implements Serializable {

    private DocumentoCarreraDTO documentoCarreraDTO;

    public DocumentoCarreraDM() {
        this.documentoCarreraDTO = new DocumentoCarreraDTO();
    }

    public DocumentoCarreraDTO getDocumentoCarreraDTO() {
        return documentoCarreraDTO;
    }

    public void setDocumentoCarreraDTO(DocumentoCarreraDTO documentoCarreraDTO) {
        this.documentoCarreraDTO = documentoCarreraDTO;
    }

}
