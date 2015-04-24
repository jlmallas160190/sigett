/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.CatalogoDocumentoProyecto;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCatalogoDocumentoProyecto implements Serializable {

    private CatalogoDocumentoProyecto catalogoDocumentoProyecto;

    public SessionCatalogoDocumentoProyecto() {
        this.catalogoDocumentoProyecto = new CatalogoDocumentoProyecto();
    }

    public CatalogoDocumentoProyecto getCatalogoDocumentoProyecto() {
        return catalogoDocumentoProyecto;
    }

    public void setCatalogoDocumentoProyecto(CatalogoDocumentoProyecto catalogoDocumentoProyecto) {
        this.catalogoDocumentoProyecto = catalogoDocumentoProyecto;
    }

}
