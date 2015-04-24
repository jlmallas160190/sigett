/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class ProyectoFacade extends AbstractFacade<Proyecto> implements ProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoFacade() {
        super(Proyecto.class);
    }

    @Override
    public List<Proyecto> buscarPorEstado() {
        try {
            Query query = em.createQuery("SELECT p FROM Proyecto p where " + " (p.estadoProyectoId.id=1 or p.estadoProyectoId.id=2 or p.estadoProyectoId.id=3)");
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarPorTema(String criterio) {
        try {
            Query query = em.createQuery("SELECT p FROM Proyecto p where " + " (LOWER(p.temaActual) like concat('%',LOWER(:criterio),'%'))");
            query.setParameter("criterio", criterio);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarByEstado(Integer estadoId) {
        try {
            Query query = em.createQuery("SELECT p FROM Proyecto p where " + " (p.estadoProyectoId.id=:id)");
            query.setParameter("id", estadoId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarByCarrera(Integer carreraId) {
        List<Proyecto> proyectos = new ArrayList<>();
        StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectoByCarrera");
        storedProcedureQuery.setParameter("car_id", carreraId);
        storedProcedureQuery.execute();
        List<Proyecto> result = (List<Proyecto>) storedProcedureQuery.getResultList();
        for (Proyecto p : result) {
            if (!proyectos.contains(p)) {
                proyectos.add(p);
            }
        }
        return proyectos;
    }

    @Override
    public List<Proyecto> buscarByLi(Long liId) {
        try {
            List<Proyecto> proyectos = new ArrayList<>();
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectoByLi");
            storedProcedureQuery.setParameter("li_id", liId);
            storedProcedureQuery.execute();
            List<Proyecto> result = (List<Proyecto>) storedProcedureQuery.getResultList();
            for (Proyecto p : result) {
                if (!proyectos.contains(p)) {
                    proyectos.add(p);
                }
            }
            return proyectos;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarByPeriodo(Integer periodoId) {
        try {
            List<Proyecto> proyectos = new ArrayList<>();
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectoByPeriodo");
            storedProcedureQuery.setParameter("per_id", periodoId);
            storedProcedureQuery.execute();
            List<Proyecto> result = (List<Proyecto>) storedProcedureQuery.getResultList();
            for (Proyecto p : result) {
                if (!proyectos.contains(p)) {
                    proyectos.add(p);
                }
            }
            return proyectos;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarPorCarreraEstado(Integer carreraId, String codigoEstado) {
        try {
            List<ProyectoCarreraOferta> proyectoCarreraOfertas = new ArrayList<>();
            List<Proyecto> proyectos = new ArrayList<>();
            Query query = em.createQuery("SELECT pc FROM ProyectoCarreraOferta pc where " + " (pc.proyectoId.estadoProyectoId.codigo=:codigo and pc.carreraId=:carreraId)");
            query.setParameter("codigo", codigoEstado);
            query.setParameter("carreraId", carreraId);
            proyectoCarreraOfertas = query.getResultList();
            for (ProyectoCarreraOferta pco : proyectoCarreraOfertas) {
                proyectos.add(pco.getProyectoId());
            }
            return proyectos;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Proyecto> buscarPorCulminar(Integer carreraId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectosPorCulminar");
            storedProcedureQuery.setParameter("carreraId", carreraId);
            storedProcedureQuery.execute();
            List<Proyecto> result = (List<Proyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public List<Proyecto> buscarCaducados(Integer carreraId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectosCaducados");
            storedProcedureQuery.setParameter("carreraId", carreraId);
            storedProcedureQuery.execute();
            List<Proyecto> result = (List<Proyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
        }
        return null;
    }

}
