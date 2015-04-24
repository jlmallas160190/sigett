/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import com.jlmallas.seguridad.entity.Rol;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionRol implements Serializable {

    private Rol rol;

    public SessionRol() {
        this.rol = new Rol();
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
