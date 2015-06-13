/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao.implement;

import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.AbstractDao;
import edu.jlmallas.academico.dao.EstudianteDao;
import edu.jlmallas.academico.entity.Estudiante;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class EstudianteDaoImplement extends AbstractDao<Estudiante> implements EstudianteDao {

    public EstudianteDaoImplement() {
        super(Estudiante.class);
    }

    @Override
    public List<Estudiante> buscar(final Estudiante estudiante) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("Select e from Estudiante e where 1=1 ");
        if (estudiante.getEsActivo() != null) {
            sql.append(" and e.esActivo=:activo");
            parametros.put("activo", estudiante.getEsActivo());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
