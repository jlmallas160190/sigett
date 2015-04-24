/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionPeriodoCoordinacion implements Serializable {

    private PeriodoCoordinacion periodoCoordinacion;

    public SessionPeriodoCoordinacion() {
        this.periodoCoordinacion = new PeriodoCoordinacion();
    }

    public PeriodoCoordinacion getPeriodoCoordinacion() {
        return periodoCoordinacion;
    }

    public void setPeriodoCoordinacion(PeriodoCoordinacion periodoCoordinacion) {
        this.periodoCoordinacion = periodoCoordinacion;
    }

}
