/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoEvento;
import edu.unl.sigett.entity.CatalogoOficio;
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
public class CatalogoEventoFacade extends AbstractFacade<CatalogoEvento> implements CatalogoEventoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CatalogoEventoFacade() {
        super(CatalogoEvento.class);
    }

    @Override
    public CatalogoEvento buscarPorCodigo(String codigo) {
        List<CatalogoEvento> catalogos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoEvento c WHERE" + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            catalogos = query.getResultList();
            return !catalogos.isEmpty() ? catalogos.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
