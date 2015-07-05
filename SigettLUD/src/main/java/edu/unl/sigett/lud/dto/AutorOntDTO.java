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
public class AutorOntDTO extends PersonaOntDTO implements Serializable {

    private Long id;
    private Individual individual;

    public AutorOntDTO(Long id) {
        this.id = id;
    }

    public AutorOntDTO(Long id, String nombres, String apellidos, String fechaNacimiento, String genero, String email,String uri) {
        super(nombres, apellidos, fechaNacimiento, genero, email,uri);
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
