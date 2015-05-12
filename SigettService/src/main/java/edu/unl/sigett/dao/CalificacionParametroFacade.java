/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CalificacionParametro;
import java.util.ArrayList;
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
public class CalificacionParametroFacade extends AbstractDao<CalificacionParametro> implements CalificacionParametroFacadeLocal {

    public CalificacionParametroFacade() {
        super(CalificacionParametro.class);
    }

    @Override
    public List<CalificacionParametro> buscarPorCalificacionMiembro(Long calificacionMiembroId) {
        try {
            Query query = em.createQuery("SELECT cp FROM CalificacionParametro cp WHERE " + "(cp.calificacionMiembroId.id=:id)");
            query.setParameter("id", calificacionMiembroId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public CalificacionParametro buscarPorCalificacionMiembroParametro(Long calificacionMiembroId, Long parametroId) {
        try {
            List<CalificacionParametro> calificacionParametros = new ArrayList<>();
            Query query = em.createQuery("SELECT cp FROM CalificacionParametro cp WHERE " + "(cp.calificacionMiembroId.id=:id and cp.parametroCatalogoEvaluacion.id=:parametroId)");
            query.setParameter("id", calificacionMiembroId);
            query.setParameter("parametroId", parametroId);
            calificacionParametros = query.getResultList();
            return !calificacionParametros.isEmpty() ? calificacionParametros.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
