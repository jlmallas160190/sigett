/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacion;
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
public class LineaInvestigacionFacade extends AbstractFacade<LineaInvestigacion> implements LineaInvestigacionFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaInvestigacionFacade() {
        super(LineaInvestigacion.class);
    }

    @Override
    public List<LineaInvestigacion> buscarPorCriterio(String nombre) {
        try {
            Query query = em.createQuery("SELECT l FROM LineaInvestigacion l WHERE " + " (LOWER(l.nombre) like concat('%',LOWER(:nombre),'%'))");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }
}
