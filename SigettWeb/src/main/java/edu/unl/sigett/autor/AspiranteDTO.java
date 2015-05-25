/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.autor.dto;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.unl.sigett.entity.Aspirante;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class AspiranteDTO implements Serializable{
    private Aspirante aspirante;
    private EstudianteCarrera estudianteCarrera;
    private Persona persona;

    public AspiranteDTO() {
    }

    public AspiranteDTO(Aspirante aspirante, EstudianteCarrera estudianteCarrera, Persona persona) {
        this.aspirante = aspirante;
        this.estudianteCarrera = estudianteCarrera;
        this.persona = persona;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
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
    
}
