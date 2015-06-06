/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.unl.sigett.academico.coordinadorPeriodo.CoordinadorPeriodoDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "docenteProyectoDM")
@SessionScoped
public class DocenteProyectoDM implements Serializable {

    private DocenteProyectoDTO docenteProyectoDTOSeleccionado;
    private Item estadoActualProyecto;
    private CoordinadorPeriodoDTO coordinadorPeriodoDTO;
    private List<DocenteProyectoDTO> docentesProyectoDTO;
    private List<DocenteProyectoDTO> filterDocentesProyectoDTO;
    private List<DocenteProyectoDTO> historialDocenteProyectosDTO;
    private List<DocenteProyectoDTO> filterHistorialDocenteProyectosDTO;

    public DocenteProyectoDM() {
        this.filterHistorialDocenteProyectosDTO = new ArrayList<>();
        this.historialDocenteProyectosDTO = new ArrayList<>();
        this.estadoActualProyecto = new Item();
        this.docenteProyectoDTOSeleccionado = new DocenteProyectoDTO();
        this.filterDocentesProyectoDTO = new ArrayList<>();
        this.docentesProyectoDTO = new ArrayList<>();
    }

    public DocenteProyectoDTO getDocenteProyectoDTOSeleccionado() {
        return docenteProyectoDTOSeleccionado;
    }

    public void setDocenteProyectoDTOSeleccionado(DocenteProyectoDTO docenteProyectoDTOSeleccionado) {
        this.docenteProyectoDTOSeleccionado = docenteProyectoDTOSeleccionado;
    }

    public List<DocenteProyectoDTO> getDocentesProyectoDTO() {
        return docentesProyectoDTO;
    }

    public void setDocentesProyectoDTO(List<DocenteProyectoDTO> docentesProyectoDTO) {
        this.docentesProyectoDTO = docentesProyectoDTO;
    }

    public List<DocenteProyectoDTO> getFilterDocentesProyectoDTO() {
        return filterDocentesProyectoDTO;
    }

    public void setFilterDocentesProyectoDTO(List<DocenteProyectoDTO> filterDocentesProyectoDTO) {
        this.filterDocentesProyectoDTO = filterDocentesProyectoDTO;
    }

    public List<DocenteProyectoDTO> getHistorialDocenteProyectosDTO() {
        return historialDocenteProyectosDTO;
    }

    public void setHistorialDocenteProyectosDTO(List<DocenteProyectoDTO> historialDocenteProyectosDTO) {
        this.historialDocenteProyectosDTO = historialDocenteProyectosDTO;
    }

    public List<DocenteProyectoDTO> getFilterHistorialDocenteProyectosDTO() {
        return filterHistorialDocenteProyectosDTO;
    }

    public void setFilterHistorialDocenteProyectosDTO(List<DocenteProyectoDTO> filterHistorialDocenteProyectosDTO) {
        this.filterHistorialDocenteProyectosDTO = filterHistorialDocenteProyectosDTO;
    }

    public Item getEstadoActualProyecto() {
        return estadoActualProyecto;
    }

    public void setEstadoActualProyecto(Item estadoActualProyecto) {
        this.estadoActualProyecto = estadoActualProyecto;
    }

    public CoordinadorPeriodoDTO getCoordinadorPeriodoDTO() {
        return coordinadorPeriodoDTO;
    }

    public void setCoordinadorPeriodoDTO(CoordinadorPeriodoDTO coordinadorPeriodoDTO) {
        this.coordinadorPeriodoDTO = coordinadorPeriodoDTO;
    }

}
