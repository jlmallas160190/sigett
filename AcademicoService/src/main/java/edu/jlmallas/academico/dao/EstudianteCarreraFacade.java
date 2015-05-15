/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.dao.implement.EstudianteCarreraFacadeLocal;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.EstudianteCarrera;
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
public class EstudianteCarreraFacade extends AbstractDao<EstudianteCarrera> implements EstudianteCarreraFacadeLocal {

    public EstudianteCarreraFacade() {
        super(EstudianteCarrera.class);
    }

    @Override
    public List<EstudianteCarrera> buscarPorEstudiante(Long estudianteId) {
        try {
            Query query = em.createQuery("SELECT ec FROM EstudianteCarrera ec WHERE" + " (ec.estudianteId.id=:id AND ec.esActivo=TRUE)");
            query.setParameter("id", estudianteId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<EstudianteCarrera> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT ec FROM EstudianteCarrera ec WHERE" + " (ec.carreraId.id=:id AND ec.esActivo=TRUE)");
            query.setParameter("id", carreraId);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
