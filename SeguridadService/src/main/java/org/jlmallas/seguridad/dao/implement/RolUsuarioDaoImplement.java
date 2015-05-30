/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlmallas.seguridad.dao.implement;

import java.util.ArrayList;
import java.util.HashMap;
import org.jlmallas.seguridad.entity.RolUsuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jlmallas.seguridad.dao.AbstractDao;
import org.jlmallas.seguridad.dao.RolUsuarioDao;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class RolUsuarioDaoImplement extends AbstractDao<RolUsuario> implements RolUsuarioDao {

    public RolUsuarioDaoImplement() {
        super(RolUsuario.class);
    }

    @Override
    public List<RolUsuario> buscarPorUsuario(Long usuarioId) {
        try {
            Query query = em.createQuery("SELECT ru from RolUsuario ru WHERE " + "(ru.usuarioId.id=:id)");
            query.setParameter("id", usuarioId);
            return query.getResultList();
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<RolUsuario> buscar(final RolUsuario rolUsuario) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT ru from RolUsuario ru WHERE 1=1 ");
        Boolean existeFiltro = Boolean.FALSE;
        if (rolUsuario.getRolId() != null) {
            sql.append(" and ru.rolId=:rolId");
            parametros.put("rolId", rolUsuario.getRolId());
            existeFiltro = Boolean.TRUE;
        }
        if (rolUsuario.getUsuarioId() != null) {
            sql.append(" and ru.usuarioId=:usuarioId");
            parametros.put("usuarioId", rolUsuario.getUsuarioId());
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
