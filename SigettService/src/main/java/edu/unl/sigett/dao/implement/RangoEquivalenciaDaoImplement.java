/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RangoEquivalenciaDao;
import edu.unl.sigett.entity.RangoEquivalencia;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RangoEquivalenciaDaoImplement extends AbstractDao<RangoEquivalencia> implements RangoEquivalenciaDao {

    public RangoEquivalenciaDaoImplement() {
        super(RangoEquivalencia.class);
    }

    @Override
    public List<RangoEquivalencia> buscar(RangoEquivalencia rangoEquivalencia) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT r FROM RangoEquivalencia r  WHERE 1=1");
        if (rangoEquivalencia.getRangoNotaId() != null) {
            sql.append(" and r.rangoNotaId=:rangoNotaId ");
            parametros.put("rangoNotaId", rangoEquivalencia.getRangoNotaId());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
