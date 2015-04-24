/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.session;

import edu.unl.sigett.entity.CatalogoInformeProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCatalogoInformeProyecto implements Serializable {

    private CatalogoInformeProyecto catalogoInformeProyecto;

    public SessionCatalogoInformeProyecto() {
        this.catalogoInformeProyecto = new CatalogoInformeProyecto();
    }

    public CatalogoInformeProyecto getCatalogoInformeProyecto() {
        return catalogoInformeProyecto;
    }

    public void setCatalogoInformeProyecto(CatalogoInformeProyecto catalogoInformeProyecto) {
        this.catalogoInformeProyecto = catalogoInformeProyecto;
    }

}
