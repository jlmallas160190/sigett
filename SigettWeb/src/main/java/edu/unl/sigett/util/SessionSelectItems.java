/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.util;

import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionSelectItems")
@SessionScoped
public class SessionSelectItems implements Serializable {

    private List<OfertaAcademica> ofertaAcademicas;
    private List<OfertaAcademica> filterOfertaAcademicas;
    private List<PeriodoAcademico> periodoAcademicos;

    public SessionSelectItems() {
        this.periodoAcademicos = new ArrayList<>();
        this.ofertaAcademicas = new ArrayList<>();
        this.filterOfertaAcademicas = new ArrayList<>();
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }

    public List<PeriodoAcademico> getPeriodoAcademicos() {
        return periodoAcademicos;
    }

    public void setPeriodoAcademicos(List<PeriodoAcademico> periodoAcademicos) {
        this.periodoAcademicos = periodoAcademicos;
    }

    public List<OfertaAcademica> getFilterOfertaAcademicas() {
        return filterOfertaAcademicas;
    }

    public void setFilterOfertaAcademicas(List<OfertaAcademica> filterOfertaAcademicas) {
        this.filterOfertaAcademicas = filterOfertaAcademicas;
    }

}
