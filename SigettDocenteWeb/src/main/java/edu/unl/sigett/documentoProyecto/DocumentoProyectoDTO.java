/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.entity.Documento;
import edu.unl.sigett.entity.DocumentoProyecto;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class DocumentoProyectoDTO implements Serializable {

    private DocumentoProyecto documentoProyecto;
    private Documento documento;

    public DocumentoProyectoDTO() {
    }

    public DocumentoProyectoDTO(DocumentoProyecto documentoProyecto, Documento documento) {
        this.documentoProyecto = documentoProyecto;
        this.documento = documento;
    }

    public DocumentoProyecto getDocumentoProyecto() {
        return documentoProyecto;
    }

    public void setDocumentoProyecto(DocumentoProyecto documentoProyecto) {
        this.documentoProyecto = documentoProyecto;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

}
