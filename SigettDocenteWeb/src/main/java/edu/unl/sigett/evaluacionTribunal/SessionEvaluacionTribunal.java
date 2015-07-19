/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.evaluacionTribunal;

import edu.unl.sigett.entity.EvaluacionTribunal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionEvaluacionTribunal")
@SessionScoped
public class SessionEvaluacionTribunal implements Serializable {

    private List<EvaluacionTribunal> evaluacionesTribunal;
    private List<EvaluacionTribunal> filterTribunal;

    private EvaluacionTribunal evaluacionTribunal;

    public SessionEvaluacionTribunal() {
        this.evaluacionTribunal = new EvaluacionTribunal();
        this.filterTribunal = new ArrayList<>();
        this.evaluacionesTribunal = new ArrayList<>();
    }

    public List<EvaluacionTribunal> getEvaluacionesTribunal() {
        return evaluacionesTribunal;
    }

    public void setEvaluacionesTribunal(List<EvaluacionTribunal> evaluacionesTribunal) {
        this.evaluacionesTribunal = evaluacionesTribunal;
    }

    public List<EvaluacionTribunal> getFilterTribunal() {
        return filterTribunal;
    }

    public void setFilterTribunal(List<EvaluacionTribunal> filterTribunal) {
        this.filterTribunal = filterTribunal;
    }

    public EvaluacionTribunal getEvaluacionTribunal() {
        return evaluacionTribunal;
    }

    public void setEvaluacionTribunal(EvaluacionTribunal evaluacionTribunal) {
        this.evaluacionTribunal = evaluacionTribunal;
    }

}
