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
public class DirectorProyectoOntDTO implements Serializable {

    private Long id;
    private DocenteOntDTO docenteDTO;
    private ProyectoOntDTO proyectoDTO;
    private Individual individual;
    private String uri;

    public DirectorProyectoOntDTO() {
    }

    public DirectorProyectoOntDTO(Long id, DocenteOntDTO docenteDTO, ProyectoOntDTO proyectoDTO, String uri) {
        this.id = id;
        this.docenteDTO = docenteDTO;
        this.proyectoDTO = proyectoDTO;
        this.uri = uri;
    }

    public DocenteOntDTO getDocenteDTO() {
        return docenteDTO;
    }

    public void setDocenteDTO(DocenteOntDTO docenteDTO) {
        this.docenteDTO = docenteDTO;
    }

    public ProyectoOntDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoOntDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
