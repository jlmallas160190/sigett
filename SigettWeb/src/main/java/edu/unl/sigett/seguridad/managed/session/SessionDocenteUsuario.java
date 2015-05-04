/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.managed.session;

import org.jlmallas.seguridad.entity.Usuario;
import edu.jlmallas.academico.entity.Docente;
import edu.unl.sigett.entity.DocenteUsuario;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionDocenteUsuario implements Serializable {

    private DocenteUsuario docenteUsuario;
    private Usuario usuario;
    private Docente docente;

    public SessionDocenteUsuario() {
        this.docente = new Docente();
        this.usuario = new Usuario();
        this.docenteUsuario = new DocenteUsuario();
    }

    public DocenteUsuario getDocenteUsuario() {
        return docenteUsuario;
    }

    public void setDocenteUsuario(DocenteUsuario docenteUsuario) {
        this.docenteUsuario = docenteUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

}
