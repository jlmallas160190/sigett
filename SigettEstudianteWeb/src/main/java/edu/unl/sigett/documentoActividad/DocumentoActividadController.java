/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoActividad;

import com.jlmallas.comun.entity.Documento;
import edu.unl.sigett.actividad.SessionActividad;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTipo("pdf");
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setContents(event.getFile().getContents());
        Long size = event.getFile().getSize();
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTamanio(size.doubleValue());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("uploaded"), "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void remover(DocumentoActividadDTO documentoActividadDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionActividad.getDocumentosActividadDTO().remove(documentoActividadDTO);
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("eliminar"), "");
        if (documentoActividadDTO.getDocumentoActividad().getId() != null) {
           sessionActividad.getDocumentosActividadEliminadosDTO().add(documentoActividadDTO);
        }
    }

    public void agregar(DocumentoActividadDTO documentoActividadDTO) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionActividad.getDocumentosActividadDTO().add(documentoActividadDTO);
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("agregar"), "");
    }
}
