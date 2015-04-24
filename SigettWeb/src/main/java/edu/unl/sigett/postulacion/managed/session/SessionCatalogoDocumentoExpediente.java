/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.postulacion.managed.session;

import edu.unl.sigett.entity.CatalogoDocumentoExpediente;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionCatalogoDocumentoExpediente implements Serializable {

    private CatalogoDocumentoExpediente catalogoDocumentoExpediente;

    public SessionCatalogoDocumentoExpediente() {
        this.catalogoDocumentoExpediente = new CatalogoDocumentoExpediente();
    }

    public CatalogoDocumentoExpediente getCatalogoDocumentoExpediente() {
        return catalogoDocumentoExpediente;
    }

    public void setCatalogoDocumentoExpediente(CatalogoDocumentoExpediente catalogoDocumentoExpediente) {
        this.catalogoDocumentoExpediente = catalogoDocumentoExpediente;
    }

}
