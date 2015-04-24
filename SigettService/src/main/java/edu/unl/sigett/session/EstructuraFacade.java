/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.session;

import edu.unl.sigett.entity.Estructura;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class EstructuraFacade extends AbstractFacade<Estructura> implements EstructuraFacadeLocal {
    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstructuraFacade() {
        super(Estructura.class);
    }
    
}
