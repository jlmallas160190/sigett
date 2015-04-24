/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.TemaProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionTemaProyecto implements Serializable {

    private TemaProyecto temaProyecto;

    public SessionTemaProyecto() {
        this.temaProyecto = new TemaProyecto();
    }

    public TemaProyecto getTemaProyecto() {
        return temaProyecto;
    }

    public void setTemaProyecto(TemaProyecto temaProyecto) {
        this.temaProyecto = temaProyecto;
    }

}
