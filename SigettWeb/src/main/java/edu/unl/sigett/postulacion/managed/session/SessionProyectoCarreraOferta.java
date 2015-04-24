/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionProyectoCarreraOferta implements Serializable {

    private ProyectoCarreraOferta proyectoCarreraOferta;

    public SessionProyectoCarreraOferta() {
        this.proyectoCarreraOferta = new ProyectoCarreraOferta();
    }

    public ProyectoCarreraOferta getProyectoCarreraOferta() {
        return proyectoCarreraOferta;
    }

    public void setProyectoCarreraOferta(ProyectoCarreraOferta proyectoCarreraOferta) {
        this.proyectoCarreraOferta = proyectoCarreraOferta;
    }

}
