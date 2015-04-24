/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import edu.unl.sigett.entity.CalificacionMiembro;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionCalificacionMiembro")
@SessionScoped
public class SessionCalificacionMiembro implements Serializable {

    private CalificacionMiembro calificacionMiembro;

    public SessionCalificacionMiembro() {
        this.calificacionMiembro = new CalificacionMiembro();
    }

    public CalificacionMiembro getCalificacionMiembro() {
        return calificacionMiembro;
    }

    public void setCalificacionMiembro(CalificacionMiembro calificacionMiembro) {
        this.calificacionMiembro = calificacionMiembro;
    }

}
