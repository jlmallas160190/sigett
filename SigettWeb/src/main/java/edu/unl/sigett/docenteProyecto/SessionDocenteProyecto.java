/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import edu.unl.sigett.entity.DocumentoCarrera;
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
public class SessionDocenteProyecto implements Serializable {

    private DocenteProyectoDTO docenteProyectoDTO;
    private DocumentoCarrera documentoCarrera;

    private List<DirectorDTO> directoresDTO;
    private List<DirectorDTO> filterDirectoresDTO;

    private Boolean renderedBuscar;
    private Boolean renderedBuscarEspecialista;
    private Boolean renderedImprimiOficio;
    private Boolean renderedSeleccionarEspecialista;
    private Boolean renderedEliminar;
    private Boolean renderedDialogoOficio;
    
    private Long docenteProyectoId;
    private Integer carreraId;

    public SessionDocenteProyecto() {
        this.filterDirectoresDTO=new ArrayList<>();
        this.directoresDTO = new ArrayList<>();
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

    public Boolean getRenderedImprimiOficio() {
        return renderedImprimiOficio;
    }

    public void setRenderedImprimiOficio(Boolean renderedImprimiOficio) {
        this.renderedImprimiOficio = renderedImprimiOficio;
    }

    public Boolean getRenderedSeleccionarEspecialista() {
        return renderedSeleccionarEspecialista;
    }

    public void setRenderedSeleccionarEspecialista(Boolean renderedSeleccionarEspecialista) {
        this.renderedSeleccionarEspecialista = renderedSeleccionarEspecialista;
    }

    public Boolean isRenderedEliminar() {
        return renderedEliminar;
    }

    public void setRenderedEliminar(Boolean renderedEliminar) {
        this.renderedEliminar = renderedEliminar;
    }

    public List<DirectorDTO> getDirectoresDTO() {
        return directoresDTO;
    }

    public void setDirectoresDTO(List<DirectorDTO> directoresDTO) {
        this.directoresDTO = directoresDTO;
    }

    public List<DirectorDTO> getFilterDirectoresDTO() {
        return filterDirectoresDTO;
    }

    public void setFilterDirectoresDTO(List<DirectorDTO> filterDirectoresDTO) {
        this.filterDirectoresDTO = filterDirectoresDTO;
    }

    public Long getDocenteProyectoId() {
        return docenteProyectoId;
    }

    public void setDocenteProyectoId(Long docenteProyectoId) {
        this.docenteProyectoId = docenteProyectoId;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

 
    public Boolean getRenderedDialogoOficio() {
        return renderedDialogoOficio;
    }

    public void setRenderedDialogoOficio(Boolean renderedDialogoOficio) {
        this.renderedDialogoOficio = renderedDialogoOficio;
    }

    public DocumentoCarrera getDocumentoCarrera() {
        return documentoCarrera;
    }

    public void setDocumentoCarrera(DocumentoCarrera documentoCarrera) {
        this.documentoCarrera = documentoCarrera;
    }

}
