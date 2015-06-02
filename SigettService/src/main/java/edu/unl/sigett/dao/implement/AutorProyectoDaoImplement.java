/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao.implement;

import edu.unl.sigett.dao.AbstractDao;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.unl.sigett.entity.AutorProyecto;
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
public class AutorProyectoDaoImplement extends AbstractDao<AutorProyecto> implements AutorProyectoDao {

    public AutorProyectoDaoImplement() {
        super(AutorProyecto.class);
    }

    @Override
    public List<AutorProyecto> buscarPorProyecto(Long proyectoId) {
        try {
            Query query = em.createQuery("SELECT a FROM AutorProyecto a WHERE " + "(a.estadoAutorId.id!=10 AND a.proyectoId.id=:id)");
            query.setParameter("id", proyectoId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<AutorProyecto> buscarPorEstudiante(Long estudianteId) {
        try {
            Query query = em.createQuery("SELECT a FROM AutorProyecto a WHERE " + "(a.aspiranteId.estudianteCarrera.estudianteId.id=:id)");
            query.setParameter("id", estudianteId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<AutorProyecto> buscar(AutorProyecto autorProyecto) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> parametros = new HashMap<>();
        sql.append("SELECT a FROM AutorProyecto a WHERE 1=1 ");
        Boolean existeFiltro = Boolean.FALSE;
        if (autorProyecto.getAspiranteId() != null) {
            sql.append(" and a.aspiranteId=:aspiranteId ");
            parametros.put("aspiranteId", autorProyecto.getAspiranteId());
            existeFiltro = Boolean.TRUE;
        }
        if (autorProyecto.getEstadoAutorId() != null) {
            sql.append(" and a.estadoAutorId!=:estado ");
            parametros.put("estado", autorProyecto.getEstadoAutorId());
            existeFiltro = Boolean.TRUE;
        }
        if (autorProyecto.getProyectoId() != null) {
            sql.append(" and a.proyectoId=:proyectoId ");
            parametros.put("proyectoId", autorProyecto.getProyectoId());
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
