/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.ParametroCatalogoEvaluacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ParametroCatalogoEvaluacionFacade extends AbstractDao<ParametroCatalogoEvaluacion> implements ParametroCatalogoEvaluacionFacadeLocal {

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
