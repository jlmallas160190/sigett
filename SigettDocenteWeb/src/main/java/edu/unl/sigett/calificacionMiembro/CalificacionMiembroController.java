/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.calificacionMiembro;

import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.evaluacionTribunal.SessionEvaluacionTribunal;
import edu.unl.sigett.service.CalificacionMiembroService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author jorge-luis
 */
@Named(value = "calificacionMiembroController")
@SessionScoped
public class CalificacionMiembroController implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private SessionCalificacionMiembro sessionCalificacionMiembro;
    @Inject
    private SessionEvaluacionTribunal sessionEvaluacionTribunal;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SERVICIOS">
    @EJB(lookup = "java:global/SigettService/CalificacionMiembroServiceImplement!edu.unl.sigett.service.CalificacionMiembroService")
    private CalificacionMiembroService calificacionMiembroService;
//</editor-fold>

    public CalificacionMiembroController() {
    }

    public void preRenderView() {
        buscar();
    }

    private void buscar() {
        sessionCalificacionMiembro.getCalificacionMiembros().clear();
        CalificacionMiembro calificacionMiembroBuscar = new CalificacionMiembro();
        calificacionMiembroBuscar.setEvaluacionTribunalId(sessionEvaluacionTribunal.getEvaluacionTribunal());
        sessionCalificacionMiembro.getCalificacionMiembros().addAll(calificacionMiembroService.buscar(calificacionMiembroBuscar));
    }
}
