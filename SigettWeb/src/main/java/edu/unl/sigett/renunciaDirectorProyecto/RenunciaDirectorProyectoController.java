/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.renunciaDirectorProyecto;

import com.jlmallas.comun.entity.Item;
import com.jlmallas.comun.enumeration.CatalogoEnum;
import com.jlmallas.comun.service.ItemService;
import edu.unl.sigett.directorProyecto.DirectorProyectoDM;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.entity.Renuncia;
import edu.unl.sigett.entity.RenunciaDirector;
import edu.unl.sigett.enumeration.EstadoDirectorEnum;
import edu.unl.sigett.proyecto.SessionProyecto;
import edu.unl.sigett.renuncia.SessionRenuncia;
import edu.unl.sigett.service.DirectorProyectoService;
import edu.unl.sigett.service.RenunciaDirectorService;
import edu.unl.sigett.service.RenunciaService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "renunciaDirectorProyectoController")
@SessionScoped
public class RenunciaDirectorProyectoController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionRenunciaDirectorProyecto sessionRenunciaDirectorProyecto;
    @Inject
    private SessionProyecto sessionProyecto;
    @Inject
    private DirectorProyectoDM directorProyectoDM;
    @Inject
    private SessionRenuncia sessionRenuncia;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private RenunciaDirectorService renunciaDirectorService;
    @EJB
    private RenunciaService renunciaService;
    @EJB
    private DirectorProyectoService directorProyectoService;
    @EJB
    private ItemService itemService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(RenunciaDirectorProyectoController.class.getName());
    
    public RenunciaDirectorProyectoController() {
    }
    
    public void guardar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            Item estadoDirector = itemService.buscarPorCatalogoCodigo(CatalogoEnum.ESTADODIRECTOR.getTipo(), EstadoDirectorEnum.RENUNCIADO.getTipo());
            if (sessionRenunciaDirectorProyecto.getRenunciaDirector().getId() == null) {
                renunciaService.guardar(sessionRenuncia.getRenuncia());
                sessionRenunciaDirectorProyecto.getRenunciaDirector().setRenuncia(sessionRenuncia.getRenuncia());
                sessionRenunciaDirectorProyecto.getRenunciaDirector().setId(sessionRenuncia.getRenuncia().getId());
                renunciaDirectorService.guardar(sessionRenunciaDirectorProyecto.getRenunciaDirector());
                sessionRenunciaDirectorProyecto.getRenunciaDirector().getDirectorProyectoId().setEstadoDirectorId(estadoDirector.getId());
                directorProyectoService.actualizar(sessionRenunciaDirectorProyecto.getRenunciaDirector().getDirectorProyectoId());
                sessionProyecto.getDirectoresProyectoDTO().remove(directorProyectoDM.getDirectorProyectoDTO());
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
                cancelarEdicion();
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
    
    public void cancelarEdicion() {
        this.sessionRenuncia.setRenuncia(new Renuncia());
        this.sessionRenunciaDirectorProyecto.setRenunciaDirector(new RenunciaDirector());
        this.directorProyectoDM.setDirectorProyectoDTO(new DirectorProyectoDTO());
        this.sessionRenunciaDirectorProyecto.setRenderedCrud(Boolean.FALSE);
    }
}
