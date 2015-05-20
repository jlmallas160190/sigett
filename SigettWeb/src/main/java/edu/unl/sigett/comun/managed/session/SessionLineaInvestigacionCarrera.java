/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import edu.unl.sigett.entity.LineaInvestigacionCarrera;
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
public class SessionLineaInvestigacionCarrera implements Serializable {

    private LineaInvestigacionCarrera lineaInvestigacionCarrera;
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreras;
    private List<LineaInvestigacionCarrera> filterLineasInvestigacionCarreras;
    private boolean renderedEditar;
    private boolean renderedBuscar;
    private boolean renderedCrear;
    private boolean renderedEliminar;

    public SessionLineaInvestigacionCarrera() {
        this.filterLineasInvestigacionCarreras = new ArrayList<>();
        this.lineaInvestigacionCarreras = new ArrayList<>();
        this.lineaInvestigacionCarrera = new LineaInvestigacionCarrera();
    }

    public LineaInvestigacionCarrera getLineaInvestigacionCarrera() {
        return lineaInvestigacionCarrera;
    }

    public void setLineaInvestigacionCarrera(LineaInvestigacionCarrera lineaInvestigacionCarrera) {
        this.lineaInvestigacionCarrera = lineaInvestigacionCarrera;
    }

    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreras() {
        return lineaInvestigacionCarreras;
    }

    public void setLineaInvestigacionCarreras(List<LineaInvestigacionCarrera> lineaInvestigacionCarreras) {
        this.lineaInvestigacionCarreras = lineaInvestigacionCarreras;
    }

    public boolean isRenderedEditar() {
        return renderedEditar;
    }

    public void setRenderedEditar(boolean renderedEditar) {
        this.renderedEditar = renderedEditar;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public boolean isRenderedCrear() {
        return renderedCrear;
    }

    public void setRenderedCrear(boolean renderedCrear) {
        this.renderedCrear = renderedCrear;
    }

    public boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public List<LineaInvestigacionCarrera> getFilterLineasInvestigacionCarreras() {
        return filterLineasInvestigacionCarreras;
    }

    public void setFilterLineasInvestigacionCarreras(List<LineaInvestigacionCarrera> filterLineasInvestigacionCarreras) {
        this.filterLineasInvestigacionCarreras = filterLineasInvestigacionCarreras;
    }

}
