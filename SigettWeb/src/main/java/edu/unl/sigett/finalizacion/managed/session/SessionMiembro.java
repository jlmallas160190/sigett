/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import com.jlmallas.comun.entity.Persona;
import edu.unl.sigett.entity.MiembroTribunal;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionMiembro implements Serializable {

    private MiembroTribunal miembro;
    private Persona persona;

    public SessionMiembro() {
        this.miembro = new MiembroTribunal();
        this.persona = new Persona();
    }

    public MiembroTribunal getMiembro() {
        return miembro;
    }

    public void setMiembro(MiembroTribunal miembro) {
        this.miembro = miembro;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
