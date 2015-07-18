/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.tribunal;

import edu.unl.sigett.entity.Tribunal;
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
public class SessionTribunal implements Serializable {

    private Tribunal tribunal;
    private List<Tribunal> tribunales;
    private List<Tribunal> filterTribunales;

    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;
    private Boolean renderedBuscar;
    private Boolean renderedCrud;

    public SessionTribunal() {
        renderedCrud=Boolean.FALSE;
        this.filterTribunales = new ArrayList<>();
        this.tribunales = new ArrayList<>();
        this.tribunal = new Tribunal();
    }

    public Tribunal getTribunal() {
        return tribunal;
    }

    public void setTribunal(Tribunal tribunal) {
        this.tribunal = tribunal;
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

    public List<Tribunal> getTribunales() {
        return tribunales;
    }

    public void setTribunales(List<Tribunal> tribunales) {
        this.tribunales = tribunales;
    }

    public List<Tribunal> getFilterTribunales() {
        return filterTribunales;
    }

    public void setFilterTribunales(List<Tribunal> filterTribunales) {
        this.filterTribunales = filterTribunales;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
    }

}
