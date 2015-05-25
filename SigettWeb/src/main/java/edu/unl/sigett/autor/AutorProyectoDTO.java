/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autor.dto;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.unl.sigett.entity.Aspirante;
import edu.unl.sigett.entity.AutorProyecto;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class AutorProyectoDTO implements Serializable {

    private AutorProyecto autorProyecto;
    private Aspirante aspirante;
    private EstudianteCarrera estudianteCarrera;
    private Persona persona;

    public AutorProyectoDTO() {
    }

    public AutorProyectoDTO(AutorProyecto autorProyecto, Aspirante aspirante, EstudianteCarrera estudianteCarrera, Persona persona) {
        this.autorProyecto = autorProyecto;
        this.aspirante = aspirante;
        this.estudianteCarrera = estudianteCarrera;
        this.persona = persona;
    }

    public AutorProyecto getAutorProyecto() {
        return autorProyecto;
    }

    public void setAutorProyecto(AutorProyecto autorProyecto) {
        this.autorProyecto = autorProyecto;
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
