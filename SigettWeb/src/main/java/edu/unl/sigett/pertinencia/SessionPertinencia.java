/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.pertinencia;

import edu.unl.sigett.entity.Pertinencia;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionPertinencia implements Serializable {

    private Pertinencia pertinencia;

    public SessionPertinencia() {
        this.pertinencia = new Pertinencia();
    }

    public Pertinencia getPertinencia() {
        return pertinencia;
    }

    public void setPertinencia(Pertinencia pertinencia) {
        this.pertinencia = pertinencia;
    }

}
