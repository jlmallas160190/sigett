/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import org.jlmallas.seguridad.entity.Usuario;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.UsuarioCarrera;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionUsuarioCarrera implements Serializable {

    private UsuarioCarrera usuarioCarrera;
    private Carrera carrera;
    private Usuario usuario;

    public SessionUsuarioCarrera() {
        this.usuario= new Usuario();
        this.usuarioCarrera = new UsuarioCarrera();
        this.carrera=new Carrera();
    }

    public UsuarioCarrera getUsuarioCarrera() {
        return usuarioCarrera;
    }

    public void setUsuarioCarrera(UsuarioCarrera usuarioCarrera) {
        this.usuarioCarrera = usuarioCarrera;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
