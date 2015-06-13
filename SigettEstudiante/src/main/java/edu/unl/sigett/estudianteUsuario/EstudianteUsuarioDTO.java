/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.estudianteUsuario;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Estudiante;
import java.io.Serializable;
import org.jlmallas.seguridad.entity.Rol;
import org.jlmallas.seguridad.entity.Usuario;

/**
 *
 * @author jorge-luis
 */
public class EstudianteUsuarioDTO implements Serializable {

    private Usuario usuario;
    private Estudiante estudiante;
    private Persona persona;
    private Rol rolSeleccionado;

    public EstudianteUsuarioDTO() {
    }

    public EstudianteUsuarioDTO(Usuario usuario, Estudiante estudiante, Persona persona, Rol rolSeleccionado) {
        this.usuario = usuario;
        this.estudiante = estudiante;
        this.persona = persona;
        this.rolSeleccionado = rolSeleccionado;
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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Rol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(Rol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

}
