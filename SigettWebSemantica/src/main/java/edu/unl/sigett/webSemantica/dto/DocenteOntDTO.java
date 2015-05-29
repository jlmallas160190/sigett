/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jorge-luis
 */
public class DocenteOntDTO extends PersonaOntDTO implements Serializable {

    private Long id;

    public DocenteOntDTO(Long id) {
        this.id = id;
    }

    public DocenteOntDTO(Long id, String nombres, String apellidos, Date fechaNacimiento, String genero, String email) {
        super(nombres, apellidos, fechaNacimiento, genero, email);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
