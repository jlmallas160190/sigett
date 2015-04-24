/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoOficio;
import edu.unl.sigett.entity.OficioCarrera;
import java.util.ArrayList;
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
public class CatalogoOficioFacade extends AbstractFacade<CatalogoOficio> implements CatalogoOficioFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CatalogoOficioFacade() {
        super(CatalogoOficio.class);
    }

    @Override
    public CatalogoOficio buscarPorCodigo(String codigo) {
        List<CatalogoOficio> catalogoOficios = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM CatalogoOficio c WHERE" + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            catalogoOficios = query.getResultList();
            return !catalogoOficios.isEmpty() ? catalogoOficios.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
