/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.adjudicacion.session;

import edu.unl.sigett.entity.Prorroga;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionProrroga implements Serializable {

    private Prorroga prorroga;

    public SessionProrroga() {
        this.prorroga = new Prorroga();
    }

    public Prorroga getProrroga() {
        return prorroga;
    }

    public void setProrroga(Prorroga prorroga) {
        this.prorroga = prorroga;
    }

}
