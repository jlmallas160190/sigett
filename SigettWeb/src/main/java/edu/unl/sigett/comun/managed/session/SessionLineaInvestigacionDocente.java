/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionLineaInvestigacionDocente implements Serializable {

    private LineaInvestigacionDocente lineaInvestigacionDocente;

    public SessionLineaInvestigacionDocente() {
        this.lineaInvestigacionDocente = new LineaInvestigacionDocente();
    }

    public LineaInvestigacionDocente getLineaInvestigacionDocente() {
        return lineaInvestigacionDocente;
    }

    public void setLineaInvestigacionDocente(LineaInvestigacionDocente lineaInvestigacionDocente) {
        this.lineaInvestigacionDocente = lineaInvestigacionDocente;
    }

}
