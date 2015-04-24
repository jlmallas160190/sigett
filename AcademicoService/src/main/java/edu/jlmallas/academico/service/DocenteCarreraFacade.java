/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.dao.AbstractFacade;
import edu.jlmallas.academico.entity.DocenteCarrera;
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
public class DocenteCarreraFacade extends AbstractFacade<DocenteCarrera> implements DocenteCarreraFacadeLocal {

    public DocenteCarreraFacade() {
        super(DocenteCarrera.class);
    }

    @Override
    public List<DocenteCarrera> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT d from DocenteCarrera d WHERE " + "(d.carreraId.id=:id)");
            query.setParameter("id", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DocenteCarrera> buscarPorDocente(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT d from DocenteCarrera d WHERE " + "(d.docenteId.id=:id)");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
