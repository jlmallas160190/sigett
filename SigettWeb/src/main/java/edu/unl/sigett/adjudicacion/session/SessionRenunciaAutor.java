/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.session;

import edu.unl.sigett.entity.RenunciaAutor;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionRenunciaAutor implements Serializable {

    private RenunciaAutor renunciaAutor;

    public SessionRenunciaAutor() {
        this.renunciaAutor = new RenunciaAutor();
    }

    public RenunciaAutor getRenunciaAutor() {
        return renunciaAutor;
    }

    public void setRenunciaAutor(RenunciaAutor renunciaAutor) {
        this.renunciaAutor = renunciaAutor;
    }

}
