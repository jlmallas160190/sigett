/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.dao.OficioCarreraFacadeLocal;
import edu.unl.sigett.comun.managed.session.SessionOficioCarrera;
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
@Named(value = "administrarOficiosCarrera")
@SessionScoped
public class AdministrarOficiosCarrera implements Serializable {

    @Inject
    private SessionOficioCarrera sessionOficioCarrera;
    @EJB
    private OficioCarreraFacadeLocal oficioCarreraFacadeLocal;

    public AdministrarOficiosCarrera() {
    }

    public void grabar(OficioCarrera oficioCarrera) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            if (oficioCarrera.getId() == null) {
                oficioCarrera.setEsActivo(true);
                oficioCarreraFacadeLocal.create(oficioCarrera);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.oficio") + " " + bundle.getString("lbl.msm_grabar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                oficioCarreraFacadeLocal.edit(oficioCarrera);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, bundle.getString("lbl.oficio") + " " + bundle.getString("lbl.msm_editar"), "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception e) {
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (sessionOficioCarrera.getOficioCarrera().getId() != null) {
                sessionOficioCarrera.getOficioCarrera().setOficio(event.getFile().getContents());
                grabar(sessionOficioCarrera.getOficioCarrera());
            }
        } catch (Exception e) {
        }
    }
}
