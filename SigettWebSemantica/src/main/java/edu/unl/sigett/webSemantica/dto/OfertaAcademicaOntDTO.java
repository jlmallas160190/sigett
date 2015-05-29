/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jorge-luis
 */
public class OfertaAcademicaOntDTO implements Serializable {

    private Long id;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private PeriodoAcademicoOntDTO periodoAcademicoDTO;
    private Individual individual;

    public OfertaAcademicaOntDTO(Long id, String nombre, Date fechaInicio, Date fechaFin, PeriodoAcademicoOntDTO periodoAcademicoDTO) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.periodoAcademicoDTO = periodoAcademicoDTO;
    }

    public OfertaAcademicaOntDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public PeriodoAcademicoOntDTO getPeriodoAcademicoDTO() {
        return periodoAcademicoDTO;
    }

    public void setPeriodoAcademicoDTO(PeriodoAcademicoOntDTO periodoAcademicoDTO) {
        this.periodoAcademicoDTO = periodoAcademicoDTO;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}
