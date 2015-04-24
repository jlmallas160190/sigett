/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.managed.session;

import edu.unl.sigett.entity.InformeProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionInformeProyecto implements Serializable {

    private InformeProyecto informeProyecto;

    public SessionInformeProyecto() {
        this.informeProyecto = new InformeProyecto();
    }

    public InformeProyecto getInformeProyecto() {
        return informeProyecto;
    }

    public void setInformeProyecto(InformeProyecto informeProyecto) {
        this.informeProyecto = informeProyecto;
    }

}
