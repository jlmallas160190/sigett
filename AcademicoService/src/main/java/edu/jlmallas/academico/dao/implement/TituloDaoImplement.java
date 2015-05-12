/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.entity.Titulo;
import edu.jlmallas.academico.dao.TituloDao;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TituloDaoImplement extends AbstractDao<Titulo> implements TituloDao {

    public TituloDaoImplement() {
        super(Titulo.class);
    }

    @Override
    public List<Titulo> buscar(Titulo titulo) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select t from Titulo t where 1=1 ");
        if (titulo.getNombre() != null) {
            sql.append(" and t.nombre=:nombre ");
            parametros.put("nombre", titulo.getNombre());
        }

        sql.append(" order by t.nombre asc ");
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
