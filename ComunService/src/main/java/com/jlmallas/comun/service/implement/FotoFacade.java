/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.entity.Foto;
import com.jlmallas.comun.service.AbstractFacade;
import com.jlmallas.comun.service.FotoFacadeLocal;
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
public class FotoFacade extends AbstractFacade<Foto> implements FotoFacadeLocal {

    @PersistenceContext(unitName = "comunPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FotoFacade() {
        super(Foto.class);
    }

    @Override
    public List<Foto> buscarPorPersona(Long id) {
        try {
            Query query = em.createQuery("SELECT f FROM Foto f WHERE " + " (f.personaId.id=:id)");
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Foto fotoActual(Long personaId) {
        List<Foto> fotos = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT f FROM Foto f WHERE " + " (f.personaId.id=:id and f.esActual=TRUE)");
            query.setParameter("id", personaId);
            fotos = query.getResultList();
            return !fotos.isEmpty() ? fotos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
