/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.Excepcion;
import com.jlmallas.soporte.entity.Objeto;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ExcepcionFacade extends AbstractFacade<Excepcion> implements ExcepcionFacadeLocal {

    @PersistenceContext(unitName = "soportePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcepcionFacade() {
        super(Excepcion.class);
    }

    @Override
    public Excepcion crearExcepcion(String usuario, String descripcion, Objeto obj) {
        Excepcion excepcion = new Excepcion();
        Calendar fechaActual = Calendar.getInstance();
        excepcion.setFecha(fechaActual.getTime());
        excepcion.setDescripcion(descripcion);
        excepcion.setObjetoId(obj);
        excepcion.setUsuario(usuario);
        return excepcion;
    }
}
