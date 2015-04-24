/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import com.jlmallas.seguridad.entity.Usuario;
import edu.jlmallas.academico.entity.Estudiante;
import edu.unl.sigett.entity.EstudianteUsuario;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEstudianteUsuario implements Serializable {

    private EstudianteUsuario estudianteUsuario;
    private Usuario usuario;
    private Estudiante estudiante;

    public SessionEstudianteUsuario() {
        this.estudiante = new Estudiante();
        this.usuario = new Usuario();
        this.estudianteUsuario = new EstudianteUsuario();
    }

    public EstudianteUsuario getEstudianteUsuario() {
        return estudianteUsuario;
    }

    public void setEstudianteUsuario(EstudianteUsuario estudianteUsuario) {
        this.estudianteUsuario = estudianteUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

}
