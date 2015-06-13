/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.estudianteUsuario;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEstudianteUsuario implements Serializable {

    private EstudianteUsuarioDTO estudianteUsuarioDTO;

    private Boolean validarEstudianteWS;

    public SessionEstudianteUsuario() {
        this.estudianteUsuarioDTO = new EstudianteUsuarioDTO();
    }

    public EstudianteUsuarioDTO getEstudianteUsuarioDTO() {
        return estudianteUsuarioDTO;
    }

    public void setEstudianteUsuarioDTO(EstudianteUsuarioDTO estudianteUsuarioDTO) {
        this.estudianteUsuarioDTO = estudianteUsuarioDTO;
    }

    public Boolean getValidarEstudianteWS() {
        return validarEstudianteWS;
    }

    public void setValidarEstudianteWS(Boolean validarEstudianteWS) {
        this.validarEstudianteWS = validarEstudianteWS;
    }

}
