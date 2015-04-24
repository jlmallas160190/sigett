/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.DocenteCarrera;
import edu.unl.sigett.entity.DirectorProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDirectorProyecto implements Serializable {

    private DirectorProyecto directorProyecto;
    private DocenteCarrera docenteCarrera;
    private Persona persona;

    public SessionDirectorProyecto() {
        this.persona = new Persona();
        this.directorProyecto = new DirectorProyecto();
        this.directorProyecto = new DirectorProyecto();
    }

    public DirectorProyecto getDirectorProyecto() {
        return directorProyecto;
    }

    public void setDirectorProyecto(DirectorProyecto directorProyecto) {
        this.directorProyecto = directorProyecto;
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

}
