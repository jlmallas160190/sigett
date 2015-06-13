/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.NivelDao;
import edu.jlmallas.academico.entity.Nivel;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class NivelDaoImplement extends AbstractDao<Nivel> implements NivelDao {

    public NivelDaoImplement() {
        super(Nivel.class);
    }

    @Override
    public List<Nivel> buscar(Nivel nivel) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select n from Nivel n where 1=1 ");
        if (nivel.getNombre() != null) {
            sql.append(" and n.nombre=:nombre");
            parametros.put("nombre", nivel.getNombre());
        }
        sql.append(" order by n.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }

}
