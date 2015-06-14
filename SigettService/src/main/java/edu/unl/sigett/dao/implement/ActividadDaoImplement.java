/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.ActividadDao;
import edu.unl.sigett.entity.Actividad;
import java.math.BigDecimal;
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
public class ActividadDaoImplement extends AbstractDao<Actividad> implements ActividadDao {

    public ActividadDaoImplement() {
        super(Actividad.class);
    }

    @Override
    public List<Actividad> buscar(Actividad actividad) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT a from Actividad a WHERE 1=1 ");
        Boolean existeFiltro = Boolean.FALSE;
        if (actividad.getEsActivo() != null) {
            sql.append(" and a.esActivo=:activo");
            parametros.put("activo", actividad.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getCronogramaId() != null) {
            sql.append(" and a.cronogramaId=:cronogramaId");
            parametros.put("cronogramaId", actividad.getCronogramaId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getCronogramaId() != null) {
            sql.append(" and a.cronogramaId=:cronogramaId");
            parametros.put("cronogramaId", actividad.getCronogramaId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getEstadoId() != null) {
            sql.append(" and a.estadoId=:estadoId");
            parametros.put("estadoId", actividad.getEstadoId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getTipoId() != null) {
            sql.append(" and a.tipoId=:tipoId");
            parametros.put("tipoId", actividad.getTipoId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getPadreId() != null) {
            sql.append(" and a.padreId=:padreId");
            parametros.put("padreId", actividad.getPadreId());
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

    @Override
    public BigDecimal sumatoriaDuracion(Actividad actividad) {
        @SuppressWarnings("UnusedAssignment")
        BigDecimal sum = BigDecimal.ZERO;
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT SUM(a.duracion) from Actividad a WHERE 1=1 ");
        Boolean existeFiltro = Boolean.FALSE;
        if (actividad.getEsActivo() != null) {
            sql.append(" and a.esActivo=:activo");
            parametros.put("activo", actividad.getEsActivo());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getCronogramaId() != null) {
            sql.append(" and a.cronogramaId=:cronogramaId");
            parametros.put("cronogramaId", actividad.getCronogramaId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getCronogramaId() != null) {
            sql.append(" and a.cronogramaId=:cronogramaId");
            parametros.put("cronogramaId", actividad.getCronogramaId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getEstadoId() != null) {
            sql.append(" and a.estadoId=:estadoId");
            parametros.put("estadoId", actividad.getEstadoId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getTipoId() != null) {
            sql.append(" and a.tipoId=:tipoId");
            parametros.put("tipoId", actividad.getTipoId());
            existeFiltro = Boolean.TRUE;
        }
        if (actividad.getPadreId() != null) {
            sql.append(" and a.padreId=:padreId");
            parametros.put("padreId", actividad.getPadreId());
            existeFiltro = Boolean.TRUE;
        }
         if (actividad.getId() != null) {
            sql.append(" and a.id!=:id");
            parametros.put("id", actividad.getId());
            existeFiltro = Boolean.TRUE;
        }
        if (!existeFiltro) {
            return BigDecimal.ZERO;
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        sum = (BigDecimal) q.getSingleResult();
        if (sum == null) {
            return BigDecimal.ZERO;
        }
        return sum;
    }

}
