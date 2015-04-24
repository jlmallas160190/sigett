/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Pertinencia;
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
public class PertinenciaFacade extends AbstractFacade<Pertinencia> implements PertinenciaFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PertinenciaFacade() {
        super(Pertinencia.class);
    }

    @Override
    public List<Pertinencia> buscarPertinenciasPorDocenteProyecto(Long docenteProyectoId) {
        try {
            Query query = em.createQuery("Select p from Pertinencia p WHERE " + "(p.docenteProyectoId.id=:id AND p.esActivo=TRUE)");
            query.setParameter("id", docenteProyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
