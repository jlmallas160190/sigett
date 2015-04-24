/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionLineaInvestigacionCarrera implements Serializable {

    private LineaInvestigacionCarrera lineaInvestigacionCarrera;

    public SessionLineaInvestigacionCarrera() {
        this.lineaInvestigacionCarrera = new LineaInvestigacionCarrera();
    }

    public LineaInvestigacionCarrera getLineaInvestigacionCarrera() {
        return lineaInvestigacionCarrera;
    }

    public void setLineaInvestigacionCarrera(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        this.lineaInvestigacionCarrera = lineaInvestigacionCarrera;
    }

}
