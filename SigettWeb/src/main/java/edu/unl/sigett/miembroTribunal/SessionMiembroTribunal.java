/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.Item;
import edu.unl.sigett.director.DirectorDTO;
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
public class SessionMiembroTribunal implements Serializable {

    private MiembroTribunalDTO miembroTribunalDTOSeleccionado;
    private DirectorDTO directorDTOSeleccionado;
    private Item cargoSeleccionado;

    private List<MiembroTribunalDTO> miembrosTribunalDTO;
    private List<MiembroTribunalDTO> filterMiembrosTribunalDTO;
    private List<DirectorDTO> directoresDTO;
    private List<DirectorDTO> directoresDTOAux;
    private List<DirectorDTO> filterDirectoresDTO;
    private List<Item> cargos;

    private Boolean renderedCrear;
    private Boolean renderedEditar;
    private Boolean renderedEliminar;
    private Boolean renderedDlgCrud;
    private Boolean renderedDlgDocentesDisponibles;

    public SessionMiembroTribunal() {
        this.filterDirectoresDTO = new ArrayList<>();
        this.cargoSeleccionado = new Item();
        this.cargos = new ArrayList<>();
        this.miembrosTribunalDTO = new ArrayList<>();
        this.filterMiembrosTribunalDTO = new ArrayList<>();
        this.miembroTribunalDTOSeleccionado = new MiembroTribunalDTO();
        this.directoresDTO = new ArrayList<>();
        this.directoresDTOAux = new ArrayList<>();
    }

    public MiembroTribunalDTO getMiembroTribunalDTOSeleccionado() {
        return miembroTribunalDTOSeleccionado;
    }

    public void setMiembroTribunalDTOSeleccionado(MiembroTribunalDTO miembroTribunalDTOSeleccionado) {
        this.miembroTribunalDTOSeleccionado = miembroTribunalDTOSeleccionado;
    }

    public List<DirectorDTO> getDirectoresDTO() {
        return directoresDTO;
    }

    public void setDirectoresDTO(List<DirectorDTO> directoresDTO) {
        this.directoresDTO = directoresDTO;
    }

    public List<DirectorDTO> getDirectoresDTOAux() {
        return directoresDTOAux;
    }

    public void setDirectoresDTOAux(List<DirectorDTO> directoresDTOAux) {
        this.directoresDTOAux = directoresDTOAux;
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

    public Boolean getRenderedDlgCrud() {
        return renderedDlgCrud;
    }

    public void setRenderedDlgCrud(Boolean renderedDlgCrud) {
        this.renderedDlgCrud = renderedDlgCrud;
    }

    public Boolean getRenderedDlgDocentesDisponibles() {
        return renderedDlgDocentesDisponibles;
    }

    public void setRenderedDlgDocentesDisponibles(Boolean renderedDlgDocentesDisponibles) {
        this.renderedDlgDocentesDisponibles = renderedDlgDocentesDisponibles;
    }

    public List<MiembroTribunalDTO> getMiembrosTribunalDTO() {
        return miembrosTribunalDTO;
    }

    public void setMiembrosTribunalDTO(List<MiembroTribunalDTO> miembrosTribunalDTO) {
        this.miembrosTribunalDTO = miembrosTribunalDTO;
    }

    public List<MiembroTribunalDTO> getFilterMiembrosTribunalDTO() {
        return filterMiembrosTribunalDTO;
    }

    public void setFilterMiembrosTribunalDTO(List<MiembroTribunalDTO> filterMiembrosTribunalDTO) {
        this.filterMiembrosTribunalDTO = filterMiembrosTribunalDTO;
    }

    public List<Item> getCargos() {
        return cargos;
    }

    public void setCargos(List<Item> cargos) {
        this.cargos = cargos;
    }

    public DirectorDTO getDirectorDTOSeleccionado() {
        return directorDTOSeleccionado;
    }

    public void setDirectorDTOSeleccionado(DirectorDTO directorDTOSeleccionado) {
        this.directorDTOSeleccionado = directorDTOSeleccionado;
    }

    public Item getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Item cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public List<DirectorDTO> getFilterDirectoresDTO() {
        return filterDirectoresDTO;
    }

    public void setFilterDirectoresDTO(List<DirectorDTO> filterDirectoresDTO) {
        this.filterDirectoresDTO = filterDirectoresDTO;
    }

}
