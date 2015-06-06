/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteProyecto;

import com.jlmallas.comun.entity.Persona;
import edu.unl.sigett.entity.DocenteProyecto;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class DocenteProyectoDTO implements Serializable {

    private DocenteProyecto docenteProyecto;
    private Persona persona;

    public DocenteProyectoDTO() {
    }

    public DocenteProyectoDTO(DocenteProyecto docenteProyecto, Persona persona) {
        this.docenteProyecto = docenteProyecto;
        this.persona = persona;
    }

    public DocenteProyecto getDocenteProyecto() {
        return docenteProyecto;
    }

    public void setDocenteProyecto(DocenteProyecto docenteProyecto) {
        this.docenteProyecto = docenteProyecto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
