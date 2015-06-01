/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.entity.PeriodoCoordinacion;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.PeriodoCoordinacionDao;
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
public class PeriodoCoordinacionDaoImplement extends AbstractDao<PeriodoCoordinacion> implements PeriodoCoordinacionDao {

    public PeriodoCoordinacionDaoImplement() {
        super(PeriodoCoordinacion.class);
    }

    @Override
    public List<PeriodoCoordinacion> buscar(PeriodoCoordinacion periodoCoordinacion) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT p from PeriodoCoordinacion p WHERE 1=1 ");
        if (periodoCoordinacion.getCarreraId() != null) {
            sql.append(" and p.carreraId=:carreraId ");
            parametros.put("carreraId", periodoCoordinacion.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (periodoCoordinacion.getEsActivo() != null) {
            sql.append(" and p.esActivo=:activo");
            parametros.put("activo", periodoCoordinacion.getEsActivo());
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
