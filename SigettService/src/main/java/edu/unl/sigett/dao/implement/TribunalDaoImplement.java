/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.TribunalDao;
import edu.unl.sigett.entity.Tribunal;
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
public class TribunalDaoImplement extends AbstractDao<Tribunal> implements TribunalDao {

    public TribunalDaoImplement() {
        super(Tribunal.class);
    }

    @Override
    public List<Tribunal> buscar(final Tribunal tribunal) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        Boolean existeFiltro = Boolean.FALSE;
        sql.append("SELECT t FROM Tribunal t WHERE 1=1");
        if (tribunal.getProyectoId() != null) {
            sql.append(" and t.proyectoId=:proyectoId ");
            parametros.put("proyectoId", tribunal.getProyectoId());
            existeFiltro = Boolean.TRUE;
        }
        if (tribunal.getEsActivo() != null) {
            sql.append(" and t.esActivo=:activo ");
            parametros.put("activo", tribunal.getEsActivo());
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
