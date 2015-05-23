/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.TemaProyectoDao;
import edu.unl.sigett.entity.TemaProyecto;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class TemaProyectoDaoImplement extends AbstractDao<TemaProyecto> implements TemaProyectoDao {

    public TemaProyectoDaoImplement() {
        super(TemaProyecto.class);
    }

    @Override
    public List<TemaProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT t FROM TemaProyecto t WHERE " + "(t.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<TemaProyecto> buscar(TemaProyecto temaProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT t FROM TemaProyecto t where");
        if (temaProyecto.getProyectoId() != null) {
            sql.append(" t.proyectoId=:proyectoId ");
            parametros.put("proyectoId", temaProyecto.getProyectoId());
        }
        if (temaProyecto.getEsActual() != null) {
            sql.append(" and t.actual=:actual ");
            parametros.put("actual", temaProyecto.getEsActual());
        }
        if (temaProyecto.getTemaId() != null) {
            sql.append(" and t.tema=:tema ");
            parametros.put("tema", temaProyecto.getTemaId());
        }
        final Query q = em.createQuery(sql.toString());
        for (String key : parametros.keySet()) {
            q.setParameter(key, parametros.get(key));
        }
        return q.getResultList();
    }
}
