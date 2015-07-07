/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoActividad;

import com.jlmallas.comun.entity.Documento;
import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.actividad.SessionActividad;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.enumeration.CatalogoDocumentoActividad;
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
import org.primefaces.context.RequestContext;
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
    @EJB(lookup = "java:global/SigettService/DocumentoActividadServiceImplement!edu.unl.sigett.service.DocumentoActividadService")
    private DocumentoActividadService documentoActividadService;
    @EJB(lookup = "java:global/ComunService/DocumentoServiceImplement!com.jlmallas.comun.service.DocumentoService")
    private DocumentoService documentoService;
    @EJB(lookup = "java:global/ComunService/ItemServiceImplement!com.jlmallas.comun.service.ItemService")
    private ItemService itemService;

    public DocumentoActividadController() {
    }

    public void crear() {
        sessionDocumentoActividad.setDocumentoActividadDTO(new DocumentoActividadDTO(new Documento(),
                new DocumentoActividad(null, Boolean.TRUE, null, sessionActividad.getActividad())));
        sessionDocumentoActividad.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoActividad').show()");
    }

    public void editar(DocumentoActividadDTO documentoActividadDTO) {
        sessionDocumentoActividad.setDocumentoActividadDTO(documentoActividadDTO);
        sessionDocumentoActividad.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoActividad').show()");
    }

    public void handleFileUpload(FileUploadEvent event) {
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTipo("pdf");
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setContents(event.getFile().getContents());
        Long size = event.getFile().getSize();
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setTamanio(size.doubleValue());
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

    public void cancelarEdicion() {
        sessionDocumentoActividad.setRenderedCrud(Boolean.FALSE);
        sessionDocumentoActividad.setDocumentoActividadDTO(new DocumentoActividadDTO());
        RequestContext.getCurrentInstance().execute("PF('dlgCrudDocumentoActividad').hide()");
    }

    public void agregar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        Item catalogo = itemService.buscarPorCatalogoCodigo(CatalogoEnum.CATALOGODOCUMENTOCTIVIDAD.getTipo(), CatalogoDocumentoActividad.TAREA.getTipo());
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setCatalogoId(catalogo.getId());
        sessionDocumentoActividad.getDocumentoActividadDTO().getDocumento().setCatalogo(catalogo.getNombre());
        if (!sessionActividad.getDocumentosActividadDTO().contains(sessionDocumentoActividad.getDocumentoActividadDTO())) {
            sessionActividad.getDocumentosActividadDTO().add(sessionDocumentoActividad.getDocumentoActividadDTO());
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("agregar"), "");
        }
        cancelarEdicion();
    }
}
