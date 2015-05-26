/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocenteProyectoDao;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class DocenteProyectoDaoImplement extends AbstractDao<DocenteProyecto> implements DocenteProyectoDao {

    public DocenteProyectoDaoImplement() {
        super(DocenteProyecto.class);
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

    @Override
    public List<DocenteProyecto> buscar(final DocenteProyecto docenteProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT dp FROM DocenteProyecto dp where ");
        if (docenteProyecto.getDocenteId() != null) {
            sql.append(" and dp.docenteId=:docenteId ");
            parametros.put("docenteId", docenteProyecto.getDocenteId());
            existeFiltro = Boolean.TRUE;
        }
        if (docenteProyecto.getEsActivo() != null) {
            sql.append(" and dp.esActivo=:activo ");
            parametros.put("activo", docenteProyecto.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (docenteProyecto.getProyectoId() != null) {
            sql.append(" and dp.proyectoId=:proyecto ");
            parametros.put("proyecto", docenteProyecto.getProyectoId());
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