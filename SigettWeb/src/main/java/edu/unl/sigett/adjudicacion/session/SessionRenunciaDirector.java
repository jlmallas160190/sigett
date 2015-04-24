/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.session;

import edu.unl.sigett.entity.RenunciaDirector;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionRenunciaDirector implements Serializable {

    private RenunciaDirector renunciaDirector;

    public SessionRenunciaDirector() {
        this.renunciaDirector = new RenunciaDirector();
    }

    public RenunciaDirector getRenunciaDirector() {
        return renunciaDirector;
    }

    public void setRenunciaDirector(RenunciaDirector renunciaDirector) {
        this.renunciaDirector = renunciaDirector;
    }

}
