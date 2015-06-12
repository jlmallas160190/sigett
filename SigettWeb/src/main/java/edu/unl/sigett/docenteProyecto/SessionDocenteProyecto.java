/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocenteProyecto implements Serializable {

    private DocenteProyectoDTO docenteProyectoDTO;

    private Boolean renderedBuscar;
    private Boolean renderedBuscarEspecialista;
    private Boolean renderedImprimirOficio;
    private Boolean renderedSeleccionarEspecialista;
    private Boolean renderedEliminar;
    private Boolean renderedMediaOficio;
    private Boolean renderedMediaFePresentacion;
    private Boolean renderedPnlDocentesDisponibles;

    public SessionDocenteProyecto() {
        this.docenteProyectoDTO = new DocenteProyectoDTO();
    }

    public DocenteProyectoDTO getDocenteProyectoDTO() {
        return docenteProyectoDTO;
    }

    public void setDocenteProyectoDTO(DocenteProyectoDTO docenteProyectoDTO) {
        this.docenteProyectoDTO = docenteProyectoDTO;
    }

    public Boolean getRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(Boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public Boolean getRenderedBuscarEspecialista() {
        return renderedBuscarEspecialista;
    }

    public void setRenderedBuscarEspecialista(Boolean renderedBuscarEspecialista) {
        this.renderedBuscarEspecialista = renderedBuscarEspecialista;
    }

    public Boolean getRenderedImprimirOficio() {
        return renderedImprimirOficio;
    }

    public void setRenderedImprimirOficio(Boolean renderedImprimirOficio) {
        this.renderedImprimirOficio = renderedImprimirOficio;
    }

    public Boolean getRenderedSeleccionarEspecialista() {
        return renderedSeleccionarEspecialista;
    }

    public void setRenderedSeleccionarEspecialista(Boolean renderedSeleccionarEspecialista) {
        this.renderedSeleccionarEspecialista = renderedSeleccionarEspecialista;
    }

    public Boolean getRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(Boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public Boolean getRenderedMediaOficio() {
        return renderedMediaOficio;
    }

    public void setRenderedMediaOficio(Boolean renderedMediaOficio) {
        this.renderedMediaOficio = renderedMediaOficio;
    }

    public Boolean getRenderedMediaFePresentacion() {
        return renderedMediaFePresentacion;
    }

    public void setRenderedMediaFePresentacion(Boolean renderedMediaFePresentacion) {
        this.renderedMediaFePresentacion = renderedMediaFePresentacion;
    }

    public Boolean getRenderedPnlDocentesDisponibles() {
        return renderedPnlDocentesDisponibles;
    }

    public void setRenderedPnlDocentesDisponibles(Boolean renderedPnlDocentesDisponibles) {
        this.renderedPnlDocentesDisponibles = renderedPnlDocentesDisponibles;
    }

}
