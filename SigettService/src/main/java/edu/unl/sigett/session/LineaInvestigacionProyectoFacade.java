/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.LineaInvestigacionProyecto;
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
public class LineaInvestigacionProyectoFacade extends AbstractFacade<LineaInvestigacionProyecto> implements LineaInvestigacionProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaInvestigacionProyectoFacade() {
        super(LineaInvestigacionProyecto.class);
    }

    @Override
    public List<LineaInvestigacionProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("Select lp from LineaInvestigacionProyecto lp WHERE " + "(lp.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<LineaInvestigacionProyecto> buscarPorLi(Long lineaInvestigacionId) {
        try {
            Query query = em.createQuery("Select lp from LineaInvestigacionProyecto lp WHERE " + "(lp.lineaInvestigacionId.id=:id)");
            query.setParameter("id", lineaInvestigacionId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
