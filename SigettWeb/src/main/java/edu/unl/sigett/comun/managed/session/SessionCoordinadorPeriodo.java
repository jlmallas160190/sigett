/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCoordinadorPeriodo implements Serializable {

    private CoordinadorPeriodo coordinadorPeriodo;
    private Persona persona;

    public SessionCoordinadorPeriodo() {
        this.persona = new Persona();
        this.coordinadorPeriodo = new CoordinadorPeriodo();
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

}
