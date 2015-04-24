/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import edu.unl.sigett.entity.EvaluacionTribunal;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEvaluacionTribunal implements Serializable {

    private EvaluacionTribunal evaluacionTribunal;

    public SessionEvaluacionTribunal() {
        this.evaluacionTribunal = new EvaluacionTribunal();
    }

    public EvaluacionTribunal getEvaluacionTribunal() {
        return evaluacionTribunal;
    }

    public void setEvaluacionTribunal(EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunal = evaluacionTribunal;
    }

}
