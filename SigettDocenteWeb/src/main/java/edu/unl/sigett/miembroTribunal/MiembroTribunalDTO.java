/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.miembroTribunal;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.MiembroTribunal;

/**
 *
 * @author jorge-luis
 */
    public class MiembroTribunalDTO {

    private MiembroTribunal miembroTribunal;
    private Docente docente;
    private Persona persona;

    public MiembroTribunalDTO() {
    }

    public MiembroTribunalDTO(MiembroTribunal miembroTribunal, Docente docente, Persona persona) {
        this.miembroTribunal = miembroTribunal;
        this.docente = docente;
        this.persona = persona;
    }

    public MiembroTribunal getMiembroTribunal() {
        return miembroTribunal;
    }

    public void setMiembroTribunal(MiembroTribunal miembroTribunal) {
        this.miembroTribunal = miembroTribunal;
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
