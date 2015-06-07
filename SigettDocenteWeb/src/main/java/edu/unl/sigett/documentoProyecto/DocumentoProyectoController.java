/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoProyectoController")
@SessionScoped
public class DocumentoProyectoController implements Serializable {
    
    @Inject
    private DocumentoProyectoDM documentoProyectoDM;
    @Inject
    private CabeceraController cabeceraController;
    private static final Logger LOG = Logger.getLogger(DocumentoProyectoController.class.getName());
    
    public DocumentoProyectoController() {
    }
    
    public void editar(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            File file = new File(documentoProyectoDTO.getDocumento().getRuta());
            documentoProyectoDTO.getDocumento().setContents(cabeceraController.getUtilService().obtenerBytes(file));
            documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(documentoProyectoDTO);
            documentoProyectoDM.setRenderedMediaDocumentoProyecto(Boolean.TRUE);
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
    public void cancelarEdicion(){
        documentoProyectoDM.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO());
        documentoProyectoDM.setRenderedMediaDocumentoProyecto(Boolean.FALSE);
    }
}
