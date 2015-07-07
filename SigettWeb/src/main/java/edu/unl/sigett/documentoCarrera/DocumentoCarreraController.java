/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoCarrera;

import com.jlmallas.comun.service.DocumentoService;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.service.DocumentoCarreraService;
import edu.unl.sigett.util.CabeceraController;
import java.io.File;
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
@Named(value = "documentoCarreraController")
@SessionScoped
public class DocumentoCarreraController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private DocumentoCarreraDM documentoCarreraDM;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/ComunService/DocumentoServiceImplement!com.jlmallas.comun.service.DocumentoService")
    private DocumentoService documentoService;

    //</editor-fold>
    public DocumentoCarreraController() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setContents(event.getFile().getContents());
            Long size = event.getFile().getSize();
            documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().setTamanio(size.doubleValue());
            cabeceraController.getUtilService().generaDocumento(new File(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getRuta()),
                    documentoCarreraDM.getDocumentoCarreraDTO().getDocumento().getContents());
            documentoService.actualizar(documentoCarreraDM.getDocumentoCarreraDTO().getDocumento());
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.uploaded"), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        }
    }
}
