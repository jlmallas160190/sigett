/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class LineaInvestigacionCarreraFacade extends AbstractFacade<LineaInvestigacionCarrera> implements LineaInvestigacionCarreraFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaInvestigacionCarreraFacade() {
        super(LineaInvestigacionCarrera.class);
    }

    @Override
    public List<LineaInvestigacionCarrera> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT lc FROM  LineaInvestigacionCarrera lc WHERE" + " (lc.carreraId=:carreraId)");
            query.setParameter("carreraId", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<LineaInvestigacionCarrera> buscarPorLineaInvestigacion(Long lineaId) {
        try {
            Query query = em.createQuery("SELECT lc FROM  LineaInvestigacionCarrera lc WHERE" + " (lc.lineaInvestigacionId.id=:id)");
            query.setParameter("id", lineaId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
