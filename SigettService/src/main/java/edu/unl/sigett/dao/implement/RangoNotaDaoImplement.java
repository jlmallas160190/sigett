/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.RangoNotaDao;
import edu.unl.sigett.entity.RangoNota;
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
public class RangoNotaDaoImplement extends AbstractDao<RangoNota> implements RangoNotaDao {

    public RangoNotaDaoImplement() {
        super(RangoNota.class);
    }

    @Override
    public List<RangoNota> buscar(RangoNota rangoNota) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT r from RangoNota r WHERE 1=1");
        if (rangoNota.getId() != null) {
            sql.append(" and r.id=:id ");
            parametros.put("id", rangoNota.getId());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
