/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.CalificacionMiembro;
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
public class CalificacionMiembroFacade extends AbstractDao<CalificacionMiembro> implements CalificacionMiembroFacadeLocal {

    public CalificacionMiembroFacade() {
        super(CalificacionMiembro.class);
    }

    @Override
    public List<CalificacionMiembro> buscarPorMiembro(String miembroId) {
        try {
            Query query = em.createQuery("SELECT cm FROM CalificacionMiembro cm WHERE " + "(cm.miembroId=:id and cm.esActivo=TRUE)");
            query.setParameter("id", miembroId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<CalificacionMiembro> buscarPorEvaluacionTribunal(Long evaluacionTribunalId) {
        try {
            Query query = em.createQuery("SELECT cm FROM CalificacionMiembro cm WHERE " + "(cm.evaluacionTribunal.id=:id and cm.esActivo=TRUE) order by cm.nota DESC");
            query.setParameter("id", evaluacionTribunalId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<CalificacionMiembro> buscarPorMiembroEvaluacionTribunal(String miembroId, Long evaluacionTribunalId) {
        try {
            Query query = em.createQuery("SELECT cm FROM CalificacionMiembro cm WHERE " + "(cm.evaluacionTribunal.id=:id and cm.miembroId=:miembroId and cm.esActivo=TRUE)");
            query.setParameter("id", evaluacionTribunalId);
            query.setParameter("miembroId", miembroId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
