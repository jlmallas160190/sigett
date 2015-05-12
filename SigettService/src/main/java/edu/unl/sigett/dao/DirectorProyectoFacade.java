/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DirectorProyecto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DirectorProyectoFacade extends AbstractDao<DirectorProyecto> implements DirectorProyectoFacadeLocal {

    public DirectorProyectoFacade() {
        super(DirectorProyecto.class);
    }

    @Override
    public List<DirectorProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.proyectoId.id=:id and dp.estadoDirectorId.id!=2)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DirectorProyecto> buscarPorProyectoEstadoAsignado(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.proyectoId.id=:id and dp.estadoDirectorId.id=1)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DirectorProyecto> buscarPorDocente(Long docenteId) {
        try {
            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.directorId.docenteCarrera.docenteId.id=:id and dp.estadoDirectorId.id!=2)");
            query.setParameter("id", docenteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<DirectorProyecto> buscarPorDocenteOferta(String ci, Long ofertaId) {
        try {
            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("directorProyectoPorDocenteOferta");
            storedProcedureQuery.setParameter("ci", ci);
            storedProcedureQuery.setParameter("ofertaId", ofertaId);
            storedProcedureQuery.execute();
            List<DirectorProyecto> result = (List<DirectorProyecto>) storedProcedureQuery.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
