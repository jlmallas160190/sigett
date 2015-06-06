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
    public List<DocenteProyecto> buscar(final DocenteProyecto docenteProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT dp FROM DocenteProyecto dp where 1=1");
        if (docenteProyecto.getDocenteCarreraId() != null) {
            sql.append(" and dp.docenteId=:docenteId ");
            parametros.put("docenteId", docenteProyecto.getDocenteCarreraId());
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
        if (docenteProyecto.getEstadoProyecto() != null) {
            sql.append(" and dp.proyectoId.estadoProyectoId=:esatadoProyecto ");
            parametros.put("estadoProyecto", docenteProyecto.getEstadoProyecto());
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
