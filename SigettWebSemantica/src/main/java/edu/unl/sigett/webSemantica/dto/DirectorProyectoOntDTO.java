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
public class DirectorProyectoOntDTO implements Serializable {

    private DocenteOntDTO docenteDTO;
    private ProyectoOntDTO proyectoDTO;

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

}
