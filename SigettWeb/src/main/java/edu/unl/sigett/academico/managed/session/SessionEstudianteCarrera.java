/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.unl.sigett.entity.Aspirante;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEstudianteCarrera implements Serializable {

    private EstudianteCarrera estudianteCarrera;
    private Persona persona;
    private Aspirante aspirante;

    public SessionEstudianteCarrera() {
        this.estudianteCarrera = new EstudianteCarrera();
        this.persona = new Persona();
        this.aspirante= new Aspirante();
    }

    public EstudianteCarrera getEstudianteCarrera() {
        return estudianteCarrera;
    }

    public void setEstudianteCarrera(EstudianteCarrera estudianteCarrera) {
        this.estudianteCarrera = estudianteCarrera;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

}
