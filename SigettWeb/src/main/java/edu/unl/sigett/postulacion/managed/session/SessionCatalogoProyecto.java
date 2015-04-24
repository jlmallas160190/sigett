/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.CatalogoProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCatalogoProyecto implements Serializable{

    private CatalogoProyecto catalogoProyecto;

    public SessionCatalogoProyecto() {
        catalogoProyecto = new CatalogoProyecto();
    }

    public CatalogoProyecto getCatalogoProyecto() {
        return catalogoProyecto;
    }

    public void setCatalogoProyecto(CatalogoProyecto catalogoProyecto) {
        this.catalogoProyecto = catalogoProyecto;
    }

}
