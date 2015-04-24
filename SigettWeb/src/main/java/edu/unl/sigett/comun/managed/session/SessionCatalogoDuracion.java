/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import edu.unl.sigett.entity.CatalogoDuracion;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCatalogoDuracion implements Serializable{

    private CatalogoDuracion catalogoDuracion;

    public SessionCatalogoDuracion() {
        this.catalogoDuracion = new CatalogoDuracion();
    }

    public CatalogoDuracion getCatalogoDuracion() {
        return catalogoDuracion;
    }

    public void setCatalogoDuracion(CatalogoDuracion catalogoDuracion) {
        this.catalogoDuracion = catalogoDuracion;
    }

}
