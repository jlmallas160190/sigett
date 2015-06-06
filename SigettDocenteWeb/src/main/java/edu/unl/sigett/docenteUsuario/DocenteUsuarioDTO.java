/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.docenteUsuario;

import com.jlmallas.comun.entity.Persona;
import edu.jlmallas.academico.entity.Docente;
import java.io.Serializable;
import org.jlmallas.seguridad.entity.Usuario;

/**
 *
 * @author jorge-luis
 */
public class DocenteUsuarioDTO implements Serializable {

    private Docente docente;
    private Usuario usuario;
    private Persona persona;

    public DocenteUsuarioDTO() {
    }

    public DocenteUsuarioDTO(Docente docente, Usuario usuario, Persona persona) {
        this.docente = docente;
        this.usuario = usuario;
        this.persona = persona;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
