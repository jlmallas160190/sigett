/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.session;

import edu.unl.sigett.entity.EstadoActividad;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionEstadoActividad implements Serializable {

    private EstadoActividad estadoActividad;

    public SessionEstadoActividad() {
        this.estadoActividad = new EstadoActividad();
    }

    public EstadoActividad getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(EstadoActividad estadoActividad) {
        this.estadoActividad = estadoActividad;
    }

}
