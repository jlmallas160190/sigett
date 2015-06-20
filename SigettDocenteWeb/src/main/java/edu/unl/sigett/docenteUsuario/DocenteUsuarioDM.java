/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteUsuario;

import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.jlmallas.seguridad.entity.Usuario;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class DocenteUsuarioDM implements Serializable {

    private DocenteUsuarioDTO docenteUsuarioDTO;

    private Boolean validarDocenteWS;

    public DocenteUsuarioDM() {
        this.docenteUsuarioDTO = new DocenteUsuarioDTO(null, new Usuario(), null);
    }

    public DocenteUsuarioDTO getDocenteUsuarioDTO() {
        return docenteUsuarioDTO;
    }

    public void setDocenteUsuarioDTO(DocenteUsuarioDTO docenteUsuarioDTO) {
        this.docenteUsuarioDTO = docenteUsuarioDTO;
    }

    public Boolean getValidarDocenteWS() {
        return validarDocenteWS;
    }

    public void setValidarDocenteWS(Boolean validarDocenteWS) {
        this.validarDocenteWS = validarDocenteWS;
    }
}
