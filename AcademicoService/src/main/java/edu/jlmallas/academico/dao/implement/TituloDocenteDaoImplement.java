/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.TituloDocente;
import edu.jlmallas.academico.dao.TituloDocenteDao;
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
public class TituloDocenteDaoImplement extends AbstractDao<TituloDocente> implements TituloDocenteDao {

    public TituloDocenteDaoImplement() {
        super(TituloDocente.class);
    }

    @Override
    public List<TituloDocente> buscar(TituloDocente tituloDocente) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT td FROM TituloDocente td WHERE 1=1 ");
        if (tituloDocente.getTituloId().getNombre() != null) {
            sql.append(" and td.tituloId.nombre=:nombre ");
            parametros.put("nombre", tituloDocente.getTituloId().getNombre());
        }
        sql.append(" order by td.tituloId.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
