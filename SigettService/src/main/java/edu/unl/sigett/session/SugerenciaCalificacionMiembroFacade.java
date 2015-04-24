/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.SugerenciaCalificacionMiembro;
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
public class SugerenciaCalificacionMiembroFacade extends AbstractFacade<SugerenciaCalificacionMiembro> implements SugerenciaCalificacionMiembroFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SugerenciaCalificacionMiembroFacade() {
        super(SugerenciaCalificacionMiembro.class);
    }

    @Override
    public List<SugerenciaCalificacionMiembro> buscarPorCalificacioMiembro(Long calificacionMiembroId) {
        try {
            Query query = em.createQuery("SELECT s FROM SugerenciaCalificacionMiembro s WHERE " + "(s.calificacionMiembroId.id=:id and s.esActivo=TRUE)");
            query.setParameter("id", calificacionMiembroId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
