/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import edu.unl.sigett.util.DocumentoCarreraDTO;
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
    private DocumentoCarreraDTO oficioPertinenciaDTO;

    private List<DirectorDTO> directoresDTO;
    private List<DirectorDTO> filterDirectoresDTO;

    private Boolean renderedBuscar;
    private Boolean renderedBuscarEspecialista;
    private Boolean renderedImprimirOficio;
    private Boolean renderedSeleccionarEspecialista;
    private Boolean renderedEliminar;
    private Boolean renderedPnlDialogoOficio;
    private Boolean renderedPnlDialogoFe;

    private Long docenteProyectoId;

    public SessionDocenteProyecto() {
        this.filterDirectoresDTO = new ArrayList<>();
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

    public Boolean getRenderedPnlDialogoOficio() {
        return renderedPnlDialogoOficio;
    }

    public void setRenderedPnlDialogoOficio(Boolean renderedPnlDialogoOficio) {
        this.renderedPnlDialogoOficio = renderedPnlDialogoOficio;
    }

    public DocumentoCarreraDTO getOficioPertinenciaDTO() {
        return oficioPertinenciaDTO;
    }

    public void setOficioPertinenciaDTO(DocumentoCarreraDTO oficioPertinenciaDTO) {
        this.oficioPertinenciaDTO = oficioPertinenciaDTO;
    }

    public Boolean isRenderedPnlDialogoFe() {
        return renderedPnlDialogoFe;
    }

    public void setRenderedPnlDialogoFe(Boolean renderedPnlDialogoFe) {
        this.renderedPnlDialogoFe = renderedPnlDialogoFe;
    }

}
