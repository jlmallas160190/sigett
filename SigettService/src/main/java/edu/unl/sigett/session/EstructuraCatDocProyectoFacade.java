/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstructuraCatDocProyecto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstructuraCatDocProyectoFacade extends AbstractFacade<EstructuraCatDocProyecto> implements EstructuraCatDocProyectoFacadeLocal {
    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstructuraCatDocProyectoFacade() {
        super(EstructuraCatDocProyecto.class);
    }
    
}
