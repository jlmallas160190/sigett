/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.Rol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RolDaoImplement extends AbstractDao<Rol> implements RolDao {

    public RolDaoImplement() {
        super(Rol.class);
    }

    @Override
    public List<Rol> buscar(final Rol rol) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select r from Rol r where 1=1 ");
        if (rol.getNombre() != null) {
            sql.append(" and (LOWER(r.nombre) like concat('%',LOWER(:nombre),'%'))");
            parametros.put("nombre", rol.getNombre());
        }
        sql.append(" order by r.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
