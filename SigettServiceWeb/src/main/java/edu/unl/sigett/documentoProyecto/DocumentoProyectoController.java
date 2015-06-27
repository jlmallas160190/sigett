/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.documentoProyecto;

import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

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
    public void editar(DocumentoProyectoDTO documentoProyectoDTO) {
        try {
            sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(documentoProyectoDTO);
            sessionDocumentoProyecto.setRenderedCrud(Boolean.TRUE);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
   
    public void cancelarEdicion() {
        sessionDocumentoProyecto.setRenderedCrud(Boolean.FALSE);
        sessionDocumentoProyecto.setDocumentoProyectoDTOSeleccionado(new DocumentoProyectoDTO());
    }
    //</editor-fold>
}
