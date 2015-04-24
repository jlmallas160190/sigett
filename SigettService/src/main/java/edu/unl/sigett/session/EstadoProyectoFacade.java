/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.EstadoProyecto;
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
public class EstadoProyectoFacade extends AbstractFacade<EstadoProyecto> implements EstadoProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoProyectoFacade() {
        super(EstadoProyecto.class);
    }

    @Override
    public List<EstadoProyecto> buscarPorNombre(String nombre) {
        try {
            Query query = em.createNamedQuery("EstadoProyecto.findByNombre");
            query.setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public EstadoProyecto buscarPorCodigo(String codigo) {
        List<EstadoProyecto> estados = new ArrayList<>();
        try {
            Query query = em.createQuery("SELECT e FROM EstadoProyecto e WHERE" + " (e.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            estados = query.getResultList();
            return !estados.isEmpty() ? estados.get(0) : null;
        } catch (Exception e) {
        }
        return null;
    }
}
