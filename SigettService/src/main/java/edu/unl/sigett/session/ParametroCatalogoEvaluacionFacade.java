/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ParametroCatalogoEvaluacionFacade extends AbstractFacade<ParametroCatalogoEvaluacion> implements ParametroCatalogoEvaluacionFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametroCatalogoEvaluacionFacade() {
        super(ParametroCatalogoEvaluacion.class);
    }

    @Override
    public List<ParametroCatalogoEvaluacion> buscarPorCatalogoEvaluacion(Integer catalogoEvaluacionId) {
        try {
            Query query = em.createQuery("SELECT pce FROM ParametroCatalogoEvaluacion pce WHERE " + "(pce.catalogoEvaluacionId.id=:id)");
            query.setParameter("id", catalogoEvaluacionId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
