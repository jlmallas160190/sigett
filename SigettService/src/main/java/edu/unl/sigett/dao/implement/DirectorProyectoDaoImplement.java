/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DirectorProyectoDao;
import edu.unl.sigett.entity.DirectorProyecto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DirectorProyectoDaoImplement extends AbstractDao<DirectorProyecto> implements DirectorProyectoDao {

    public DirectorProyectoDaoImplement() {
        super(DirectorProyecto.class);
    }

//    @Override
//    public List<DirectorProyecto> buscarPorProyecto(Long proyectoId) {
//        try {
//            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.proyectoId.id=:id and dp.estadoDirectorId.id!=2)");
//            query.setParameter("id", proyectoId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<DirectorProyecto> buscarPorProyectoEstadoAsignado(Long proyectoId) {
//        try {
//            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.proyectoId.id=:id and dp.estadoDirectorId.id=1)");
//            query.setParameter("id", proyectoId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<DirectorProyecto> buscarPorDocente(Long docenteId) {
//        try {
//            Query query = em.createQuery("SELECT dp FROM DirectorProyecto dp WHERE " + "(dp.directorId.docenteCarrera.docenteId.id=:id and dp.estadoDirectorId.id!=2)");
//            query.setParameter("id", docenteId);
//            return query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    @Override
//    public List<DirectorProyecto> buscarPorDocenteOferta(String ci, Long ofertaId) {
//        try {
//            StoredProcedureQuery storedProcedureQuery = em.createNamedStoredProcedureQuery("directorProyectoPorDocenteOferta");
//            storedProcedureQuery.setParameter("ci", ci);
//            storedProcedureQuery.setParameter("ofertaId", ofertaId);
//            storedProcedureQuery.execute();
//            List<DirectorProyecto> result = (List<DirectorProyecto>) storedProcedureQuery.getResultList();
//            return result;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
    @Override
    public List<DirectorProyecto> buscar(DirectorProyecto directorProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT dp FROM DirectorProyecto dp where 1=1");
        if (directorProyecto.getDirectorId() != null) {
            sql.append(" and dp.directorId=:directorId ");
            parametros.put("directorId", directorProyecto.getDirectorId());
            existeFiltro = Boolean.TRUE;
        }
        if (directorProyecto.getEstadoDirectorId() != null) {
            sql.append(" and dp.estadoDirectorId=:estadoDirectorId ");
            parametros.put("estadoDirectorId", directorProyecto.getEstadoDirectorId());
            existeFiltro = Boolean.TRUE;
        }
        if (directorProyecto.getProyectoId() != null) {
            sql.append(" and dp.proyectoId=:proyectoId ");
            parametros.put("proyectoId", directorProyecto.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return new ArrayList<>();
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
