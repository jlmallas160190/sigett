/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.dao.implement;

import com.jlmallas.comun.entity.Pais;
import com.jlmallas.comun.dao.AbstractDao;
import com.jlmallas.comun.dao.PaisDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PaisDaoImplement extends AbstractDao<Pais> implements PaisDao {

    public PaisDaoImplement() {
        super(Pais.class);
    }

    @Override
    public List<Pais> buscar(final Pais pais) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT p FROM Pais p WHERE 1=1 ");
        if (pais.getNombre() != null) {
            sql.append(" and p.nombre=:nombre ");
            parametros.put("nombre", pais.getNombre());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
