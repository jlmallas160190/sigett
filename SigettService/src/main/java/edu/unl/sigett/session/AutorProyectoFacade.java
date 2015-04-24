/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.AutorProyecto;
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
public class AutorProyectoFacade extends AbstractFacade<AutorProyecto> implements AutorProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AutorProyectoFacade() {
        super(AutorProyecto.class);
    }

    @Override
    public List<AutorProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT a FROM AutorProyecto a WHERE " + "(a.estadoAutorId.id!=10 AND a.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<AutorProyecto> buscarPorEstudiante(Long estudianteId) {
        try {
            Query query = em.createQuery("SELECT a FROM AutorProyecto a WHERE " + "(a.aspiranteId.estudianteCarrera.estudianteId.id=:id)");
            query.setParameter("id", estudianteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
