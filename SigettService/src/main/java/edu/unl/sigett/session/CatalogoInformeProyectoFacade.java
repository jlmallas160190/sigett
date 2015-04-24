/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoInformeProyecto;
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
public class CatalogoInformeProyectoFacade extends AbstractFacade<CatalogoInformeProyecto> implements CatalogoInformeProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CatalogoInformeProyectoFacade() {
        super(CatalogoInformeProyecto.class);
    }

    @Override
    public List<CatalogoInformeProyecto> buscarActivos() {
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoInformeProyecto c WHERE" + " (c.esActivo=TRUE)");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
