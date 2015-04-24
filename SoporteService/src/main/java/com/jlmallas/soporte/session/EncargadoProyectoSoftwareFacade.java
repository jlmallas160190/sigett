/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jlmallas.soporte.session;

import com.jlmallas.soporte.entity.EncargadoProyectoSoftware;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EncargadoProyectoSoftwareFacade extends AbstractFacade<EncargadoProyectoSoftware> implements EncargadoProyectoSoftwareFacadeLocal {
    @PersistenceContext(unitName = "soportePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EncargadoProyectoSoftwareFacade() {
        super(EncargadoProyectoSoftware.class);
    }
    
}
