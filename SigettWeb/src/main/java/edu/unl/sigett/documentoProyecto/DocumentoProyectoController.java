/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.dao.ConfiguracionDao;
import com.jlmallas.comun.entity.Configuracion;
import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.enumeration.ConfiguracionEnum;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jlmallas.seguridad.entity.Usuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jorge-luis
 */
@Named(value = "documentoProyectoController")
@SessionScoped
public class DocumentoProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionDocumentoProyecto sessionDocumentoProyecto;
    @Inject
    private SessionProyecto sessionProyecto;
    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="SERVICIOS">

    //</editor-fold>
    private CabeceraController cabeceraController;
    
    public DocumentoProyectoController() {
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public String crear() {
        sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO(
                new DocumentoProyecto(Boolean.TRUE, null, sessionProyecto.getProyectoSeleccionado()), new Documento()));
        RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').show()");
        return "";
    }
    
    public void editar(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(documentoProyectoDTO);
            RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').show()");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            String tipoArchivo = event.getFile().getContentType();
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setTipo(tipoArchivo);
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setContents(event.getFile().getContents());
            Long size = event.getFile().getSize();
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setTamanio(size.doubleValue());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.uploaded"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
    }
    
    public void agregarDocumentoProyecto(final DocumentoProyectoDTO documentoProyectoDTO) {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setCatalogoId(
                    sessionDocumentoProyecto.getCatalogoSeleccionado().getId());
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setFechaCreacion(fecha.getTime());
            if (!sessionProyecto.getDocumentosProyectosDTOAgregados().contains(documentoProyectoDTO)) {
                sessionProyecto.getDocumentosProyectosDTOAgregados().add(documentoProyectoDTO);
            }
            if (!sessionProyecto.getDocumentosProyectoDTO().contains(documentoProyectoDTO)) {
                sessionProyecto.getDocumentosProyectoDTO().add(documentoProyectoDTO);
                sessionProyecto.getFilterDocumentosProyectoDTO().add(documentoProyectoDTO);
            }
            RequestContext.getCurrentInstance().execute("PF('dlgEditarDocumentoProyecto').hide()");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void remover(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            documentoProyectoDTO.getDocumentoProyecto().setEsActivo(Boolean.FALSE);
            sessionProyecto.getDocumentosProyectoDTO().remove(documentoProyectoDTO);
            sessionProyecto.getFilterDocumentosProyectoDTO().remove(documentoProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_ERROR, bundle.getString("lbl.msm_permiso_denegado_eliminar") + ". "
                    + bundle.getString("lbl.msm_consulte"), "");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
