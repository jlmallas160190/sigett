/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.coordinadorPeriodo;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.entity.Docente;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class CoordinadorPeriodoDTO implements Serializable {

    private CoordinadorPeriodo coordinadorPeriodo;
    private Persona persona;
    private Docente docente;

    public CoordinadorPeriodoDTO() {
    }

    public CoordinadorPeriodoDTO(CoordinadorPeriodo coordinadorPeriodo, Persona persona, Docente docente) {
        this.coordinadorPeriodo = coordinadorPeriodo;
        this.persona = persona;
        this.docente = docente;
    }

    public CoordinadorPeriodo getCoordinadorPeriodo() {
        return coordinadorPeriodo;
    }

    public void setCoordinadorPeriodo(CoordinadorPeriodo coordinadorPeriodo) {
        this.coordinadorPeriodo = coordinadorPeriodo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

}
