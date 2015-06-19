/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoActividad;

import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.service.DocumentoService;
import edu.unl.sigett.actividad.SessionActividad;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.service.DocumentoActividadService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoActividadController")
@SessionScoped
public class DocumentoActividadController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionDocumentoActividad sessionDocumentoActividad;
    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private DocumentoActividadService documentoActividadService;
    @EJB
    private DocumentoService documentoService;
    
    public DocumentoActividadController() {
    }
    
    public void crear() {
        sessionDocumentoActividad.setDocumentoActividadDTO(new DocumentoActividadDTO(new Documento(),
                new DocumentoActividad(null, Boolean.TRUE, null, sessionActividad.getActividad())));
    }
    
    public void editar(DocumentoActividadDTO documentoActividadDTO) {
        sessionDocumentoActividad.setDocumentoActividadDTO(documentoActividadDTO);
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTipo("pdf");
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setContents(event.getFile().getContents());
        Long size = event.getFile().getSize();
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTamanio(size.doubleValue());
        agregar();
    }
    
    public void remover(DocumentoActividadDTO documentoActividadDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionActividad.getDocumentosActividadDTO().remove(documentoActividadDTO);
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("eliminar"), "");
        if (documentoActividadDTO.getDocumentoActividad().getId() != null) {
            Documento documento = documentoService.buscarPorId(new Documento(documentoActividadDTO.getDocumento().getId()));
            if (documento != null) {
                documentoService.eliminar(documento);
            }
            documentoActividadService.eliminar(documentoActividadDTO.getDocumentoActividad());
        }
    }
    
    public void agregar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionActividad.getDocumentosActividadDTO().add(sessionDocumentoActividad.getDocumentoActividadDTO());
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("agregar"), "");
    }
}
