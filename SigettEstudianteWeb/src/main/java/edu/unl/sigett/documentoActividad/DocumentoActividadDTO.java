/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoActividad;

import com.jlmallas.comun.entity.Documento;
import edu.unl.sigett.entity.DocumentoActividad;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class DocumentoActividadDTO implements Serializable {

    private Documento documento;
    private DocumentoActividad documentoActividad;

    public DocumentoActividadDTO() {
    }

    public DocumentoActividadDTO(Documento documento, DocumentoActividad documentoActividad) {
        this.documento = documento;
        this.documentoActividad = documentoActividad;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public DocumentoActividad getDocumentoActividad() {
        return documentoActividad;
    }

    public void setDocumentoActividad(DocumentoActividad documentoActividad) {
        this.documentoActividad = documentoActividad;
    }

}
