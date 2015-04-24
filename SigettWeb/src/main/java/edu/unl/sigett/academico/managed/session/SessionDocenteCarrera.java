/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.Director;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocenteCarrera implements Serializable {

    private DocenteCarrera docenteCarrera;
    private Persona persona;
    private Director director;

    public SessionDocenteCarrera() {
        this.persona = new Persona();
        this.director= new Director();
        this.docenteCarrera = new DocenteCarrera();
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
