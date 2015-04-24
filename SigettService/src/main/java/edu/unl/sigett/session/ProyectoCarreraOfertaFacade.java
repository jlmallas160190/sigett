/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.ProyectoCarreraOferta;
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
public class ProyectoCarreraOfertaFacade extends AbstractFacade<ProyectoCarreraOferta> implements ProyectoCarreraOfertaFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoCarreraOfertaFacade() {
        super(ProyectoCarreraOferta.class);
    }

    @Override
    public List<ProyectoCarreraOferta> buscarPorCarreraOferta(Integer carreraId, Integer periodoId) {
        try {
            Query query = em.createQuery("SELECT pco from ProyectoCarreraOferta pco WHERE" + " ( (pco.carreraId.id=:carreraId or pco.ofertaAcademicaId.periodoAcademicoId.id=:periodoId) and pco.esActivo=TRUE )");
            query.setParameter("carreraId", carreraId);
            query.setParameter("periodoId", periodoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ProyectoCarreraOferta> buscarPorCarrera(Integer carreraId) {
        try {
            Query query = em.createQuery("SELECT pco from ProyectoCarreraOferta pco WHERE" + " (pco.carreraId.id=:carreraId) order by pco.ofertaAcademicaId.fechaInicio DESC");
            query.setParameter("carreraId", carreraId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<ProyectoCarreraOferta> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT pco from ProyectoCarreraOferta pco WHERE" + " ( pco.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
