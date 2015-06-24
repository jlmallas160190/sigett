/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
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
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private ItemService itemService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(DocumentoProyectoController.class.getName());

    public DocumentoProyectoController() {
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public void crear() {
        listarCategorias();
        sessionDocumentoProyecto.setTamanioArchivo(cabeceraController.getConfiguracionGeneralUtil().getTamanioArchivo());
        sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO(
                new DocumentoProyecto(Boolean.TRUE, null, sessionProyecto.getProyectoSeleccionado()), new Documento()));
        sessionDocumentoProyecto.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoProyecto').show()");
    }

    public void editar(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            sessionDocumentoProyecto.setTamanioArchivo(cabeceraController.getConfiguracionGeneralUtil().getTamanioArchivo());
            listarCategorias();
            sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(documentoProyectoDTO);
            sessionDocumentoProyecto.setRenderedCrud(Boolean.TRUE);
            RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoProyecto').show()");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * SUBIR ARCHIVO
     *
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setTipo("pdf");
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

    /**
     * AGREGAR DOCUMENTO
     *
     */
    public void agregar() {
        Calendar fecha = Calendar.getInstance();
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setCatalogoId(
                    sessionDocumentoProyecto.getCatalogoSeleccionado().getId());
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setCatalogo(
                    sessionDocumentoProyecto.getCatalogoSeleccionado().getNombre());
            sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().setFechaCreacion(fecha.getTime());
            if (!sessionProyecto.getDocumentosProyectosDTOAgregados().contains(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado())) {
                sessionProyecto.getDocumentosProyectosDTOAgregados().add(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado());
            }
            if (!sessionProyecto.getDocumentosProyectoDTO().contains(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado())) {
                sessionProyecto.getDocumentosProyectoDTO().add(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado());
            }
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_agregar"), "");
            cancelarEdicion();
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public void remover(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (documentoProyectoDTO.getDocumentoProyecto().getId() != null) {
                documentoProyectoDTO.getDocumentoProyecto().setEsActivo(Boolean.FALSE);
                sessionProyecto.getDocumentosProyectoDTO().remove(documentoProyectoDTO);
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
                return;
            }
            sessionProyecto.getDocumentosProyectoDTO().remove(documentoProyectoDTO);
            sessionProyecto.getDocumentosProyectosDTOAgregados().remove(documentoProyectoDTO);
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_eliminar"), "");
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * LISTAR LAS CATEGORIAS DE LOS DOCUMENTOS DEL PROYECTO.
     */
    private void listarCategorias() {
        sessionDocumentoProyecto.getCatalogosDocumento().clear();
        sessionDocumentoProyecto.getCatalogosDocumento().addAll(itemService.buscarPorCatalogo(CatalogoEnum.CATALOGODOCUMENTOPROYECTO.getTipo()));
    }

    public void cancelarEdicion() {
        sessionDocumentoProyecto.setRenderedCrud(Boolean.FALSE);
        sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO());
        RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoProyecto').hide()");
    }
    //</editor-fold>
}
