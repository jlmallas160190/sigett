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
public class DirectorProyectoDTO implements Serializable {

    private DocenteDTO docenteDTO;
    private ProyectoDTO proyectoDTO;

    public DocenteDTO getDocenteDTO() {
        return docenteDTO;
    }

    public void setDocenteDTO(DocenteDTO docenteDTO) {
        this.docenteDTO = docenteDTO;
    }

    public ProyectoDTO getProyectoDTO() {
        return proyectoDTO;
    }

    public void setProyectoDTO(ProyectoDTO proyectoDTO) {
        this.proyectoDTO = proyectoDTO;
    }

}
