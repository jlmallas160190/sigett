/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.entity.Documento;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.DocumentoCarrera;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class OficioPertinenciaDTO implements Serializable {

    private DocumentoCarrera documentoCarrera;
    private Documento documento;
    private Carrera carrera;

    public OficioPertinenciaDTO() {
    }

    public OficioPertinenciaDTO(DocumentoCarrera documentoCarrera, Documento documento, Carrera carrera) {
        this.documentoCarrera = documentoCarrera;
        this.documento = documento;
        this.carrera = carrera;
    }

    public DocumentoCarrera getDocumentoCarrera() {
        return documentoCarrera;
    }

    public void setDocumentoCarrera(DocumentoCarrera documentoCarrera) {
        this.documentoCarrera = documentoCarrera;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

}
