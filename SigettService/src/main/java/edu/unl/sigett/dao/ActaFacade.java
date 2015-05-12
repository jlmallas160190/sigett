/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.Acta;
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
public class ActaFacade extends AbstractDao<Acta> implements ActaFacadeLocal {

    public ActaFacade() {
        super(Acta.class);
    }

    @Override
    public List<Acta> buscarPorEvaluacionTribunal(Long evaluacionTribunalId) {
        try {
            Query query = em.createQuery("SELECT a FROM Acta a WHERE " + "(a.evaluacionTribunal.id=:id)");
            query.setParameter("id", evaluacionTribunalId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Acta buscarPorEvaluacionCategoria(Long evaluacionId, Integer categoriaId) {
        try {
            List<Acta> actas = new ArrayList<>();
            Query query = em.createQuery("SELECT a FROM Acta a WHERE " + "(a.evaluacionTribunal.id=:id and a.categoriaActaId.id=:categoriaId)");
            query.setParameter("id", evaluacionId);
            query.setParameter("categoriaId", categoriaId);
            actas = query.getResultList();
            return !actas.isEmpty() ? actas.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
