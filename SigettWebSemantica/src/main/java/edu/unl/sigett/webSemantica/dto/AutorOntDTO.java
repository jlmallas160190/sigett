/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import com.hp.hpl.jena.ontology.Individual;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class AutorOntDTO extends PersonaDTO implements Serializable {

    private Long id;
    private static final Logger LOG = Logger.getLogger(AutorOntDTO.class.getName());
    private Individual individual;

    public AutorOntDTO(Long id) {
        this.id = id;
    }

    public AutorOntDTO(Long id, String nombres, String apellidos, Date fechaNacimiento, String genero, String email) {
        super(nombres, apellidos, fechaNacimiento, genero, email);
        this.id = id;
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
