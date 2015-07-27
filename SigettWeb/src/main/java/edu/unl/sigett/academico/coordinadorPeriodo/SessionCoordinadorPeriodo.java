/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.coordinadorPeriodo;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import edu.unl.sigett.academico.docenteCarrera.DocenteCarreraDTO;
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
public class SessionCoordinadorPeriodo implements Serializable {

    private CoordinadorPeriodoDTO coordinadorPeriodoDTO;
    private List<CoordinadorPeriodoDTO> coordinadorPeriodos;
    private List<CoordinadorPeriodoDTO> filterCoordinadorPeriodos;
    private DocenteCarreraDTO docenteCarreraDTOSeleccionado;
    private List<PeriodoCoordinacion> periodosCoordinacion;
    private String numeroIdentificacion;
    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;
    private String periodoCoordinacion;

    public SessionCoordinadorPeriodo() {
        this.docenteCarreraDTOSeleccionado = new DocenteCarreraDTO();
        this.filterCoordinadorPeriodos = new ArrayList<>();
        this.periodosCoordinacion = new ArrayList<>();
        this.coordinadorPeriodos = new ArrayList<>();
        this.coordinadorPeriodoDTO = new CoordinadorPeriodoDTO();
    }

    public CoordinadorPeriodoDTO getCoordinadorPeriodoDTO() {
        return coordinadorPeriodoDTO;
    }

    public void setCoordinadorPeriodoDTO(CoordinadorPeriodoDTO coordinadorPeriodoDTO) {
        this.coordinadorPeriodoDTO = coordinadorPeriodoDTO;
    }

    public List<CoordinadorPeriodoDTO> getCoordinadorPeriodos() {
        return coordinadorPeriodos;
    }

    public void setCoordinadorPeriodos(List<CoordinadorPeriodoDTO> coordinadorPeriodos) {
        this.coordinadorPeriodos = coordinadorPeriodos;
    }

    public DocenteCarreraDTO getDocenteCarreraDTOSeleccionado() {
        return docenteCarreraDTOSeleccionado;
    }

    public void setDocenteCarreraDTOSeleccionado(DocenteCarreraDTO docenteCarreraDTOSeleccionado) {
        this.docenteCarreraDTOSeleccionado = docenteCarreraDTOSeleccionado;
    }

    public List<PeriodoCoordinacion> getPeriodosCoordinacion() {
        return periodosCoordinacion;
    }

    public void setPeriodosCoordinacion(List<PeriodoCoordinacion> periodosCoordinacion) {
        this.periodosCoordinacion = periodosCoordinacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
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

    public List<CoordinadorPeriodoDTO> getFilterCoordinadorPeriodos() {
        return filterCoordinadorPeriodos;
    }

    public void setFilterCoordinadorPeriodos(List<CoordinadorPeriodoDTO> filterCoordinadorPeriodos) {
        this.filterCoordinadorPeriodos = filterCoordinadorPeriodos;
    }

    public String getPeriodoCoordinacion() {
        return periodoCoordinacion;
    }

    public void setPeriodoCoordinacion(String periodoCoordinacion) {
        this.periodoCoordinacion = periodoCoordinacion;
    }

}
