/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.dto;

import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class LineaInvestigacionDTO implements Serializable{

    private Long id;
    private String nombre;

    public LineaInvestigacionDTO() {
    }

    public LineaInvestigacionDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

}
