/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstadoAutor;
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
public class EstadoAutorFacade extends AbstractFacade<EstadoAutor> implements EstadoAutorFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoAutorFacade() {
        super(EstadoAutor.class);
    }

    @Override
    public EstadoAutor buscarPorCodigo(String codigo) {
        List<EstadoAutor> estados = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM EstadoAutor e WHERE" + " (e.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            estados = query.getResultList();
            return !estados.isEmpty() ? estados.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
