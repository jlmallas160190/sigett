/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.academico.docenteCarrera;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Docente;

/**
 *
 * @author jorge-luis
 */
public class DocentePersonaDTO {

    private Docente docente;
    private Persona persona;

    public DocentePersonaDTO() {
    }

    public DocentePersonaDTO(Docente docente, Persona persona) {
        this.docente = docente;
        this.persona = persona;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
