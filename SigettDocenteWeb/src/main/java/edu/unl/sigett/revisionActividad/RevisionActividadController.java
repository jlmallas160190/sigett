/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.revisionActividad;

import edu.unl.sigett.actividad.SessionActividad;
import edu.unl.sigett.entity.Revision;
import edu.unl.sigett.entity.RevisionActividad;
import edu.unl.sigett.service.RevisionActividadService;
import edu.unl.sigett.service.RevisionService;
import edu.unl.sigett.util.CabeceraController;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge-luis
 */
@Named(value = "revisionActividadController")
@SessionScoped
public class RevisionActividadController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionRevisionActividad sessionRevisionActividad;
    @Inject
    private SessionActividad sessionActividad;
    @Inject
    private CabeceraController cabeceraController;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/RevisionActividadServiceImplement!edu.unl.sigett.service.RevisionActividadService")
    private RevisionActividadService revisionActividadService;
    @EJB(lookup = "java:global/SigettService/RevisionServiceImplement!edu.unl.sigett.service.RevisionService")
    private RevisionService revisionService;

    //</editor-fold>
    public RevisionActividadController() {
    }

    public void editar(RevisionActividad revisionActividad) {
        Calendar fechaActual = Calendar.getInstance();
        revisionActividad.getRevision().setFechaInicio(fechaActual.getTime());
        sessionRevisionActividad.setRevisionActividad(revisionActividad);
        sessionRevisionActividad.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudRevisionActividad').show()");
    }

    public void crear() {
        Calendar fechaActual = Calendar.getInstance();
        sessionRevisionActividad.setRevisionActividad(new RevisionActividad(null, sessionActividad.getActividad(), new Revision(null, fechaActual.getTime(), null, null, null, null)));
        sessionRevisionActividad.setRenderedCrud(Boolean.TRUE);
        RequestContext.getCurrentInstance().execute("PF('dlgCrudRevisionActividad').show()");
    }

    public void cancelarEdicion() {
        sessionRevisionActividad.setRenderedCrud(Boolean.FALSE);
        sessionRevisionActividad.setRevisionActividad(new RevisionActividad());
        RequestContext.getCurrentInstance().execute("PF('dlgCrudRevisionActividad').hide()");
    }

    public void remover(RevisionActividad revisionActividad) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionActividad.getRevisionesActividad().remove(revisionActividad);
        cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("eliminar"), "");
        if (revisionActividad.getId() != null) {
            revisionActividadService.eliminar(revisionActividad);
            revisionService.eliminar(revisionActividad.getRevision());
        }
    }

    public void agregar() {
        Calendar fechaActual = Calendar.getInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
        sessionRevisionActividad.getRevisionActividad().getRevision().setFechaFin(fechaActual.getTime());
        if (!sessionActividad.getRevisionesActividad().contains(sessionRevisionActividad.getRevisionActividad())) {
            sessionActividad.getRevisionesActividad().add(sessionRevisionActividad.getRevisionActividad());
            cabeceraController.getMessageView().message(FacesMessage.SEVERITY_INFO, bundle.getString("agregar"), "");
        }
        cancelarEdicion();
    }
}
