/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.unl.sigett.session;

import edu.unl.sigett.entity.PlazoEvaluacionTribunal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PlazoEvaluacionTribunalFacade extends AbstractFacade<PlazoEvaluacionTribunal> implements PlazoEvaluacionTribunalFacadeLocal {
    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlazoEvaluacionTribunalFacade() {
        super(PlazoEvaluacionTribunal.class);
    }
    
}
