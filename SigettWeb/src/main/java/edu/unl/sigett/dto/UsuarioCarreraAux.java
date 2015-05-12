/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dto;

import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.UsuarioCarrera;
import org.jlmallas.seguridad.entity.Usuario;

/**
 *
 * @author jorge-luis
 */
public class UsuarioCarreraAux {

    private UsuarioCarrera usuarioCarrera;
    private Usuario usuario;
    private Carrera carrera;

    public UsuarioCarreraAux(UsuarioCarrera usuarioCarrera, Usuario usuario, Carrera carrera) {
        this.usuarioCarrera = usuarioCarrera;
        this.usuario = usuario;
        this.carrera = carrera;
    }

    public UsuarioCarreraAux() {
    }

    public UsuarioCarrera getUsuarioCarrera() {
        return usuarioCarrera;
    }

    public void setUsuarioCarrera(UsuarioCarrera usuarioCarrera) {
        this.usuarioCarrera = usuarioCarrera;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

}
