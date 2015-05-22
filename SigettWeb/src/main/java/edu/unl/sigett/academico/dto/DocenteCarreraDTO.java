/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.dto;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.Director;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class DocenteCarreraDTO implements Serializable{

    private DocenteCarrera docenteCarrera;
    private Persona persona;
    private Director director;

    public DocenteCarreraDTO(DocenteCarrera docenteCarrera, Persona persona, Director director) {
        this.docenteCarrera = docenteCarrera;
        this.persona = persona;
        this.director = director;
    }

    public DocenteCarreraDTO() {
    }

    public DocenteCarrera getDocenteCarrera() {
        return docenteCarrera;
    }

    public void setDocenteCarrera(DocenteCarrera docenteCarrera) {
        this.docenteCarrera = docenteCarrera;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

}
