/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Tribunal;
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
public class TribunalFacade extends AbstractFacade<Tribunal> implements TribunalFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TribunalFacade() {
        super(Tribunal.class);
    }

    @Override
    public List<Tribunal> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT t FROM Tribunal t WHERE" + " (t.proyectoId.id=:id and t.esActivo=TRUE)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
