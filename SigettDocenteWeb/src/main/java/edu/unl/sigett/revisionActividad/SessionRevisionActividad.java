/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.revisionActividad;

import edu.unl.sigett.entity.RevisionActividad;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionRevisionActividad implements Serializable {

    private RevisionActividad revisionActividad;

    private Boolean renderedCrud;

    public SessionRevisionActividad() {
        this.revisionActividad = new RevisionActividad();
    }

    public RevisionActividad getRevisionActividad() {
        return revisionActividad;
    }

    public void setRevisionActividad(RevisionActividad revisionActividad) {
        this.revisionActividad = revisionActividad;
    }

    public Boolean getRenderedCrud() {
        return renderedCrud;
    }

    public void setRenderedCrud(Boolean renderedCrud) {
        this.renderedCrud = renderedCrud;
    }

}
