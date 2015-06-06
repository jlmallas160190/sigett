/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.DocenteUsuarioDao;
import edu.unl.sigett.entity.DocenteUsuario;
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
public class DocenteUsuarioDaoImplement extends AbstractDao<DocenteUsuario> implements DocenteUsuarioDao {

    public DocenteUsuarioDaoImplement() {
        super(DocenteUsuario.class);
    }

    @Override
    public List<DocenteUsuario> buscar(DocenteUsuario docenteUsuario) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("Select d from DocenteUsuario d WHERE 1=1 ");
        if (docenteUsuario.getDocenteId() != null) {
            sql.append(" and d.docenteId=:docenteId");
            parametros.put("docenteId", docenteUsuario.getDocenteId());
            existeFiltro = Boolean.TRUE;
        }
        if (docenteUsuario.getId() != null) {
            sql.append(" and d.usuarioId=:usuarioId");
            parametros.put("usuarioId", docenteUsuario.getId());
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
