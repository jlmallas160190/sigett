/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EvaluacionTribunal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EvaluacionTribunalFacade extends AbstractFacade<EvaluacionTribunal> implements EvaluacionTribunalFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluacionTribunalFacade() {
        super(EvaluacionTribunal.class);
    }

    @Override
    public List<EvaluacionTribunal> buscarPorTribunal(Long tribunalId) {
        try {
            Query query = em.createQuery("SELECT ev FROM EvaluacionTribunal ev WHERE" + " (ev.tribunalId.id=:id)");
            query.setParameter("id", tribunalId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<EvaluacionTribunal> buscarPorUsuarioCarrera(Long usuarioId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("sustentacionesPorUsuarioCarrera");
            storedProcedureQuery.setParameter("usuarioId", usuarioId);
            storedProcedureQuery.execute();
            List<EvaluacionTribunal> result = (List<EvaluacionTribunal>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
