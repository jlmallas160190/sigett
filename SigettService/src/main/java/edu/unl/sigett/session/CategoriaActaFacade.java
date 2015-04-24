/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.CatalogoActa;
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
public class CategoriaActaFacade extends AbstractFacade<CatalogoActa> implements CategoriaActaFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaActaFacade() {
        super(CatalogoActa.class);
    }

    @Override
    public CatalogoActa buscarPorCodigo(String codigo) {
        List<CatalogoActa> categorias = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT c FROM CategoriaActa c WHERE" + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            categorias = query.getResultList();
            return !categorias.isEmpty() ? categorias.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
