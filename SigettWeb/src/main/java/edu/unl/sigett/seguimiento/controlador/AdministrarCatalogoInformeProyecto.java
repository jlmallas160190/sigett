/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import edu.unl.sigett.seguimiento.session.SessionCatalogoInformeProyecto;
import edu.unl.sigett.entity.CatalogoInformeProyecto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.session.CatalogoInformeProyectoFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarCatalogoInformeProyecto implements Serializable {

    @Inject
    private SessionCatalogoInformeProyecto sessionCatalogoInformeProyecto;
    @EJB
    private CatalogoInformeProyectoFacadeLocal catalogoInformeProyectoFacadeLocal;

    public AdministrarCatalogoInformeProyecto() {
    }

    public List<CatalogoInformeProyecto> buscarActivos() {
        List<CatalogoInformeProyecto> catalogoInformeProyectos = new ArrayList<>();
        try {
            catalogoInformeProyectos = catalogoInformeProyectoFacadeLocal.buscarActivos();
        } catch (Exception e) {
        }
        return catalogoInformeProyectos;
    }

    public SessionCatalogoInformeProyecto getSessionCatalogoInformeProyecto() {
        return sessionCatalogoInformeProyecto;
    }

    public void setSessionCatalogoInformeProyecto(SessionCatalogoInformeProyecto sessionCatalogoInformeProyecto) {
        this.sessionCatalogoInformeProyecto = sessionCatalogoInformeProyecto;
    }

}
