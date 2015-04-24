/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Actividad;
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
public class ActividadFacade extends AbstractFacade<Actividad> implements ActividadFacadeLocal {

    @PersistenceContext(unitName = "sigettPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadFacade() {
        super(Actividad.class);
    }

    @Override
    public List<Actividad> buscarPorProyecto(Long cronogramaId) {
        try {
            Query query = em.createQuery("SELECT a FROM Actividad  a WHERE " + " (a.cronogramaId.id=:id) ORDER BY a.fechaInicio");
            query.setParameter("id", cronogramaId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Actividad> buscarPorRevisar(Long cronogramaId) {
        try {
            Query query = em.createQuery("SELECT a FROM Actividad  a WHERE " + "(a.estadoActividadId.id=2  AND a.cronogramaId.id=:id AND a.esActivo=TRUE ) ORDER BY a.fechaCulminacion DESC");
            query.setParameter("id", cronogramaId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Actividad> buscarRevisadas(Long cronogramaId) {
        try {
            Query query = em.createQuery("SELECT a Actividad FROM a WHERE " + "( a.estadoActividad.id=3 and a.cronogramaId.id=:id AND a.esActivo=TRUE) ORDER BY a.fechaInicio");
            query.setParameter("id", cronogramaId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<Actividad> buscarEnDesarrollo(Long cronogramaId) {
        try {
            Query query = em.createQuery("SELECT a Actividad FROM a WHERE " + "( a.estadoActividad.id=1 and a.cronogramaId.id=:id AND a.esActivo=TRUE) ORDER BY a.fechaInicio");
            query.setParameter("id", cronogramaId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public double sumatoriaActividades(Long actividadId, Long cronogramaId) {
        double sum = 0;
        try {
            Query query = em.createQuery("SELECT SUM(a.duracion) from Actividad a WHERE " + "(a.cronogramaId.id=:id AND a.id!=:actividadId AND a.id=a.actividadId and a.esActivo=TRUE)");
            query.setParameter("id", cronogramaId);
            query.setParameter("actividadId", actividadId);
            sum = (double) query.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
        }
        return sum;
    }

    @Override
    public double sumatoriaSubActividades(Long actividadId, Long actividadPadreId) {
        double sum = 0;
        try {
            Query query = em.createQuery("SELECT SUM(a.duracion) from Actividad a WHERE " + "(a.actividadId=:id AND a.id!=:id AND a.id!=:actividadId and a.esActivo=TRUE)");
            query.setParameter("id", actividadPadreId);
            query.setParameter("actividadId", actividadId);
            sum = (double) query.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
        }
        return sum;
    }

    @Override
    public List<Actividad> buscarPorDirectorProyecto(String numeroIdentificacion) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("actividadesByDirectorProyecto");
            storedProcedureQuery.setParameter("cedula", numeroIdentificacion);
            storedProcedureQuery.execute();
            List<Actividad> result = (List<Actividad>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Actividad> buscarPorRevisarDirectorProyecto(String numeroIdentificacion) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("actividadesRevisarByDirectorProyecto");
            storedProcedureQuery.setParameter("cedula", numeroIdentificacion);
            storedProcedureQuery.execute();
            List<Actividad> result = (List<Actividad>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Actividad> buscarSubActividades(Long actividadId) {
        try {
            Query query = em.createQuery("SELECT a FROM Actividad  a WHERE " + " (a.actividadId=:id) ORDER BY a.fechaInicio");
            query.setParameter("id", actividadId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Actividad> buscarPorAutorProyecto(String numeroIdentificacion) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("actividadesByAutorProyecto");
            storedProcedureQuery.setParameter("cedula", numeroIdentificacion);
            storedProcedureQuery.execute();
            List<Actividad> result = (List<Actividad>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
