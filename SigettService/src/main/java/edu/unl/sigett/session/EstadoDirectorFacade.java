/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstadoDirector;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstadoDirectorFacade extends AbstractFacade<EstadoDirector> implements EstadoDirectorFacadeLocal {
    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoDirectorFacade() {
        super(EstadoDirector.class);
    }
    
}
