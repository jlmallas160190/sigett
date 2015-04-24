/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.Proyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocenteProyecto implements Serializable {

    private DocenteProyecto docenteProyecto;
    private Persona persona;
    private Proyecto proyecto;
    private Docente docente;

    public SessionDocenteProyecto() {
        this.docenteProyecto = new DocenteProyecto();
        this.persona = new Persona();
        this.docente = new Docente();
        this.proyecto = new Proyecto();
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

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

}
