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
public class PeriodoAcademicoOntDTO implements Serializable {

    private Integer id;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private Individual individual;

    public PeriodoAcademicoOntDTO() {
    }

    public PeriodoAcademicoOntDTO(Integer id, String nombre, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}