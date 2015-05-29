/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class ProyectoCarreraOfertaOntDTO implements Serializable {

    private Long id;
    private OfertaAcademicaOntDTO ofertaAcademicaDTO;
    private ProyectoOntDTO proyectoDTO;
    private CarreraOntDTO carreraDTO;
    private Individual individual;

    public ProyectoCarreraOfertaOntDTO(Long id, OfertaAcademicaOntDTO ofertaAcademicaDTO, ProyectoOntDTO proyectoDTO, CarreraOntDTO carreraDTO) {
        this.id = id;
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
        this.proyectoDTO = proyectoDTO;
        this.carreraDTO = carreraDTO;
    }

    public ProyectoCarreraOfertaOntDTO() {
    }

    public OfertaAcademicaOntDTO getOfertaAcademicaDTO() {
        return ofertaAcademicaDTO;
    }

    public void setOfertaAcademicaDTO(OfertaAcademicaOntDTO ofertaAcademicaDTO) {
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
    }

    public ProyectoOntDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoOntDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

    public CarreraOntDTO getCarreraDTO() {
        return carreraDTO;
    }

    public void setCarreraDTO(CarreraOntDTO carreraDTO) {
        this.carreraDTO = carreraDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

}
