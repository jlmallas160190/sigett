/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ProyectoOntDTO implements Serializable {

    private Long id;
    private String tema;
    private String fechaCreacion;
    private String tipo;
    private String estado;
    private Individual individual;
    private String uri;
    private String autor;
    private String docente;
    private String lineaInvestigacion;
    private String carrera;
    private String nivelAcademico;
    private Integer carreraId;
    private Long ofertaAcademicaId;

    public ProyectoOntDTO() {
    }

    public ProyectoOntDTO(Long id, String tema, String fechaCreacion, String tipo, String estado, String uri) {
        this.id = id;
        this.tema = tema;
        this.fechaCreacion = fechaCreacion;
        this.tipo = tipo;
        this.estado = estado;
        this.uri = uri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public String getLineaInvestigacion() {
        return lineaInvestigacion;
    }

    public void setLineaInvestigacion(String lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public Integer getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Integer carreraId) {
        this.carreraId = carreraId;
    }

    public Long getOfertaAcademicaId() {
        return ofertaAcademicaId;
    }

    public void setOfertaAcademicaId(Long ofertaAcademicaId) {
        this.ofertaAcademicaId = ofertaAcademicaId;
    }

}
