/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.comun.controlador;

import edu.unl.sigett.comun.managed.session.SessionCatalogoDuracion;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import edu.unl.sigett.entity.CatalogoDuracion;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import edu.unl.sigett.session.CatalogoDuracionFacadeLocal;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class AdministrarCatalogoDuracion implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;
    @Inject
    private SessionCatalogoDuracion sessionCatalogoDuracion;
    @EJB
    private CatalogoDuracionFacadeLocal catalogoDuracionFacadeLocal;
    private String catalogoDuracion;

    public AdministrarCatalogoDuracion() {
    }

    //<editor-fold defaultstate="collapsed" desc="METODOS CRUD">
    public CatalogoDuracion seleccionaCatalogoDuracion() {
        CatalogoDuracion cat = null;
        try {
            int pos = catalogoDuracion.indexOf(":");
            CatalogoDuracion cd = catalogoDuracionFacadeLocal.find(Integer.parseInt(catalogoDuracion.substring(0, pos)));
            if (cd != null) {
                cat = cd;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        return cat;
    }

    public void catalogoDuracionActual(ConfiguracionProyecto configuracionProyecto) {
        try {
            if (configuracionProyecto.getCodigo().equalsIgnoreCase("CD")) {
                Integer id = Integer.parseInt(configuracionProyecto.getValor());
                catalogoDuracion = catalogoDuracionFacadeLocal.find(id).toString();
            }
        } catch (Exception e) {
        }
    }

    public List<CatalogoDuracion> buscarActivos() {
        try {
            return catalogoDuracionFacadeLocal.buscarActivos();
        } catch (Exception e) {
        }
        return null;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SET Y GET">

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
    }

    public SessionCatalogoDuracion getSessionCatalogoDuracion() {
        return sessionCatalogoDuracion;
    }

    public void setSessionCatalogoDuracion(SessionCatalogoDuracion sessionCatalogoDuracion) {
        this.sessionCatalogoDuracion = sessionCatalogoDuracion;
    }

    public String getCatalogoDuracion() {
        return catalogoDuracion;
    }

    public void setCatalogoDuracion(String catalogoDuracion) {
        this.catalogoDuracion = catalogoDuracion;
    }
//</editor-fold>
}
