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
public class AutorProyectoDTO implements Serializable {

    private Long id;
    private ProyectoDTO proyectoDTO;
    private AutorOntDTO autorDTO;
    private Individual individual;

    public AutorProyectoDTO(Long id, ProyectoDTO proyectoDTO, AutorOntDTO autorDTO) {
        this.id = id;
        this.proyectoDTO = proyectoDTO;
        this.autorDTO = autorDTO;
    }

    public AutorProyectoDTO() {
    }

    public ProyectoDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

    public AutorOntDTO getAutorDTO() {
        return autorDTO;
    }

    public void setAutorDTO(AutorOntDTO autorDTO) {
        this.autorDTO = autorDTO;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
