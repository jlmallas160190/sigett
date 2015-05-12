/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.jlmallas.academico.dao.AbstractDao;
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
public class CoordinadorPeriodoFacade extends AbstractDao<CoordinadorPeriodo> implements CoordinadorPeriodoFacadeLocal {

    public CoordinadorPeriodoFacade() {
        super(CoordinadorPeriodo.class);
    }

    @Override
    public List<CoordinadorPeriodo> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT c from CoordinadorPeriodo c WHERE " + "(c.periodoId.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public CoordinadorPeriodo buscarVigente(Integer carreraId) {
        try {
            List<CoordinadorPeriodo> coordinadorPeriodos = new ArrayList<>();
            Query query = em.createQuery("SELECT c from CoordinadorPeriodo c WHERE " + "(c.periodoId.carreraId.id=:id and c.esVigente=TRUE)");
            query.setParameter("id", carreraId);
            coordinadorPeriodos = query.getResultList();
            return !coordinadorPeriodos.isEmpty() ? coordinadorPeriodos.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
