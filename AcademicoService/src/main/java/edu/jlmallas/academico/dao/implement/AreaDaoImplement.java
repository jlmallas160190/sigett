/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.AreaDao;
import edu.jlmallas.academico.entity.Area;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class AreaDaoImplement extends AbstractDao<Area> implements AreaDao {

    public AreaDaoImplement() {
        super(Area.class);
    }

    @Override
    public List<Area> buscarPorCriterio(Area area) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select a from Area a where 1=1 ");
        if (area.getNombre() != null) {
            sql.append(" and a.nombre=:nombre ");
            parametros.put("nombre", area.getNombre());
        }
        if (area.getSigla() != null) {
            sql.append(" and a.sigla=:sigla ");
            parametros.put("sigla", area.getSigla());
        }
        sql.append(" order by a.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
