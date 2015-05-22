/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private List<PeriodoCoordinacion> periodosCoordinacion;
    private List<PeriodoCoordinacion> filterPeriodosCoordinacion;
    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;

    public SessionPeriodoCoordinacion() {
        this.filterPeriodosCoordinacion=new ArrayList<>();
        this.periodosCoordinacion = new ArrayList<>();
        this.periodoCoordinacion = new PeriodoCoordinacion();
    }

    public PeriodoCoordinacion getPeriodoCoordinacion() {
        return periodoCoordinacion;
    }

    public void setPeriodoCoordinacion(PeriodoCoordinacion periodoCoordinacion) {
        this.periodoCoordinacion = periodoCoordinacion;
    }

    public List<PeriodoCoordinacion> getPeriodosCoordinacion() {
        return periodosCoordinacion;
    }

    public void setPeriodosCoordinacion(List<PeriodoCoordinacion> periodosCoordinacion) {
        this.periodosCoordinacion = periodosCoordinacion;
    }

    public Boolean getRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(Boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public Boolean getRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(Boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public Boolean getRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(Boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public List<PeriodoCoordinacion> getFilterPeriodosCoordinacion() {
        return filterPeriodosCoordinacion;
    }

    public void setFilterPeriodosCoordinacion(List<PeriodoCoordinacion> filterPeriodosCoordinacion) {
        this.filterPeriodosCoordinacion = filterPeriodosCoordinacion;
    }

}
