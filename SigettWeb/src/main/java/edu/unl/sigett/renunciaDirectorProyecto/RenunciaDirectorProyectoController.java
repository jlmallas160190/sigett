/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.renunciaDirectorProyecto;

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
    private RenunciaDirectorProyectoDM renunciaDirectorProyectoDM;
    @Inject
    private CabeceraController cabeceraController;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB
    private RenunciaDirectorService renunciaDirectorService;
    @EJB
    private RenunciaService renunciaService;
    //</editor-fold>
    private static final Logger LOG = Logger.getLogger(RenunciaDirectorProyectoController.class.getName());

    public RenunciaDirectorProyectoController() {
    }

    public void guardar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        try {
            if (renunciaDirectorProyectoDM.getRenunciaDirector().getId() == null) {
                renunciaService.guardar(renunciaDirectorProyectoDM.getRenunciaDirector().getRenuncia());
                renunciaDirectorProyectoDM.getRenunciaDirector().setRenuncia(renunciaDirectorProyectoDM.getRenunciaDirector().getRenuncia());
                renunciaDirectorService.guardar(renunciaDirectorProyectoDM.getRenunciaDirector());
                cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.msm_grabar"), "");
            }
        } catch (Exception e) {
            LOG.warning(e.getMessage());
        }
    }
}
