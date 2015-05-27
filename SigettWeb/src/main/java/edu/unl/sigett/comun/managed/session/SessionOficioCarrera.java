/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.managed.session;

import edu.unl.sigett.entity.DocumentoCarrera;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionOficioCarrera")
@SessionScoped
public class SessionOficioCarrera implements Serializable {

    private DocumentoCarrera oficioCarrera;

    public SessionOficioCarrera() {
        this.oficioCarrera = new DocumentoCarrera();
    }

    public DocumentoCarrera getOficioCarrera() {
        return oficioCarrera;
    }

    public void setOficioCarrera(DocumentoCarrera oficioCarrera) {
        this.oficioCarrera = oficioCarrera;
    }

}
