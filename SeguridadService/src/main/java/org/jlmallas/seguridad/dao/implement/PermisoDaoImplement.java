/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import org.jlmallas.seguridad.entity.Permiso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.PermisoDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class PermisoDaoImplement extends AbstractDao<Permiso> implements PermisoDao {

    public PermisoDaoImplement() {
        super(Permiso.class);
    }

    @Override
    public List<Permiso> buscar(final Permiso permiso) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT p from Permiso p WHERE 1=1 ");
        Boolean existeFiltro = Boolean.FALSE;
        if (permiso.getCodigo() == null) {
            sql.append(" and p.codigo=:codigo");
            parametros.put("codigo", permiso.getCodigo());
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
