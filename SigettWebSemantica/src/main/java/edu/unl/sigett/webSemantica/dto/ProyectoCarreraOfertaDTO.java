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
public class ProyectoCarreraOfertaDTO implements Serializable {

    private Long id;
    private OfertaAcademicaDTO ofertaAcademicaDTO;
    private ProyectoDTO proyectoDTO;
    private CarreraDTO carreraDTO;
    private Individual individual;

    public ProyectoCarreraOfertaDTO(Long id, OfertaAcademicaDTO ofertaAcademicaDTO, ProyectoDTO proyectoDTO, CarreraDTO carreraDTO) {
        this.id = id;
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
        this.proyectoDTO = proyectoDTO;
        this.carreraDTO = carreraDTO;
    }

    public ProyectoCarreraOfertaDTO() {
    }

    public OfertaAcademicaDTO getOfertaAcademicaDTO() {
        return ofertaAcademicaDTO;
    }

    public void setOfertaAcademicaDTO(OfertaAcademicaDTO ofertaAcademicaDTO) {
        this.ofertaAcademicaDTO = ofertaAcademicaDTO;
    }

    public ProyectoDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

    public CarreraDTO getCarreraDTO() {
        return carreraDTO;
    }

    public void setCarreraDTO(CarreraDTO carreraDTO) {
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
