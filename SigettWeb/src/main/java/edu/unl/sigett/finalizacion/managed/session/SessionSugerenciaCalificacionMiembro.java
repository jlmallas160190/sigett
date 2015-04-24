/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import edu.unl.sigett.entity.SugerenciaCalificacionMiembro;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionSugerenciaCalificacionMiembro")
@SessionScoped
public class SessionSugerenciaCalificacionMiembro implements Serializable {

    private SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro;

    public SessionSugerenciaCalificacionMiembro() {
        this.sugerenciaCalificacionMiembro = new SugerenciaCalificacionMiembro();
    }

    public SugerenciaCalificacionMiembro getSugerenciaCalificacionMiembro() {
        return sugerenciaCalificacionMiembro;
    }

    public void setSugerenciaCalificacionMiembro(SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro) {
        this.sugerenciaCalificacionMiembro = sugerenciaCalificacionMiembro;
    }

}
