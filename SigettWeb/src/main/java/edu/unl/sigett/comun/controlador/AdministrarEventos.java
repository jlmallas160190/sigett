/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarEventos")
@SessionScoped
public class AdministrarEventos implements Serializable {

    /**
     * Creates a new instance of AdministrarEventos
     */
    public AdministrarEventos() {
    }    
}
