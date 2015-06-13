/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.prorroga;

import edu.unl.sigett.entity.Prorroga;
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
public class SessionProrroga implements Serializable {

    private Prorroga prorroga;

    private List<Prorroga> prorrogas;
    private List<Prorroga> filterProrrogas;

    private Boolean renderedCrud;
    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;
    private Boolean renderedBuscar;
    private Boolean renderedMediaOficio;
    private Boolean renderedMediaOficioInf;

    public SessionProrroga() {
        this.prorrogas = new ArrayList<>();
        this.filterProrrogas = new ArrayList<>();
        this.prorroga = new Prorroga();
    }

    public Prorroga getProrroga() {
        return prorroga;
    }

    public void setProrroga(Prorroga prorroga) {
        this.prorroga = prorroga;
    }

    public List<Prorroga> getProrrogas() {
        return prorrogas;
    }

    public void setProrrogas(List<Prorroga> prorrogas) {
        this.prorrogas = prorrogas;
    }

    public List<Prorroga> getFilterProrrogas() {
        return filterProrrogas;
    }

    public void setFilterProrrogas(List<Prorroga> filterProrrogas) {
        this.filterProrrogas = filterProrrogas;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
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

    public Boolean getRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(Boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public Boolean getRenderedMediaOficio() {
        return renderedMediaOficio;
    }

    public void setRenderedMediaOficio(Boolean renderedMediaOficio) {
        this.renderedMediaOficio = renderedMediaOficio;
    }

    public Boolean getRenderedMediaOficioInf() {
        return renderedMediaOficioInf;
    }

    public void setRenderedMediaOficioInf(Boolean renderedMediaOficioInf) {
        this.renderedMediaOficioInf = renderedMediaOficioInf;
    }

}
