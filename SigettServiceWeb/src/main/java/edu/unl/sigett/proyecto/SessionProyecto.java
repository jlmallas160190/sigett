/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.proyecto;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.autorProyecto.AutorProyectoDTO;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import edu.unl.sigett.documentoProyecto.DocumentoProyectoDTO;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionProyecto")
@SessionScoped
public class SessionProyecto implements Serializable {

    private Proyecto proyectoSeleccionado;
    private Carrera carreraSeleccionada;
    private Proyecto proyectoSeleccionadoComplete;

    private List<Proyecto> proyectos;
    private List<Proyecto> filterProyectos;
    private List<Carrera> filterCarreras;
    private List<Carrera> carreras;
    private List<Item> estados;
    private List<Item> categorias;
    private List<Item> tipos;
    private List<Area> areas;
    private List<DocumentoProyectoDTO> documentosProyectoDTO;
    private List<DocumentoProyectoDTO> filterDocumentosProyectoDTO;
    private List<AutorProyectoDTO> autoresProyectoDTO;
    private List<AutorProyectoDTO> filterAutoresProyectoDTO;
    private List<DirectorProyectoDTO> directoresProyectoDTO;
    private List<DirectorProyectoDTO> filterDirectoresProyectoDTO;
    private List<OfertaAcademica> ofertaAcademicas;

    private String filtroBuscadorSemantico;

    public SessionProyecto() {
        this.filterAutoresProyectoDTO = new ArrayList<>();
        this.autoresProyectoDTO = new ArrayList<>();
        this.filterDirectoresProyectoDTO = new ArrayList<>();
        this.directoresProyectoDTO = new ArrayList<>();
        this.documentosProyectoDTO = new ArrayList<>();
        this.filterDocumentosProyectoDTO = new ArrayList<>();
        this.areas = new ArrayList<>();
        this.estados = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.tipos = new ArrayList<>();
        this.carreras = new ArrayList<>();
        this.filterCarreras = new ArrayList<>();
        this.proyectos = new ArrayList<>();
        this.filterProyectos = new ArrayList<>();
        this.ofertaAcademicas = new ArrayList<>();
    }

    public Proyecto getProyectoSeleccionadoComplete() {
        return proyectoSeleccionadoComplete;
    }

    public void setProyectoSeleccionadoComplete(Proyecto proyectoSeleccionadoComplete) {
        this.proyectoSeleccionadoComplete = proyectoSeleccionadoComplete;
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public String getFiltroBuscadorSemantico() {
        return filtroBuscadorSemantico;
    }

    public void setFiltroBuscadorSemantico(String filtroBuscadorSemantico) {
        this.filtroBuscadorSemantico = filtroBuscadorSemantico;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<Proyecto> getFilterProyectos() {
        return filterProyectos;
    }

    public void setFilterProyectos(List<Proyecto> filterProyectos) {
        this.filterProyectos = filterProyectos;
    }

    public Carrera getCarreraSeleccionada() {
        return carreraSeleccionada;
    }

    public void setCarreraSeleccionada(Carrera carreraSeleccionada) {
        this.carreraSeleccionada = carreraSeleccionada;
    }

    public List<Carrera> getFilterCarreras() {
        return filterCarreras;
    }

    public void setFilterCarreras(List<Carrera> filterCarreras) {
        this.filterCarreras = filterCarreras;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public List<Item> getEstados() {
        return estados;
    }

    public void setEstados(List<Item> estados) {
        this.estados = estados;
    }

    public List<Item> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Item> categorias) {
        this.categorias = categorias;
    }

    public List<Item> getTipos() {
        return tipos;
    }

    public void setTipos(List<Item> tipos) {
        this.tipos = tipos;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<DocumentoProyectoDTO> getDocumentosProyectoDTO() {
        return documentosProyectoDTO;
    }

    public void setDocumentosProyectoDTO(List<DocumentoProyectoDTO> documentosProyectoDTO) {
        this.documentosProyectoDTO = documentosProyectoDTO;
    }

    public List<DocumentoProyectoDTO> getFilterDocumentosProyectoDTO() {
        return filterDocumentosProyectoDTO;
    }

    public void setFilterDocumentosProyectoDTO(List<DocumentoProyectoDTO> filterDocumentosProyectoDTO) {
        this.filterDocumentosProyectoDTO = filterDocumentosProyectoDTO;
    }

    public List<AutorProyectoDTO> getAutoresProyectoDTO() {
        return autoresProyectoDTO;
    }

    public void setAutoresProyectoDTO(List<AutorProyectoDTO> autoresProyectoDTO) {
        this.autoresProyectoDTO = autoresProyectoDTO;
    }

    public List<AutorProyectoDTO> getFilterAutoresProyectoDTO() {
        return filterAutoresProyectoDTO;
    }

    public void setFilterAutoresProyectoDTO(List<AutorProyectoDTO> filterAutoresProyectoDTO) {
        this.filterAutoresProyectoDTO = filterAutoresProyectoDTO;
    }

    public List<DirectorProyectoDTO> getDirectoresProyectoDTO() {
        return directoresProyectoDTO;
    }

    public void setDirectoresProyectoDTO(List<DirectorProyectoDTO> directoresProyectoDTO) {
        this.directoresProyectoDTO = directoresProyectoDTO;
    }

    public List<DirectorProyectoDTO> getFilterDirectoresProyectoDTO() {
        return filterDirectoresProyectoDTO;
    }

    public void setFilterDirectoresProyectoDTO(List<DirectorProyectoDTO> filterDirectoresProyectoDTO) {
        this.filterDirectoresProyectoDTO = filterDirectoresProyectoDTO;
    }

    public List<OfertaAcademica> getOfertaAcademicas() {
        return ofertaAcademicas;
    }

    public void setOfertaAcademicas(List<OfertaAcademica> ofertaAcademicas) {
        this.ofertaAcademicas = ofertaAcademicas;
    }

}
