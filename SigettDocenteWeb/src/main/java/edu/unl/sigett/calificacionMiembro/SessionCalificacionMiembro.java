/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.calificacionMiembro;

import edu.unl.sigett.entity.CalificacionMiembro;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionCalificacionMiembro")
@SessionScoped
public class SessionCalificacionMiembro implements Serializable {

    private List<CalificacionMiembro> calificacionMiembros;
    private CalificacionMiembro calificacionMiembro;

    public SessionCalificacionMiembro() {
        this.calificacionMiembro = new CalificacionMiembro();
        this.calificacionMiembros = new ArrayList<>();
    }

    public List<CalificacionMiembro> getCalificacionMiembros() {
        return calificacionMiembros;
    }

    public void setCalificacionMiembros(List<CalificacionMiembro> calificacionMiembros) {
        this.calificacionMiembros = calificacionMiembros;
    }

    public CalificacionMiembro getCalificacionMiembro() {
        return calificacionMiembro;
    }

    public void setCalificacionMiembro(CalificacionMiembro calificacionMiembro) {
        this.calificacionMiembro = calificacionMiembro;
    }

}
