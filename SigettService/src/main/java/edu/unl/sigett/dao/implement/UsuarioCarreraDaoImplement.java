/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.UsuarioCarreraDao;
import edu.unl.sigett.entity.UsuarioCarrera;
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
public class UsuarioCarreraDaoImplement extends AbstractDao<UsuarioCarrera> implements UsuarioCarreraDao {

    public UsuarioCarreraDaoImplement() {
        super(UsuarioCarrera.class);
    }

    @Override
    public List<UsuarioCarrera> buscarPorUsuario(Long usuarioId) {
        try {
            Query query = em.createQuery("SELECT uc from UsuarioCarrera uc WHERE " + "(uc.usuarioId=:id)");
            query.setParameter("id", usuarioId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UsuarioCarrera> buscar(UsuarioCarrera usuarioCarrera) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT uc from UsuarioCarrera uc WHERE 1=1 ");
        if (usuarioCarrera.getCarreraId() != null) {
            sql.append(" and uc.carreraId=:carreraId");
            parametros.put("carreraId", usuarioCarrera.getCarreraId());
            existeFiltro = Boolean.TRUE;
        }
        if (usuarioCarrera.getUsuarioId() != null) {
            sql.append(" and uc.usuarioId=:usuarioId");
            parametros.put("usuarioId", usuarioCarrera.getUsuarioId());
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
