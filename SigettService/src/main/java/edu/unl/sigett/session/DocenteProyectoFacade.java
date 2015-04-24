/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DocenteProyecto;
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
public class DocenteProyectoFacade extends AbstractFacade<DocenteProyecto> implements DocenteProyectoFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocenteProyectoFacade() {
        super(DocenteProyecto.class);
    }

    @Override
    public List<DocenteProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT dp FROM DocenteProyecto dp WHERE " + "(dp.esActivo=TRUE AND dp.proyectoId.id=:id) order by dp.fecha DESC");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DocenteProyecto> buscarProyectosPorDocente(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT dp FROM DocenteProyecto dp WHERE " + "(dp.esActivo=TRUE AND dp.docenteId.id=:id) order by (dp.fecha,dp.proyectoId.estadoProyectoId.id)");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public DocenteProyecto buscarOficioInformeDocenteProyeecto(Long docenteProyectoId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("oficioDocenteProyecto");
            storedProcedureQuery.setParameter("dpId", docenteProyectoId);
            storedProcedureQuery.execute();
            List<DocenteProyecto> result = (List<DocenteProyecto>) storedProcedureQuery.getResultList();
            return !result.isEmpty() ? result.get(0) : null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DocenteProyecto> buscarSinPertinencia(Integer carreraId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectoSinPertinencia");
            storedProcedureQuery.setParameter("carreraId", carreraId);
            storedProcedureQuery.execute();
            List<DocenteProyecto> result = (List<DocenteProyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DocenteProyecto> buscarDocenteProyectoParaPertinencia(String ci) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("proyectosParaPertinencia");
            storedProcedureQuery.setParameter("ci", ci);
            storedProcedureQuery.execute();
            List<DocenteProyecto> result = (List<DocenteProyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DocenteProyecto> buscarPorDocentePeriodo(String ci, Long ofertaId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("docenteProyectoPorDocenteOferta");
            storedProcedureQuery.setParameter("ci", ci);
            storedProcedureQuery.setParameter("ofertaId", ofertaId);
            storedProcedureQuery.execute();
            List<DocenteProyecto> result = (List<DocenteProyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
