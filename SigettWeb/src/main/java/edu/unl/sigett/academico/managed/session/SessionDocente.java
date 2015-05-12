/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.academico.dto.DocentePersona;
import edu.unl.sigett.entity.Director;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocente implements Serializable {

    private Docente docente;
    private Director director;
    private DocentePersona docentePersona;
    private List<DocentePersona> docentePersonas;
    private boolean renderedBuscar;

    public SessionDocente() {
        this.docentePersona = new DocentePersona();
        this.docentePersonas = new ArrayList<>();
        this.renderedBuscar = false;
        this.docente = new Docente();
        this.director = new Director();
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public DocentePersona getDocentePersona() {
        return docentePersona;
    }

    public void setDocentePersona(DocentePersona docentePersona) {
        this.docentePersona = docentePersona;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public boolean isRenderedBuscar() {
        return renderedBuscar;
    }

    public void setRenderedBuscar(boolean renderedBuscar) {
        this.renderedBuscar = renderedBuscar;
    }

    public List<DocentePersona> getDocentePersonas() {
        return docentePersonas;
    }

    public void setDocentePersonas(List<DocentePersona> docentePersonas) {
        this.docentePersonas = docentePersonas;
    }

}
