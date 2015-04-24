/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import edu.unl.sigett.entity.PlazoEvaluacionTribunal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionPlazoEvaluacionTribunal")
@SessionScoped
public class SessionPlazoEvaluacionTribunal implements Serializable {

    private PlazoEvaluacionTribunal plazoEvaluacionTribunal;

    public SessionPlazoEvaluacionTribunal() {
        this.plazoEvaluacionTribunal = new PlazoEvaluacionTribunal();
    }

    public PlazoEvaluacionTribunal getPlazoEvaluacionTribunal() {
        return plazoEvaluacionTribunal;
    }

    public void setPlazoEvaluacionTribunal(PlazoEvaluacionTribunal plazoEvaluacionTribunal) {
        this.plazoEvaluacionTribunal = plazoEvaluacionTribunal;
    }

}
